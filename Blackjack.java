import java.util.Random;
import java.util.Scanner;
import java.util.Locale;
import java.io.PrintStream;


public class Blackjack {

    // variables utilitaires de la class
    public static Scanner input = new Scanner(System.in).useLocale(Locale.US);
    public static PrintStream output = System.out;
    public static Random random = new Random();

    //----------------------------------------------------------------------------------------------------------------//
    //----------------------------------------------FONCTIONS PRINCIPALES---------------------------------------------//
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Entry Point permettant de récupérer le nombre de joueurs, le nombre de paquets de 52 cartes et
     * de lancer {@link #playGame(int, int) }.
     * A la fin du jeu, le couple de liste est récupéré, contenant le solde de départ de chaque joueur et le solde actuel de chaque joueur.
     * Le couple est envoyé dans la fonction {@link #àdefind } pour réaliser un affichage.
     * */
    public static void main(String[] args) {

        // input nb players
        int nbPlayer = askInfosInt(1, 6, "Donner le nombre de joueurs (entre 1 et 6) : ");

        // intput nb packs
        int nbPacks = askInfosInt(1, 8, "Donner le nombre de paquets de 52 cartes utilisés (entre 1 et 8) : ");

        // lancement d'une séance de jeu
        double [][] infos = playGame(nbPlayer, nbPacks);

        // affichage infos
        for(double[] ligne:infos){
            for(double elt:ligne)  {
                output.print(elt + " ");
            }
            output.println();
        }
    }


    /**
     * Fonction permettant de faire jouer x partie tant que les conditions le permettent.
     * @param nbPlayer nombre de joueurs
     * @param nbPacks nombre de paquet de 52 cartes
     * @return Une matrice de double correspondant au solde de départ et au solde de fin de chaque joueurs
     * @see #playRound(boolean[], double[], int[])
     * */
    public static double[][] playGame(int nbPlayer, int nbPacks) {

        // player online
        boolean[] active = new boolean[nbPlayer];
        for(int i = 0; i < nbPlayer; i++) {
            active[i] = true;
        }

        // get solde players
        double[] tabsoldeStart = getSoldePlayer(nbPlayer); // ne change pas
        double[] money = new double[nbPlayer]; // évolu tout au long de la game
        for(int i = 0; i < nbPlayer; i++) {
            money[i] = tabsoldeStart[i];
        }

        // make sabot de jeu
        int[] deck = generateCards(nbPacks);


        output.println("\nPREMIÈRE PARTIE\n"); // affichage

        // boucle qui lance partie après partie tant que c'est possible (mise possible & player en ligne)
        boolean newGame;
        do {
            newGame = playRound(active, money, deck); // un round (une partie)

            if (newGame) { // seulement si on est sur une nouvelle game, si cloture par une mise de 0 alors on n'affiche rien
                output.println("\nNOUVELLE PARTIE ?\n"); // affichage
            }
        } while(newGame);

        output.println("\nEt le combat cessa faute de combattants."); // affichage

        return new double[][]{tabsoldeStart, money}; // forme apprise dans un mail de test
    }



    //----------------------------------------------------------------------------------------------------------------//
    //----------------------------------------------FONCTIONS SECONDAIRES---------------------------------------------//
    //----------------------------------------------------------------------------------------------------------------//





    /**
     * Fonction permettant de demander un entier à l'utilisateur qui est compris entre deux bornes d'entier donné.
     * Redemande tant que la valeur donnée est en dehors des bornes
     * @param value1 la borne minimum
     * @param value2 la borne maximum
     * @param textInput text affiché lors de la demande de saisie à l'utilisateur
     * @return La valeur saisie par l'utilisateur, respectant
     * */
    public static int askInfosInt(int value1, int value2, String textInput) {
        int valeur;
        do {
            output.print(textInput);

            // try except pour éviter que le programme se coupe si l'entrée est autre chose qu'un entier
            try {
                valeur = input.nextInt(); // on demande la valeur
            } catch(Exception e) {
                valeur = -1;
            }

            if (valeur < value1  || valeur > value2) {
                output.println("Réponse incorrecte !");
                input.nextLine(); // pour clear le buffer d'entrée
            }
        } while (valeur < value1 || valeur > value2); // on boucle tant que c'est pas bon

        return valeur;
    }


    /**
     * Fonction permettant de récuperer tout les soldes de tout les joueurs de la partie.
     * @param nbPlayer nombre de joueurs
     * @return un tableau de double contenant tout les solde initiaux
     * */
    public static double[] getSoldePlayer(int nbPlayer) {
        double[] tabsolde = new double[nbPlayer]; // tableau vide
        for(int i = 0; i < nbPlayer; i++) {
            double solde;

            // demande du solde à chaque player
            do {
                output.print(String.format("Donner la solde en Euros que possède le joueur %d (entre 1.0 et 1000000.0) : ", i+1));

                //try except pour éviter que le script s'arette si la saisie n'est pas un double.
                try {
                    solde = input.nextDouble();
                } catch (Exception e) {
                    solde = 0;
                }
                if(solde < 1.0 || solde > 1000000.0) {
                    output.println("Réponse incorrecte !");
                    input.nextLine(); // pour clear le buffer d'entrée
                }
            } while (solde < 1.0 || solde > 1000000.0); // on boucle tant que c'est pas bon
            tabsolde[i] = solde;
        }

        return  tabsolde;
    }

    /**
     * Fonction permettant de collecter toutes les mises de chaque joueurs qui joue.
     * Si la mise n'est pas dans l'interval attendu alors elle st redemandé.
     * Si la mise est égale à 0, alors le joueurs n'est plus considéré comme en jeu et ne sera plus pris en compte
     * @param active tableau de tout les joueurs qui joue ou non
     * @param money tableau avec tout les soldes actuels des joueurs
     * @param bet tableau de toutes les mises de chaque joueurs qui joue
     * */
    public static void collectBets(boolean[] active, double[] money, double[] bet) {
        // get bet player
        double saisieBet;
        for(int i = 0; i < active.length; i++) {
            if(active[i] && money[i] > 0) {
                do {
                    output.print(String.format("Joueur %d , donne ta mise en Euros (entre 0.0 et %.2f ) : ", i+1, money[i]));

                    try {
                        saisieBet = input.nextDouble();
                    } catch (Exception e) {
                        saisieBet = -1.0;
                    }
                    if(saisieBet == 0.0) {
                        active[i] = false;
                    } else if(saisieBet < 0 || saisieBet > money[i]) {
                        output.println("Réponse incorrecte !");
                        input.nextLine(); // pour clear le buffer d'entrée
                    }
                } while ( saisieBet < 0 || saisieBet > money[i]); // on boucle tant que c'est pas bon

                bet[i] = saisieBet; // ajout le la mise dans la liste des mises
                money[i] -= saisieBet;
            }
        }


    }


    // ---------- Méthode utilitaires ---------- //
    public static void shuffleCards(int[] deck) {
        // méthode pour mélanger le deck
        int l = deck.length;
        for(int i = 0; i< l*2; i++) {
            int indice1 = random.nextInt(1, l-1);
            int indice2 = random.nextInt(1, l-1);

            int tempo = deck[indice1];
            deck[indice1] = deck[indice2];
            deck[indice2] = tempo;
        }
        deck[0] = deck.length-1; // reset de l'indice pour savoir qu'elle est la prochaine carte à piocher
    }

    public static int[] generateCards(int nbPacks) {
        // méthode pour former de deck de cartes
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}; // Jack 11 / Queen 12 / King 13

        int[] deck = new int[52*nbPacks +1]; // jeu de 52 cartes * n + 1 pour l'indice

        for(int i = 0; i < nbPacks; i++) {
            for(int j = 0; j< 4; j++) {
                for(int k = 0; k < 13; k++) {
                    deck[i*52 + j*13 + k +1] = values[k];
                }
            }
        }
        deck[0] = deck.length-1; // début pour tirer une carte |- a garder car test dessus  | redéfinie dans le schuffle
        return deck;
    }

    public static boolean playersRemain(boolean[] active) {
        // renvoie la présence d'au moins un joueur pour joueur
        for(boolean player:active) {
            if(player){
               return true;
            }
        }
        return false;
    }
    
    public static String cardName(int numero) {
        // à partir de la valeur d'in game d'une carte, retourne sa chaine str
        return switch (numero) {
            case 11 -> "valet";
            case 12 -> "dame";
            case 13 -> "roi";
            case 1 -> "as";
            default -> String.format("%d", numero);
        };
    }

    public static int getIntValueCard(int numero) {
        // à partir de la valeur in game d'une carte, retourne son nombre de point
        if (numero == 1) {
            return 11;
        } else if (numero > 10) {
            return 10;
        } else {
            return numero;
        }
    }

    public static String getMain(int[] mainPersonnage) {
        // retourne la chain str dela main d'un personnage. Possibilité de caché des élément en partant de la fin
        int nbCards = cardsNumber(mainPersonnage);
        String[] main = new String[nbCards];
        for(int i = 1; i <= nbCards; i++) {
            main[i-1] = cardName(mainPersonnage[i]);
        }

        String text = "";
        for(int i = 0; i < nbCards; i++) {
            String carac;
            if (i < nbCards -2) {
                carac = " , ";
            } else if (i == nbCards-2) {
                carac = " et ";
            } else {
                carac = "";
            }
            text += main[i] + carac;
        }
        return text;
    }


    public static void dealInitialCards(boolean[] playerIsActive, int[][] playerHand, int[] dealerHand, int[] deck) {
        int nbPlayer = playerIsActive.length;
        for(int i = 1; i <= 2; i++) { // nombre de carte
            for(int j = 0; j < nbPlayer; j++) {// affectation de la carte aux players
                drawCard(deck, playerHand[j]);
            }
            drawCard(deck, dealerHand); // afectation de la carte au croupier
        }
    }

    public static int drawCard(int[] deck, int[] hand) {
        // méthode qui tire la carte suivante du deck l'ajoute à la main et la renvois avec return
        int card = getNextCard(deck);
        hand[0]++; // ajout d'une carte dans le compteur
        hand[hand[0]] = card; // on ajoute la carte
        return card;
    }

    public static int getNextCard(int[] deck) {
        int card = deck[deck[0]];
        deck[0]--; // on ajoute 1 pour définir l'indice de la carete suivante lors du prochain tirage
        return card;
    }

    // retourne le nombre de cartes d'un tableau de cartes
    // (notez que T[0] est le nombre effectif de cartes et n'est donc pas une carte)
    public static int cardsNumber(int[] tab) {
        return tab[0];
    }


    public static double playerNewMoney(double pMoney, double pBet, int pScore, int dealerScore) {
        if (pScore > dealerScore) {
            if (pScore==22) {
                // le player a fait blackjack et le croupier non, il récupère 3 fois sa mise
                output.println(String.format("Tu gagnes, tu récupères 3 fois ta bet, soit %.2f", pBet*3));
                return pMoney + pBet * 3;
            }
            else {
                // le player gagne contre le croupier, il récupère 2.5 fois sa mise
                output.println(String.format("Tu gagnes, tu récupères 2.5 fois ta bet, soit %.2f", pBet*2.5));
                return pMoney + pBet * 2.5;
            }
        }
        else if (pScore == dealerScore) {
            // le player et le dealer ont fait blackJack, le player récupère sa mise
            output.println(String.format("Le croupier et toi avait fait BlackJack, tu récupères ta mise, soit %.2f Euros", pBet));
            return pMoney + pBet;
        }
        else {
            // le player a perdu contre le dealer, il ne récupère rien
            output.println("Tu perds contre le croupier, tu ne récupères rien.");
            return pMoney;
        }
    }

    public static int minScore(int[] hand) {
        int total = 0;

        for(int i = 1; i <= cardsNumber(hand); i++) {// on ajoute toutes les valeurs au max
            total += (hand[i] > 10) ? 10:hand[i];
        }

        return total;
    }

    public static int bestScore(int[] hand) {
        // permet de retourner la somme d'une main donnée
        int total = 0;
        int asCpt = 0;

        for(int i = 1; i <= cardsNumber(hand); i++) {// on ajoute toutes les valeurs au max
            if(hand[i] == 1) {
                asCpt ++;
                total += 11;
            } else if(hand[i] > 10 ) {
                total += 10;
            } else {
                total += hand[i];
            }
        }

        for(int i = 0; i < asCpt; i++) { // on retire pour que l'as compte 1 si c'est sup à 21
            if(total > 21) {
                total -= 10;
            }
        }

        return total;
    }


    public static boolean hasAnAce(int[] hand) {
        for(int card:hand) {
            if(card==1) {
                return true;
            }
        }
        return false;
    }


    public static int playDrawingPhase(int[] hand, int minScore, boolean hasAnAce, int bestScore, boolean isPlayer, int[] deck){

        if (isPlayer) { // c'est un player
            bestAffichagePlayerHand(0, minScore, bestScore);

            String reponse;
            do {

                output.print("Veux-tu tirer une carte [oui/non] ? ");
                reponse = input.next();

                while (!reponse.equalsIgnoreCase("oui") && !reponse.equalsIgnoreCase("non")) { // element robuste de upper et lower
                    output.println("Saisie incorrect !");
                    output.print("Veux-tu tirer une carte [oui/non] ? ");
                    reponse = input.next();
                }

                if(reponse.equalsIgnoreCase("oui")) {
                    int card = drawCard(deck, hand);

                    minScore = minScore(hand);
                    bestScore = bestScore(hand);

                    bestAffichagePlayerHand(card, minScore, bestScore);
                }
            } while(reponse.equalsIgnoreCase("oui") && bestScore < 21);

            return (bestScore < 22) ? bestScore:-1;
        }
        else { // c'est le croupier
            output.println(String.format("Il a %d points.", bestScore));

            while (bestScore < 17) {
                int card = drawCard(deck, hand);
                bestScore = bestScore(hand);
                output.println(String.format("Le croupier a tiré un %s. Il a %d points", cardName(card), bestScore));
            }

            return (bestScore < 22) ? bestScore:0;

        }
    }

    public static void displayGameInit(boolean[] playerIsActive, double[] playerMoney, double[] playerBet, int[][] playerHand, int dealerVisibleCard) {
        for(int i = 0; i < playerIsActive.length; i++) {
            if(playerIsActive[i]) {
                output.println(String.format("\nJoueur %d : solde = %.2f € / mise = %.2f € / cartes : %s ",i+1, playerMoney[i], playerBet[i], getMain(playerHand[i])));
            }
        }
        output.println(String.format("\nLe croupier a les cartes %s et ?", cardName(dealerVisibleCard)));

    }

    public static void playTurn(int[] allScores, boolean[] active, double[] money, double[] bet, int[][] cardPlayer, int[] dealerHand, int[] deck) {
        output.println("\nFaites vos jeux !\n"); // globale affichage

        //players
        for(int i = 0; i < active.length; i++) {
            if(active[i]) {
                allScores[i] = playerPlayTurn(i, money[i], bet[i], cardPlayer[i], deck);
            }
        }
        // dealer
        allScores[allScores.length-1] = dealerPlayTurn(dealerHand, deck);

    }

    public static int playerPlayTurn(int i, double pMoney, double pBet, int[] pHand, int[] deck) {
        output.println(String.format("\n--> Tour du joueur %d", i+1));
        displayPlayerGameState(i, pMoney, pBet, pHand);

        int minScore = minScore(pHand);
        int bestScore = bestScore(pHand);
        boolean hasAnAce = hasAnAce(pHand);

        if (bestScore == 21) {
            displayBlackJack();
            return 22;
        }
        else {
            return playDrawingPhase(pHand, minScore, hasAnAce, bestScore, true, deck);
        }
    }

    public static void bestAffichagePlayerHand(int card, int minScore, int bestScore) {

        if(card != 0) { // on a pioché une carte
            output.print(String.format("Tu as tiré un %s. ", cardName(card)));
        }

        // on affiche les pts que l'on a
        if (minScore != bestScore) {
            output.println(String.format("Tu as %d ou %d points", minScore, bestScore));
        } else {
            output.println(String.format("Tu as %d points", bestScore));
        }

    }

    public static void displayPlayerGameState(int i, double pMoney, double pBet,int[] phand) {
        output.println(String.format("Joueur %d : solde = %.2f € / mise = %.2f € / cartes : %s ",i+1, pMoney, pBet, getMain(phand)));
    }

    public static void displayBlackJack() {
        output.println("BlackJack !");
    }

    public static int dealerPlayTurn(int[] dealerHand, int[] deck) {
        //tour croupier
        output.println("\n--> Tour du croupier");
        output.println(String.format("Le croupier a les cartes %s", getMain(dealerHand)));
        return playDrawingPhase(dealerHand, minScore(dealerHand), hasAnAce(dealerHand),bestScore(dealerHand), false, deck); // croupier automatique
    }

    //----------------------------------------------------------------------------------------------------------------//
    //----------------------------------------------ZONE DE TRAVAIL PAS BO--------------------------------------------//
    //----------------------------------------------------------------------------------------------------------------//


    // méthodes à opti et à rendre propre
    public static boolean playRound(boolean[] active, double[] money, int[] deck) {
        // 0 Utilitaires

        int nbPlayer = active.length;

        // -----------------------------------------------------------------------------------------------------------//

        // 1 ANNONCES ET PAIEMENT DES betS

        //  Affichage
        output.println("Choix des mises\n");
        output.println("Pour arrêter de jouer, choisir la mise 0, cet arrêt sera définitif.\nSinon, choisir une bet strictement positive.\n");

        // Chaque joueur annonce et paie sa bet
        double[] bet = new double[active.length];
        collectBets(active, money, bet); // call de la fonction

        // vérification si tout le monde joue encore
        if(!playersRemain(active)) { // ferme le round
            return false;
        }
        // -----------------------------------------------------------------------------------------------------------//

        // 2 MÉLANGE ET DISTRIBUTION DES CARTES

        shuffleCards(deck); // on (re)mélange de jeu

        // création des main de chaque player (vide) + croupier
        int[][] cardPlayer = new int[nbPlayer][23]; // 23 --> consignes
        int[] cardCroupier = new int[23];

        // distribution des 2 premières cartes
        dealInitialCards(active, cardPlayer, cardCroupier, deck);

        displayGameInit(active, money, bet, cardPlayer, cardCroupier[1]);

        // -----------------------------------------------------------------------------------------------------------//

        // 3 TIRAGE DES CARTES + JEUX (element play turn)
        int[] allScores = new int[active.length+1];
        playTurn(allScores, active, money, bet, cardPlayer, cardCroupier, deck);

        // -----------------------------------------------------------------------------------------------------------//


        // 4 PAIEMENT DES GAINS

        // on met à jour le solde actuel en fonction des gagnants / perdant | distribution des gains


        output.println("\n--> Résultats de la partie <--\n");
        output.println(String.format("Le croupier a %d points\n", bestScore(cardCroupier)));


        for(int i = 0; i < nbPlayer; i++) {

            int pScore = allScores[i];
            int dealerScore =allScores[allScores.length-1];
            double pBet = bet[i];
            double pMoney = money[i];


            output.println(String.format("\nRésultat du joueur n°%d", i+1));
            output.println(String.format("solde = %.2f € / bet = %.2f € / cartes : %s ",pMoney, pBet, getMain(cardPlayer[i])));
            output.println(String.format("Tu as %d points",bestScore(cardPlayer[i])));

            money[i] = playerNewMoney(pMoney, pBet, pScore, dealerScore);
            output.println(String.format("Ton solde est de %.2f", money[i]));
        }


        // retrait des player qui n'ont plus de solde
        for(int i = 0; i < money.length; i++) {
            if (money[i] == 0) {
                active[i] = false;
            }
        }

        return playersRemain(active);

    }
}

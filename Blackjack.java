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


    /**
     * Fonction permettant de jouer un tour du jeu. Elle se compose en 4 étapes : la collect des mise avec {@link #collectBets(boolean[], double[], double[])}
     * Le mélange du deck {@link #shuffleCards(int[])}, la distribution des deux premières carte aux joueurs et au croupier avec {@link #dealInitialCards(boolean[], int[][], int[], int[])}
     * La gestion des tours de chaque joueurs et du croupier avec {@link #playTurn(boolean[], double[], double[], int[][], int[], int[], int[])}
     * Enfin l'affichage des résultats et la mise à jour du solde de chaque joueur avec {@link #playerNewMoney(double, double, int, int)}
     * @param active si les joueurs jouent ou non
     * @param money tableau contenant le solde actuel de chaque player
     * @param deck tableau contenant le sabot du jeu avec à l'indice 0 le nombre de carte
     * @return une valeur boolean indiquant si l'on doit lancer une nouvelle partie ou non
     *
     * */
    public static boolean playRound(boolean[] active, double[] money, int[] deck) {
        // 1 ANNONCES ET PAIEMENT DES MISES

        output.println("Choix des mises\n");
        output.println("Pour arrêter de jouer, choisir la mise 0, cet arrêt sera définitif.\nSinon, choisir une mise strictement positive.\n");

        // Chaque joueur annonce et paie sa mise
        double[] bet = new double[active.length];
        collectBets(active, money, bet); // call de la fonction

        // vérification si tout le monde joue encore
        if(!playersRemain(active)) {
            return false; // ferme le jeux
        }



        // 2 MÉLANGE ET DISTRIBUTION DES CARTES

        shuffleCards(deck); // on (re)mélange de jeu

        // création des main de chaque player (vide) + croupier
        int[][] cardPlayer = new int[active.length][23]; // 23 --> consignes
        int[] cardCroupier = new int[23];

        // distribution des 2 premières cartes
        dealInitialCards(active, cardPlayer, cardCroupier, deck);

        // affichage des mains joueurs et croupier
        displayGameInit(active, money, bet, cardPlayer, cardCroupier[1]);



        // 3 TIRAGE DES CARTES + JEUX
        int[] playerScore = new int[active.length]; // tableau des valeurs
        int pointDealer = playTurn(active, money, bet, cardPlayer, cardCroupier, playerScore, deck);




        // 4 PAIEMENT DES GAINS

        output.println("\n--> Résultats de la partie <--\n");
        output.println(String.format("Le croupier a %d points.\n", bestScore(cardCroupier)));

        for(int i = 0; i < active.length; i++) {
            // variables utilitaires
            int pScore = playerScore[i];
            int dealerScore = pointDealer;
            double pBet = bet[i];
            double pMoney = money[i];

            // affichage des informations après le tour
            output.println(String.format("\nRésultat du joueur %d", i+1));
            output.println(String.format("solde = %.1f € / bet = %.1f € / cartes : %s ",pMoney, pBet, getMain(cardPlayer[i])));
            if(bestScore(cardPlayer[i]) > 21) {
                output.println("Tu as dépassé 21 points.");
            } else {
                output.println(String.format("Tu as %d points.",bestScore(cardPlayer[i])));
            }

            // mie à jour du solde des joueurs
            money[i] = playerNewMoney(pMoney, pBet, pScore, dealerScore);
            output.println(String.format("Ton solde est de %.1f €", money[i]));
        }


        // retrait des player qui n'ont plus de solde
        for(int i = 0; i < money.length; i++) {
            if (money[i] == 0) {
                active[i] = false;
            }
        }
        // s'il reste du monde en jeu
        return playersRemain(active);
    }



    //----------------------------------------------------------------------------------------------------------------//
    //----------------------------------------------FONCTIONS SECONDAIRES---------------------------------------------//
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Fonction permettant de récupérer une carte du sabot et le ma mettre dans la main de carte données.
     * Met également à jour les informations du nombre de carte
     * @param deck sabot de carte du jeu
     * @param hand main de carte
     * @return valeur numérique de la carte
     * @see #getNextCard(int[])
     * */
    public static int drawCard(int[] deck, int[] hand) {
        int card = getNextCard(deck); // récupère la carte dans le sabot
        hand[0]++; // ajout d'une carte dans le compteur
        hand[hand[0]] = card; // on ajoute la carte
        return card;
    }

    /**
     * Fonction permettant de distribuer les 2 permières à chaque joueur et au croupier les uns à la suite des autres
     * @param playerIsActive indication des joueurs qui jouent ou non
     * @param playerHand toutes les mains des joueurs
     * @param dealerHand main du croupier
     * @param deck sabot de carte du jeu
     * @see #drawCard(int[], int[])
     * */
    public static void dealInitialCards(boolean[] playerIsActive, int[][] playerHand, int[] dealerHand, int[] deck) {
        int nbPlayer = playerIsActive.length;
        for(int i = 1; i <= 2; i++) { // nombre de carte
            for(int j = 0; j < nbPlayer; j++) {// affectation de la carte aux players
                drawCard(deck, playerHand[j]);
            }
            drawCard(deck, dealerHand); // affectation de la carte au croupier
        }
    }

    /**
     * Fonction permettant d'afficher les informations des joueurs et du croupier. (solde, mise et cartes)
     * @param playerIsActive indication des joueurs qui jouent ou non
     * @param playerMoney tous les soldes des joueurs
     * @param playerBet toutes les mises des joueurs
     * @param playerHand toutes les mains des joueurs
     * @param dealerVisibleCard la première carte du croupier
     * @see #cardName
     * @see #getMain(int[])
     * */
    public static void displayGameInit(boolean[] playerIsActive, double[] playerMoney, double[] playerBet, int[][] playerHand, int dealerVisibleCard) {
        for(int i = 0; i < playerIsActive.length; i++) {
            if(playerIsActive[i]) {
                output.println(String.format("\nJoueur %d : solde = %.1f € / mise = %.1f € / cartes : %s ",i+1, playerMoney[i], playerBet[i], getMain(playerHand[i])));
            }
        }
        output.println(String.format("\nLe croupier a les cartes %s et ? .", cardName(dealerVisibleCard)));
    }

    /**
     * Fonction permettant de gérer la phase de jeu des joueurs et du croupier pour tirer les cartes.
     * @param hand main de carte
     * @param minScore minimum score possible avec la main
     * @param hasAnAce présence d'au moins un As dans la main (valeur boolean)
     * @param bestScore meilleur score favorable avec la main
     * @param isPlayer si c'est un joueur ou non
     * @param deck sabot de carte du jeu
     * @return valeur égale à -1, 0, 1 à 21 en fonction des valeurs
     * @see #bestAffichagePlayerHand(int, int, int)
     * @see #drawCard(int[], int[])
     * @see #minScore(int[])
     * @see #bestScore(int[])
     * */
    public static int playDrawingPhase(int[] hand, int minScore, boolean hasAnAce, int bestScore, boolean isPlayer, int[] deck){
        if(isPlayer) { // c'est un player
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
            } while(reponse.equalsIgnoreCase("oui") && bestScore < 21); // conditions de boucle
            if(bestScore > 21) {
                output.println("Tu as dépassé 21 points !");
            }
            return (bestScore < 22) ? bestScore:-1; // ternaire pour retourner la valeur
        }
        else { // c'est le croupier
            output.println(String.format("Il a %d points.", bestScore));

            while (bestScore < 17) { // condition de boucle
                int card = drawCard(deck, hand);
                bestScore = bestScore(hand);
                if (card == 12) {
                    output.println(String.format("Le croupier a tiré une %s. Il a %d points.", cardName(card), bestScore));
                } else {
                    output.println(String.format("Le croupier a tiré un %s. Il a %d points.", cardName(card), bestScore));
                }
            }
            if(bestScore > 21) {
                output.println("Le croupier a dépassé 21 points !");
            }
            return (bestScore < 22) ? bestScore:0; // ternaire pour retourner la valeur
        }
    }

    /**
     * Fonction permettant de manager les tours des joueurs et celui du croupier
     * @param playerIsActive information sur l'activité des joueurs
     * @param money solde actuel des joueurs
     * @param bet mise actuelle des joueurs
     * @param cardPlayer toutes les mains des joueurs
     * @param dealerHand main du croupier
     * @param playerScore tableau avec les scores des joueurs à la fin de leurs tours
     * @param deck sabot de carte du jeu
     * @return valeur résultat du dealer pour ses cartes
     * @see #playerPlayTurn(int, double, double, int[], int[])
     * @see #dealerPlayTurn(int[], int[])
     *
     * */
    public static int playTurn(boolean[] playerIsActive, double[] money, double[] bet, int [][] cardPlayer, int[] dealerHand, int[] playerScore, int[] deck) {
        output.println("\nFaites vos jeux !\n"); // affichage
        //players
        for(int i = 0; i < playerIsActive.length; i++) {
            if(playerIsActive[i]) { // le joueur joue
                playerScore[i] = playerPlayTurn(i, money[i], bet[i], cardPlayer[i], deck); // récupération valeur
            }
        }
        // dealer
        return dealerPlayTurn(dealerHand, deck);
    }

    /**
     * Fonction permettant de manager le tour de chaque joueur avec son affichage de tirage de carte
     * @param i numéro du joueur -1
     * @param pMoney solde actuel du joueur
     * @param pBet mise actuelle du joueur
     * @param pHand main du joueur
     * @param deck sabot de carte du jeu
     * @return valeur des points du joueur -1, 1 à 21 ou 22
     * @see #displayPlayerGameState(double, double, int[])
     * @see #minScore(int[])
     * @see #bestScore(int[])
     * @see #hasAnAce(int[])
     * @see #displayBlackJack(boolean)
     * @see #playDrawingPhase(int[], int, boolean, int, boolean, int[])
     * */
    public static int playerPlayTurn(int i, double pMoney, double pBet, int[] pHand, int[] deck) {
        output.println(String.format("\n--> Tour du joueur %d", i+1));
        displayPlayerGameState(pMoney, pBet, pHand); // affichage des stats

        // calcul des valeurs
        int minScore = minScore(pHand);
        int bestScore = bestScore(pHand);
        boolean hasAnAce = hasAnAce(pHand);

        // cas de blackJack
        if (bestScore == 21) {
            displayBlackJack(true);
            return 22;
        }
        else { // cas général
            return playDrawingPhase(pHand, minScore, hasAnAce, bestScore, true, deck);
        }
    }

    /**
     * Fonction permettant de manager le tour du croupier avec son affichage de tirage de carte
     * @param dealerHand main du croupier
     * @param deck sabot de carte du jeu
     * @return valeur des points du croupier 0, 1 à 21 ou 22
     * @see #getMain(int[])
     * @see #displayBlackJack(boolean)
     * @see #minScore(int[])
     * @see #hasAnAce(int[])
     * @see #bestScore(int[])
     * */
    public static int dealerPlayTurn(int[] dealerHand, int[] deck) {
        //tour croupier
        output.println("\n--> Tour du croupier");
        output.println(String.format("Le croupier a les cartes %s.", getMain(dealerHand))); // informations

        // cas de blackjack
        if (bestScore(dealerHand) == 21) {
            displayBlackJack(false);
            return 22;
        }
        else {// cas générale
            return playDrawingPhase(dealerHand, minScore(dealerHand), hasAnAce(dealerHand),bestScore(dealerHand), false, deck);
        }
    }

    /**
     * Fonction permettant de mettre à jour l'argent des du joueurs en fonction de ses points et de sa mise
     * @param pMoney solde actuel du joueur
     * @param pBet mise actuelle du joueur
     * @param pScore valeur obtenu par le joueur
     * @param dealerScore valeur obtenue par le croupier
     * @return nouveau solde du joueur après redistribution des gains
     * */
    public static double playerNewMoney(double pMoney, double pBet, int pScore, int dealerScore) {
        if (pScore > dealerScore) {
            if (pScore==22) {
                // le player a fait blackjack et le croupier non, il récupère 3 fois sa mise
                output.println(String.format("Tu gagnes contre le croupier avec un Black Jack, tu récupères 3.0 fois ta mise, soit %.1f €", pBet*3));
                return pMoney + pBet * 3;
            }
            else {
                // le player gagne contre le croupier, il récupère 2.5 fois sa mise
                output.println(String.format("Tu gagnes, tu récupères 2.5 fois ta mise, soit %.1f €", pBet*2.5));
                return pMoney + pBet * 2.5;
            }
        }
        else if (pScore == dealerScore) {
            // le player et le dealer ont égalité, le player récupère sa mise
            output.println(String.format("Tu es à égalité avec le croupier, tu récupères ta mise, soit %.1f €", pBet));
            return pMoney + pBet;
        }
        else {
            // le player a perdu contre le dealer, il ne récupère rien
            output.println("Tu perds contre le croupier, tu ne récupères rien.");
            return pMoney;
        }
    }

    //----------------------------------------------------------------------------------------------------------------//
    //----------------------------------------------FONCTIONS UTILITAIRES---------------------------------------------//
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
                output.print(String.format("Donner la somme en Euros que possède le joueur %d (entre 1.0 et 1000000.0) : ", i+1));

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
                    output.print(String.format("Joueur %d, donne ta mise en Euros (entre 0.0 et %.1f) : ", i+1, money[i]));

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

    /**
     * Fonction permettant de mélanger le sabot de carte d jeu et de réinitialiser le nombre de carte dedans à l'indice 0
     * @param deck sabot de carte du jeu
     * */
    public static void shuffleCards(int[] deck) {
        int l = deck.length;
        for(int i = 0; i< l*2; i++) { // boucle pour tourner x fois
            int indice1 = random.nextInt(1, l-1);
            int indice2 = random.nextInt(1, l-1);

            // échange des deux indices
            int tempo = deck[indice1];
            deck[indice1] = deck[indice2];
            deck[indice2] = tempo;
        }
        deck[0] = deck.length-1; // reset de l'indice pour savoir qu'elle est la prochaine carte à piocher
    }

    /**
     * Fonction permettant de créer le sabot de jeu
     * @param nbPacks nombre de paquet de 52 cartes pour le jeu
     * @return un tableau contenant toutes les cartes du jeux et à l'indice 0 le nombre de carte restante dans le sabot
     * */
    public static int[] generateCards(int nbPacks) {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}; // Jack 11 / Queen 12 / King 13

        int[] deck = new int[52*nbPacks +1]; // jeu de 52 cartes * n + 1 pour l'indice 0 (nb cartes)

        for(int i = 0; i < nbPacks; i++) { // x nbPack
            for(int j = 0; j< 4; j++) { // x famille de carte par Packs
                for(int k = 0; k < 13; k++) { // nombre de carte par famille
                    deck[i*52 + j*13 + k +1] = values[k]; // ajout de la carte à la suite
                }
            }
        }
        deck[0] = deck.length-1; // début pour tirer une carte |- a garder car test dessus  | redéfinie dans le schuffle
        return deck;
    }

    /**
     * Fonction permettant de déterminer si au moins un joueur est toujours en train de jouer ou non
     * @param active tableau contant le statut des joueur : true -> actif | false -> inactif
     * @return valeur indicant leur statut
     * */
    public static boolean playersRemain(boolean[] active) {
        for(boolean player:active) {
            if(player){ // test de la valeur
                return true;
            }
        }
        return false;
    }

    /**
     * Fonction permettant de déterminer la valeur en chaine de caractère à partir de la valeur numérique de la carte donnée
     * @param numero valeur numérique de la carte : de 1 à 13
     * @return chaine de caractère de la carte pour l'affichage
     * */
    public static String cardName(int numero) {
        return switch (numero) {
            case 11 -> "valet";
            case 12 -> "dame";
            case 13 -> "roi";
            case 1 -> "as";
            default -> String.format("%d", numero); // element par défaut (valeur str de l'int)
        };
    }

    /**
     * Fonction permettant de retourner la main d'un joueur en chaine de caractère pour l'affichage.
     * @param mainPersonnage main de carte du joueur
     * @return chaine de caractère de la main
     * @see #cardsNumber(int[])
     * */
    public static String getMain(int[] mainPersonnage) {
        int nbCards = cardsNumber(mainPersonnage);

        // on ajoute toutes les carte dans tableau
        String[] main = new String[nbCards];
        for(int i = 1; i <= nbCards; i++) {
            main[i-1] = cardName(mainPersonnage[i]);
        }

        // on confectionne la chaine de caractère
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

    /**
     * Fonction permetant de récupérer la prochaine carte du deck
     * @param deck sabot de carte
     * @return valeur décimal de la carte
     * */
    public static int getNextCard(int[] deck) {
        int card = deck[deck[0]];
        deck[0]--; // mise à jour du nombre de carte
        return card;
    }

    /**
     * Fonction permettant de retourner le nombre de cartes d'un tableau de carte
     * (notez que T[0] est le nombre effectif de cartes et n'est donc pas une carte)
     * @param tab tableau contenant des carte et le nombre de carte à l'indice 0
     * @return valleur correspondant au nombre de carte
     * */
    public static int cardsNumber(int[] tab) {
        return tab[0];
    }
    
    /**
     * Fonction permettant de calculer le score minnimum obtensible pour une main donnée
     * @param hand main de carte
     * @return minimum score obtensible
     * @see #cardsNumber(int[]) 
     * */
    public static int minScore(int[] hand) {
        int total = 0;
        for(int i = 1; i <= cardsNumber(hand); i++) {// on ajoute toutes les valeurs au minimum
            total += (hand[i] > 10) ? 10:hand[i];
        }
        return total;
    }
    
    /**
     * Fonction permettant de calculer le meilleur score favorable pour une main donnée
     * @param hand main de carte
     * @return meilleur score obtensible
     * @see #cardsNumber(int[]) 
     * */
    public static int bestScore(int[] hand) {
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
    
    /**
     * Fonction permettant de déterminer si la main contient au moins un As
     * @param hand main de carte
     * @return valeur boolean indiquant la présance ou non d'au moins un As
     * */
    public static boolean hasAnAce(int[] hand) {
        for(int card:hand) { // parcours du tableau
            if(card==1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fonction permettant d'afficher la meilleur force des points et carte lors d'un tirage.
     * @param card valeur correspondant à la carte tirée
     * @param minScore score minimum possible (tout les as valent 1)
     * @param bestScore meilleur score obtensible
     * @see #getMain(int[])
     * */
    public static void bestAffichagePlayerHand(int card, int minScore, int bestScore) {
        if(card != 0) { // on a pioché une carte
            if (card == 12) { // si c'est une dame on affiche 'une' à la place de 'un'
                output.print(String.format("Tu as tiré une %s. ", cardName(card)));
            } else {
                output.print(String.format("Tu as tiré un %s. ", cardName(card)));
            }
        }

        // on affiche les points que l'on a
        if (minScore != bestScore) { // 2 score de points possible (as valent 1 ou 11)
            output.println(String.format("Tu as %d ou %d points.", minScore, bestScore));
        } else {
            output.println(String.format("Tu as %d points.", bestScore));
        }
    }

    /**
     * Fonction permettant d'afficher les statistiques du joueurs (numéro du joueur, son solde actuel,
     * sa mise et les cartes qu'il a dans sa main.
     * @param pMoney montant du solde actuel du joueur
     * @param pBet montant actuel de la mise du joueur
     * @param phand main de carte actuel du joueur
     * @see #getMain(int[])
     * */
    public static void displayPlayerGameState(double pMoney, double pBet,int[] phand) {
        output.println(String.format("solde = %.1f € / mise = %.1f € / cartes : %s ",pMoney, pBet, getMain(phand)));
    }

    /**
     * Fonction permettant d'afficher 'BlackJack'
     * @param isPlayer valeur boolean pour connaitre l'identité de la personne (joueur ou croupier)
     * */
    public static void displayBlackJack(boolean isPlayer) {
        if (isPlayer) {
            output.println("Tu as un Black Jack.\n");
        } else {
            output.println("Le croupier a un Black Jack.\n");
        }
    }
}
import java.util.Random;
import java.util.Scanner;
import java.util.Locale;
import java.io.PrintStream;


// Correction orthographique des docstring avec claude.ai

public class Blackjack {

    // variables utilitaires de la class
    public static Scanner input = new Scanner(System.in).useLocale(Locale.US);
    public static PrintStream output = System.out;
    public static Random random = new Random();

    //----------------------------------------------------------------------------------------------------------------//
    //----------------------------------------------FONCTIONS PRINCIPALES---------------------------------------------//
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Point d'entrée permettant de récupérer le nombre de joueurs et le nombre de paquets de 52 cartes,
     * puis de lancer {@link #playGame(int, int)}.
     * À la fin du jeu, récupère un couple de listes contenant le solde initial et le solde final de chaque joueur.
     * Ce couple est ensuite transmis à la fonction {@link #afficherResultats} pour afficher les résultats.
     */
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
     * Permet de jouer plusieurs parties tant que les conditions le permettent.
     *
     * @param nbPlayer nombre de joueurs
     * @param nbPacks nombre de paquets de 52 cartes
     * @return Une matrice de double correspondant au solde initial et au solde final de chaque joueur
     * @see #playRound(boolean[], double[], int[])
     */
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
     * Permet de jouer un tour de jeu en 4 étapes :
     * 1. Collecte des mises avec {@link #collectBets(boolean[], double[], double[])} & mélange du deck avec {@link #shuffleCards(int[])}
     * 2. Distribution des deux premières cartes aux joueurs et au croupier avec {@link #dealInitialCards(boolean[], int[][], int[], int[])}
     * 3. Gestion des tours de chaque joueur et du croupier avec {@link #playTurn(boolean[], double[], double[], int[][], int[], int[], int[])}
     * 4. Affichage des résultats et mise à jour du solde de chaque joueur avec {@link #playerNewMoney(double, double, int, int)}
     *
     * @param active indique si chaque joueur est actif ou non
     * @param money tableau contenant le solde actuel de chaque joueur
     * @param deck tableau représentant le sabot du jeu (l'indice 0 contient le nombre de cartes)
     * @return true si une nouvelle partie doit être lancée, false sinon
     */
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
     * Permet de tirer une carte du sabot et de l'ajouter à la main donnée.
     * Met également à jour le nombre de cartes restantes dans le sabot.
     *
     * @param deck sabot de cartes du jeu
     * @param hand main de cartes
     * @return valeur numérique de la carte tirée
     * @see #getNextCard(int[])
     */
    public static int drawCard(int[] deck, int[] hand) {
        int card = getNextCard(deck); // récupère la carte dans le sabot
        hand[0]++; // ajout d'une carte dans le compteur
        hand[hand[0]] = card; // on ajoute la carte
        return card;
    }

    /**
     * Permet de distribuer les 2 premières cartes à chaque joueur et au croupier, les uns après les autres.
     *
     * @param playerIsActive indique quels joueurs sont actifs
     * @param playerHand mains de tous les joueurs
     * @param dealerHand main du croupier
     * @param deck sabot de cartes du jeu
     * @see #drawCard(int[], int[])
     */
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
     * Permet d'afficher les informations des joueurs et du croupier (solde, mise et cartes).
     *
     * @param playerIsActive indique quels joueurs sont actifs
     * @param playerMoney soldes de tous les joueurs
     * @param playerBet mises de tous les joueurs
     * @param playerHand mains de tous les joueurs
     * @param dealerVisibleCard première carte visible du croupier
     * @see #cardName
     * @see #getMain(int[])
     */
    public static void displayGameInit(boolean[] playerIsActive, double[] playerMoney, double[] playerBet, int[][] playerHand, int dealerVisibleCard) {
        for(int i = 0; i < playerIsActive.length; i++) {
            if(playerIsActive[i]) {
                output.println(String.format("\nJoueur %d : solde = %.1f € / mise = %.1f € / cartes : %s ",i+1, playerMoney[i], playerBet[i], getMain(playerHand[i])));
            }
        }
        output.println(String.format("\nLe croupier a les cartes %s et ? .", cardName(dealerVisibleCard)));
    }

    /**
     * Gère la phase de jeu d'un joueur ou du croupier pour le tirage des cartes.
     *
     * @param hand main de cartes
     * @param minScore score minimum possible avec la main
     * @param hasAnAce indique la présence d'au moins un As dans la main
     * @param bestScore meilleur score possible avec la main
     * @param isPlayer true si c'est un joueur, false si c'est le croupier
     * @param deck sabot de cartes du jeu
     * @return -1 si le joueur perd (dépassement), 0 si le croupier perd, 1 à 21 selon le score obtenu
     * @see #bestAffichagePlayerHand(int, int, int)
     * @see #drawCard(int[], int[])
     * @see #minScore(int[])
     * @see #bestScore(int[])
     */
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
     * Gère les tours de jeu de tous les joueurs puis celui du croupier.
     *
     * @param playerIsActive indique quels joueurs sont actifs
     * @param money soldes actuels des joueurs
     * @param bet mises actuelles des joueurs
     * @param cardPlayer mains de tous les joueurs
     * @param dealerHand main du croupier
     * @param playerScore scores finaux de chaque joueur à la fin de leur tour
     * @param deck sabot de cartes du jeu
     * @return score final du croupier
     * @see #playerPlayTurn(int, double, double, int[], int[])
     * @see #dealerPlayTurn(int[], int[])
     */
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
     * Gère le tour d'un joueur avec l'affichage du tirage de cartes.
     *
     * @param i numéro du joueur (indice base 0)
     * @param pMoney solde actuel du joueur
     * @param pBet mise actuelle du joueur
     * @param pHand main du joueur
     * @param deck sabot de cartes du jeu
     * @return score du joueur : -1 si défaite, 1 à 21 selon le score, 22 si dépassement
     * @see #displayPlayerGameState(double, double, int[])
     * @see #minScore(int[])
     * @see #bestScore(int[])
     * @see #hasAnAce(int[])
     * @see #displayBlackJack(boolean)
     * @see #playDrawingPhase(int[], int, boolean, int, boolean, int[])
     */
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
     * Gère le tour du croupier avec l'affichage du tirage de cartes.
     *
     * @param dealerHand main du croupier
     * @param deck sabot de cartes du jeu
     * @return score du croupier : 0 si arrêt, 1 à 21 selon le score, 22 si dépassement
     * @see #getMain(int[])
     * @see #displayBlackJack(boolean)
     * @see #minScore(int[])
     * @see #hasAnAce(int[])
     * @see #bestScore(int[])
     */
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
     * Met à jour le solde du joueur en fonction de son score et de sa mise.
     *
     * @param pMoney solde actuel du joueur
     * @param pBet mise actuelle du joueur
     * @param pScore score obtenu par le joueur
     * @param dealerScore score obtenu par le croupier
     * @return nouveau solde du joueur après redistribution des gains
     */
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
     * Demande à l'utilisateur de saisir un entier compris entre deux bornes données.
     * Redemande la saisie tant que la valeur est en dehors de l'intervalle.
     *
     * @param value1 borne minimale (incluse)
     * @param value2 borne maximale (incluse)
     * @param textInput texte affiché lors de la demande de saisie
     * @return valeur saisie par l'utilisateur, comprise entre les bornes
     */
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
     * Récupère les soldes initiaux de tous les joueurs de la partie.
     *
     * @param nbPlayer nombre de joueurs
     * @return tableau contenant les soldes initiaux de tous les joueurs
     */
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
     * Collecte les mises de chaque joueur actif.
     * Si la mise n'est pas dans l'intervalle attendu, elle est redemandée.
     * Si la mise est égale à 0, le joueur n'est plus considéré comme actif et sera exclu du jeu.
     *
     * @param active indique quels joueurs sont actifs
     * @param money soldes actuels de tous les joueurs
     * @param bet mises de chaque joueur actif
     */
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
     * Mélange le sabot de cartes et réinitialise le compteur de cartes à l'indice 0.
     *
     * @param deck sabot de cartes du jeu
     */
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
     * Crée le sabot de jeu avec le nombre de paquets spécifié.
     *
     * @param nbPacks nombre de paquets de 52 cartes
     * @return tableau contenant toutes les cartes du jeu (l'indice 0 contient le nombre de cartes restantes)
     */
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
     * Détermine si au moins un joueur est encore actif dans la partie.
     *
     * @param active statut de chaque joueur (true = actif, false = inactif)
     * @return true si au moins un joueur est actif, false sinon
     */
    public static boolean playersRemain(boolean[] active) {
        for(boolean player:active) {
            if(player){ // test de la valeur
                return true;
            }
        }
        return false;
    }

    /**
     * Détermine le nom de la carte à partir de sa valeur numérique.
     *
     * @param numero valeur numérique de la carte (1 à 13)
     * @return nom de la carte sous forme de chaîne de caractères
     */
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
     * Retourne la main d'un joueur sous forme de chaîne de caractères pour l'affichage.
     *
     * @param mainPersonnage main de cartes du joueur
     * @return représentation textuelle de la main
     * @see #cardsNumber(int[])
     */
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
     * Récupère la prochaine carte du sabot.
     *
     * @param deck sabot de cartes
     * @return valeur numérique de la carte
     */
    public static int getNextCard(int[] deck) {
        int card = deck[deck[0]];
        deck[0]--; // mise à jour du nombre de carte
        return card;
    }

    /**
     * Retourne le nombre de cartes dans un tableau de cartes.
     * Note : l'indice 0 contient le nombre de cartes et n'est pas une carte.
     *
     * @param tab tableau contenant les cartes (l'indice 0 contient le compteur)
     * @return nombre de cartes dans le tableau
     */
    public static int cardsNumber(int[] tab) {
        return tab[0];
    }

    /**
     * Calcule le score minimum possible pour une main donnée.
     *
     * @param hand main de cartes
     * @return score minimum de la main
     * @see #cardsNumber(int[])
     */
    public static int minScore(int[] hand) {
        int total = 0;
        for(int i = 1; i <= cardsNumber(hand); i++) {// on ajoute toutes les valeurs au minimum
            total += (hand[i] > 10) ? 10:hand[i];
        }
        return total;
    }

    /**
     * Calcule le meilleur score possible pour une main donnée.
     *
     * @param hand main de cartes
     * @return meilleur score de la main
     * @see #cardsNumber(int[])
     */
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
     * Détermine si la main contient au moins un As.
     *
     * @param hand main de cartes
     * @return true si la main contient au moins un As, false sinon
     */
    public static boolean hasAnAce(int[] hand) {
        for(int card:hand) { // parcours du tableau
            if(card==1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Affiche le meilleur score et la carte tirée lors d'un tirage.
     *
     * @param card valeur de la carte tirée
     * @param minScore score minimum possible (tous les As valent 1)
     * @param bestScore meilleur score possible
     * @see #getMain(int[])
     */
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
     * Affiche les statistiques du joueur : numéro, solde actuel, mise et cartes en main.
     *
     * @param pMoney solde actuel du joueur
     * @param pBet mise actuelle du joueur
     * @param phand main de cartes du joueur
     * @see #getMain(int[])
     */
    public static void displayPlayerGameState(double pMoney, double pBet,int[] phand) {
        output.println(String.format("solde = %.1f € / mise = %.1f € / cartes : %s ",pMoney, pBet, getMain(phand)));
    }

    /**
     * Affiche "BlackJack" avec l'identité de la personne concernée.
     *
     * @param isPlayer true si c'est un joueur, false si c'est le croupier
     */
    public static void displayBlackJack(boolean isPlayer) {
        if (isPlayer) {
            output.println("Tu as un Black Jack.\n");
        } else {
            output.println("Le croupier a un Black Jack.\n");
        }
    }
}
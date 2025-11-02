import java.util.Random;
import java.util.Scanner;
import java.util.Locale;
import java.io.PrintStream;


public class Blackjack {


    public static Scanner input = new Scanner(System.in).useLocale(Locale.US);
    public static PrintStream output = System.out;
    public static Random random = new Random();


    public static void main(String[] args) {

        // input nb players
        int nbPlayer = getNbPlayer();

        // intput nb packs
        int nbPacks = getnbPacks();

        // lancement d'une séance de jeux
        double [][] infos = playGame(nbPlayer, nbPacks);

        // affichage infos
        for(double[] ligne:infos){
            for(double elt:ligne)  {
                output.print(elt + " ");
            }
            output.println();
        }

    }


    // ---------- Méthode get infos ---------- //
    public static int getNbPlayer() {
        // get input nb players
        int nbPlayer;
        do {
            output.print("Donner le nombre de joueurs (entre 1 et 6) : ");
            nbPlayer = input.nextInt(); // on demande la valeur
            if (nbPlayer < 1  || nbPlayer > 6) {
                output.println("Réponse incorrecte !");
            }
        } while (nbPlayer < 1 || nbPlayer > 6); // on boucle tant que c'est pas bon

        return nbPlayer;
    }

    public static int getnbPacks() {
        // intput nb packs
        int nbPacks;
        do {
            output.print("Donner le nombre de paquets de 52 cartes utilisés (entre 1 et 8) : ");
            nbPacks = input.nextInt(); // on demande la valeur
            if (nbPacks < 1 || nbPacks > 8) {
                output.println("Réponse incorrecte !");
            }
        } while (nbPacks < 1 || nbPacks > 8); // on boucle tant que c'est pas bon

        return nbPacks;
    }

    public static double[] getSoldePlayer(int nbPlayer) {
        // get solde players
        double[] tabsolde = new double[nbPlayer];
        for(int i = 0; i < nbPlayer; i++) {
            double solde;
            do {
                output.print(String.format("Donner la solde en Euros que possède le joueur %d (entre 1.0 et 1000000.0) : ", i+1));
                solde = input.nextDouble();
                if(solde < 1.0 || solde > 1000000.0) {
                    output.println("Réponse incorrecte !");
                }
            } while (solde < 1.0 || solde > 1000000.0); // on boucle tant que c'est pas bon
            tabsolde[i] = solde;
        }

        return  tabsolde;
    }

    public static double getbetPlayer(int i, double soldeDuPlayer) {
        // get bet player
        double bet;
        do {
            output.print(String.format("Joueur %d , donne ta mise en Euros (entre 0.0 et %.2f ) : ", i+1, soldeDuPlayer));
            bet = input.nextDouble();
            if(bet < 0 || bet > soldeDuPlayer) {
                output.println("Réponse incorrecte !");
            }
        } while ( bet < 0 || bet > soldeDuPlayer); // on boucle tant que c'est pas bon

        return bet;
    }


    // ---------- Méthode utilitaires ---------- //
    public static void schuffleTab(int[] tab) {
        // méthode pour mélanger le deck
        int l = tab.length;
        for(int i = 0; i< l*2; i++) {
            int indice1 = random.nextInt(0, l-1);
            int indice2 = random.nextInt(0, l-1);

            int tempo = tab[indice1];
            tab[indice1] = tab[indice2];
            tab[indice2] = tempo;
        }
    }

    public static int[] makeDeck(int nbPacks) {
        // méthode pour former de deck de cartes
        int types = 4;
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}; // Jack 11 / Queen 12 / King 13

        int[] deck = new int[52*nbPacks]; // jeu de 52 cartes

        for(int i = 0; i < nbPacks; i++) {
            for(int j = 0; j< types; j++) {
                for(int k = 0; k < values.length; k++) {
                    deck[i*52 + j*13 + k] = values[k];
                }
            }
        }
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
    
    public static String getStrValueCard(int numero) {
        // à partir de la valeur d'in game d'une carte, retourne sa chaine str
        return switch (numero) {
            case 11 -> "valet";
            case 12 -> "reine";
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

    public static String getMain(int[] mainPersonnage, int hideEnd) {
        // retourne la chain str dela main d'un personnage. Possibilité de caché des élément en partant de la fin
        int nbCards = getNbCard(mainPersonnage);
        String[] main = new String[nbCards];
        for(int i = 0; i < nbCards; i++) {
            if (nbCards - hideEnd > i) { // si les élément ne doivent pas être caché
                main[i] = getStrValueCard(mainPersonnage[i]);
            } else {
                main[i] = "?";
            }
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

    public static int getPoints(int[] main) {
        // permet de retourner la somme d'une main donnée
        int total = 0;
        int asCpt = 0;

        for(int elt:main) { // on ajoute toutes les valeurs au max
            if(elt == 1) {
                asCpt ++;
                total += 11;
            } else if(elt > 10 ) {
                total += 10;
            } else {
                total += elt;
            }
        }

        for(int i = 0; i < asCpt; i++) { // on retire pour que l'as compte 1 si c'est sup à 21
            if(total > 21) {
                total -= 10;
            }
        }

        return total;

    }

    public static int getNbCard(int[] main) {
        // permet de retourner le nombre de cartes dans une main donnée
        int cpt = 0;
        while(main[cpt] != 0) {
            cpt++;
        }
        return cpt;
    }

    public static  boolean isBlackJack(int[] main) {
        //permet de savoir s'il y a black jack ou non pour une main donnée
        return getIntValueCard(main[0]) + getIntValueCard(main[1]) == 21;
    }

    public static double[] collectBets(int nbPlayer, boolean[] active, double[] money) {
        double[] tabbetPlayers = new double[nbPlayer];
        for(int i = 0; i < nbPlayer; i++) {
            if(active[i] && money[i] > 0) { // si le joueur est encore en ligne / a encore de l'argent pour joueur
                double bet = getbetPlayer(i, money[i]);
                if (bet > 0) {
                    tabbetPlayers[i] = bet;
                    money[i] -= bet;
                } else {
                    active[i] = false; // bet nulle, le joueur arrete de jouer
                }
            }
        }
        return tabbetPlayers;
    }

    public static boolean playRound(boolean[] active, double[] money, int[] deck) {
        // 0 Utilitaires

        int nbPlayer = active.length;

        // -----------------------------------------------------------------------------------------------------------//

        // 1 ANNONCES ET PAIEMENT DES betS

        //  Affichage
        output.println("Choix des mises\n");
        output.println("Pour arrêter de jouer, choisir la mise 0, cet arrêt sera définitif.\nSinon, choisir une bet strictement positive.\n");

        // Chaque joueur annonce et paie sa bet
        double[] betPlayers = collectBets (nbPlayer, active, money);

        // vérification si tout le monde joue encore
        if(!playersRemain(active)) { // ferme le round
            return false;
        }

        // -----------------------------------------------------------------------------------------------------------//

        // 2 MÉLANGE ET DISTRIBUTION DES CARTES

        schuffleTab(deck); // on (re)mélange de jeu


        // création des main de chaque player (vide) + croupier
        int[][] cardPlayer = new int[nbPlayer][23]; // 23 --> consignes
        int[] cardCroupier = new int[23];

        // distribution des 2 premières cartes
        int indexDeck = 0;
        for(int i = 0; i < 2; i++) { // nombre de carte
            for(int j = 0; j < nbPlayer; j++) { // affectation de la carte aux players
                cardPlayer[j][i] = deck[indexDeck];
                indexDeck++;
            }
            cardCroupier[i] = deck[indexDeck]; // afectation de la carte au croupier
            indexDeck++;
        }

        // -----------------------------------------------------------------------------------------------------------//

        // 3 TIRAGE DES CARTES

        String mainCroupier = getMain(cardCroupier, 1); // infos croupier
        output.println(String.format("\nLe croupier a les cartes %s \n", mainCroupier) );

        output.println("\nFaites vos jeux !\n"); // globale affiche

        for(int i = 0; i < nbPlayer; i++) {
            int nbCards = 2;
            int nbPoint = getPoints(cardPlayer[i]);

            output.println(String.format("\n --> Tour du joueur %d", i+1));
            String mainPlayer = getMain(cardPlayer[i], 0);
            output.println(String.format("Joueur %d : solde = %.2f € / mise = %.2f € / cartes : %s ",i+1, money[i], betPlayers[i], mainPlayer));
            output.println(String.format("Tu as %d points.", nbPoint));

            // si pts sup à 21 on affiche un texte alténatif, sinon on demande de tirer une nouvelle carte
            if (getPoints(cardPlayer[i]) < 21) {
                String reponse;
                do {
                    output.print("Veux-tu tirer une carte [oui/non] ? ");
                    reponse = input.next();

                    while (!reponse.equalsIgnoreCase("oui") && !reponse.equalsIgnoreCase("non")) { // element robuste de upper et lower
                        output.println("Saisie incorrect !");
                        output.print("Veux-tu tirer une carte [oui/non] ? ");
                        reponse = input.next();
                        output.println(reponse);
                    }

                    if(reponse.equalsIgnoreCase("oui")) {
                        int card = deck[indexDeck];
                        indexDeck ++;
                        cardPlayer[i][nbCards] = card;
                        nbCards++;

                        nbPoint = getPoints(cardPlayer[i]);

                        output.println(String.format("Tu as tiré un %s. Tu as %d points", getStrValueCard(card), nbPoint));
                    }
                } while(reponse.equalsIgnoreCase("oui") && nbPoint < 21);
            } else {
                mainPlayer = getMain(cardPlayer[i], 0);
                output.println(String.format("Tu as un %s. Tu as %d points", mainPlayer, nbPoint));
            }
        }

        //tour croupier
        int nbPointCroupier = getPoints(cardCroupier);
        int nbCards = 2;

        output.println("\n--> Tour du croupier");
        mainCroupier = getMain(cardCroupier, 0);
        output.println(String.format("Le croupier a les cartes %s", mainCroupier));
        output.println(String.format("Il a %d points.", nbPointCroupier));

        while (nbPointCroupier < 17) {
            int card = deck[indexDeck];
            indexDeck++;
            cardCroupier[nbCards] = card;
            nbCards ++;
            nbPointCroupier = getPoints(cardCroupier);

            output.println(String.format("Le croupier a tiré un %s. Il a %d", getStrValueCard(card), nbPointCroupier));
        }


        // -----------------------------------------------------------------------------------------------------------//


        // 4 PAIEMENT DES GAINS

        // on met à jour le solde actuel en fonction des gagnants / perdant | distribution des gains
        int ptsCroupier = getPoints(cardCroupier);
        boolean isCroupierBlackJack = isBlackJack(cardCroupier);

        output.println("\n--> Résultats du tour ! <--\n");
        output.println(String.format("Le croupier a %d points\n", ptsCroupier));


        for(int i = 0; i < nbPlayer; i++) {
            int ptsPlayer = getPoints(cardPlayer[i]);
            boolean isPlayerBlackJack = isBlackJack(cardPlayer[i]);

            output.println(String.format("\nRésultat du joueur n°%d", i+1));
            String mainPlayer = getMain(cardPlayer[i], 0);
            output.println(String.format("solde = %.2f € / bet = %.2f € / cartes : %s ",money[i], betPlayers[i], mainPlayer));
            output.println(String.format("Tu as %d points",ptsPlayer));

            if (isCroupierBlackJack) { // black jack du croupier
                if (isPlayerBlackJack) { // player black jack
                    money[i] += betPlayers[i];
                    // le player récupère sa bet

                    //cas 1
                    output.println(String.format("Le croupier et toi avait fait BlackJack, tu récupères ta bet, soit %.2f Euros", betPlayers[i]));
                    money[i] += betPlayers[i];
                    output.println(String.format("Ton solde est de %.2f", money[i]));
                } else {

                    //cas 2
                    output.println("Tu perds contre le croupier, tu ne récupèrs rien.");
                    output.println(String.format("Ton solde est de %.2f", money[i]));
                }
            } else if (isPlayerBlackJack) {
                // cas 3
                output.println(String.format("Tu gagnes, tu récupères 3 fois ta bet, soit %.2f", betPlayers[i]*3));
                money[i] += betPlayers[i]*3;
                output.println(String.format("Ton solde est de %.2f", money[i]));

            } else if (ptsPlayer > 21) {
                //cas 2
                output.println("Tu perds contre le croupier, tu ne récupèrs rien.");
                output.println(String.format("Ton solde est de %.2f", money[i]));

            } else if (ptsPlayer <= 21 && !isPlayerBlackJack) {
                if (ptsCroupier > 21) {
                    //cas 4
                    output.println(String.format("Tu gagnes, tu récupères 2.5 fois ta bet, soit %.2f", betPlayers[i]*2.5));
                    money[i] += betPlayers[i]*2.5;
                    output.println(String.format("Ton solde est de %.2f", money[i]));

                } else {
                    //cas 5
                    if (ptsPlayer == ptsCroupier) { // cas 1
                        output.println(String.format("Le croupier et toi avait fait le meme nombre, tu récupères ta bet, soit %.2f Euros", betPlayers[i]));
                        money[i] += betPlayers[i];
                        output.println(String.format("Ton solde est de %.2f", money[i]));
                    } else if (ptsPlayer > ptsCroupier) { // cas 4
                        output.println(String.format("Tu gagnes, tu récupères 2.5 fois ta bet, soit %.2f", betPlayers[i]*2.5));
                        money[i] += betPlayers[i]*2.5;
                        output.println(String.format("Ton solde est de %.2f", money[i]));
                    } else { // cas 2
                        output.println("Tu perds contre le croupier, tu ne récupèrs rien.");
                        output.println(String.format("Ton solde est de %.2f", money[i]));
                    }
                }
            }
        }


        // retrait des player qui n'ont plus de solde
        for(int i = 0; i < money.length; i++) {
            if (money[i] == 0) {
                active[i] = false;
            }
        }

        return playersRemain(active);

    }

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
        int[] deck = makeDeck(nbPacks);

        // lancement du jeu + boucle tant qu'on joue
        boolean newGame;
        int cptGame = 0;
        do {
            cptGame++;

            // ------------ affichage ------------ //
            output.println();
            output.println(String.format("Partie n° %d", cptGame));
            output.println();

            newGame = playRound(active, money, deck);
        } while(newGame);


        output.println();
        output.println("Et le combat cessa faute de combattants.");

        double[][] infos = {tabsoldeStart, money};
        return infos;
    }
}
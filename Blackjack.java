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
        int nbPlayer = getPlayer();

        // intput nb packs
        int nbPack = getPack();

        // lancement d'une séance de jeux
        double [][] infos = playGame(nbPlayer, nbPack);

        // affichage infos
        for(double[] ligne:infos){
            for(double elt:ligne)  {
                output.print(elt + " ");
            }
            output.println();
        }

    }

    public static int getPlayer() {
        // input nb players
        int nbPlayer = 0;
        do {
            output.print("Donner le nombre de joueurs (entre 1 et 6) : ");
            nbPlayer = input.nextInt();
            if (nbPlayer < 1  || nbPlayer > 6) {
                output.println("Réponse incorrecte !");
            }
        } while (nbPlayer < 1 || nbPlayer > 6);

        return nbPlayer;
    }

    public static int getPack() {
        // intput nb packs
        int nbPack = 0;
        do {
            output.print("Donner le nombre de paquets de 52 cartes utilisés (entre 1 et 8) : ");
            nbPack = input.nextInt();
            if (nbPack < 1 || nbPack > 8) {
                output.println("Réponse incorrecte !");
            }
        } while (nbPack < 1 || nbPack > 8);

        return nbPack;
    }

    public static double[] soldePlayer(int nbPlayer) {
        // get solde players
        double[] tabsolde = new double[nbPlayer];
        for(int i = 0; i < nbPlayer; i++) {
            double solde = 0;
            do {
                output.print(String.format("Donner la solde en Euros que possède le joueur %d (entre 1.0 et 1000000.0) : ", i+1));
                solde = input.nextDouble();
                if(solde < 1.0 || solde > 1000000.0) {
                    output.println("Saisie incorrecte !");
                }
            } while (solde < 1.0 || solde > 1000000.0);
            tabsolde[i] = solde;
        }

        return  tabsolde;
    }

    public static double getMise(int i, double soldeDuPlayer) {
        // get mise player
        double mise = 0;
        do {
            output.print(String.format("Joueur %d , donne ta mise en Euros (entre 0.0 et %.2f ) : ", i+1, soldeDuPlayer)); // .format
            mise = input.nextDouble();
            if(mise < 0 || mise > soldeDuPlayer) {
                output.println("Saisie incorrecte !");
            }
        } while ( mise < 0 || mise > soldeDuPlayer);

        return mise;
    }

    public static void schuffleTab(int[] tab) {
        int l = tab.length;
        for(int i = 0; i< l*2; i++) {
            int indice1 = random.nextInt(0, l-1);
            int indice2 = random.nextInt(0, l-1);

            int tempo = tab[indice1];
            tab[indice1] = tab[indice2];
            tab[indice2] = tempo;
        }
    }

    public static int[] makeDeck(int nbPack) {
        int types = 4;
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}; // Jack 11 / Queen 12 / King 13

        int[] deck = new int[52*nbPack]; // jeu de 52 cartes

        for(int i = 0; i < nbPack; i++) {
            for(int j = 0; j< types; j++) {
                for(int k = 0; k < values.length; k++) {
                    deck[i*52 + j*13 + k] = values[k];
                }
            }
        }
        return deck;
    }

    public static boolean alwaysPlayer(boolean[] onlinePlayer) {
        // renvoie la présence d'au moins un joueur
        for(boolean player:onlinePlayer) {
            if(player){
               return true;
            }
        }
        return false;
    }

    public static  boolean toujoursArgentPourMiser(double[] tabsoldeActuel) {
        for(double solde:tabsoldeActuel) {
            if(solde > 0) {
                return true;
            }
        }
        return false;
    }

    public static String getStrValueCard(int numero) {
        switch (numero) {
            case 11 :
                return  "valet";
            case 12 :
                return  "reine";
            case 13 :
                return  "roi";
            case 1:
                return  "as";
            default:
                return String.format("%d", numero);
        }
    }

    public static int getIntValueCard(int numero) {
        if (numero == 1) {
            return 11;
        } else if (numero > 10) {
            return 10;
        } else {
            return numero;
        }
    }

    public static String getMain(int[] mainPersonnage, int hideEnd) {
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
        int total = 0;
        int asCpt = 0;

        for (int i = 0; i < main.length; i++) { // on ajoute toutes les valeurs au max
            if(main[i] == 1) {
                asCpt ++;
                total += 11;
            } else if(main[i] > 10 ) {
                total += 10;
            } else {
                total += main[i];
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
        int cpt = 0;
        while(main[cpt] != 0) {
            cpt++;
        }
        return cpt;
    }

    public static  boolean isBlackJack(int[] main) {
        return (getIntValueCard(main[0]) + getIntValueCard(main[1]) == 21) ? true:false;
    }

    public static double[][] playGame(int nbPlayer, int nbPack) {
        // player online
        boolean[] onlinePlayer = new boolean[nbPlayer];
        for(int i = 0; i < nbPlayer; i++) {
            onlinePlayer[i] = true;
        }

        // get solde players
        double[] tabsoldeStart = soldePlayer(nbPlayer); // ne change pas
        double[] tabsoldeActuel = new double[nbPlayer]; // évolu tout au long de la game
        for(int i = 0; i < nbPlayer; i++) {
            tabsoldeActuel[i] = tabsoldeStart[i];
        }

        // make sabot de jeu
        int[] deck = makeDeck(nbPack);

        // boucle de jeu
        int cptGame = 0;
        do {
            schuffleTab(deck); // on (re)mélange de jeu
            cptGame++;
            // ------------ affichage ------------ //
            output.println();
            output.println(String.format("Partie n° %d", cptGame));
            output.println();

            // ------------ get des mises des players ------------ //
            output.println("Choix des mises");
            output.println();
            output.println("Pour arrêter de jouer, choisir la mise 0, cet arrêt sera définitif.\nSinon, choisir une mise strictement positive.");
            output.println();


            // ------------ on prend les mises ------------ //
            double[] tabMise = new double[nbPlayer];
            for(int i = 0; i < nbPlayer; i++) {

                if (onlinePlayer[i] && tabsoldeActuel[i] > 0) { // si toujours argent pour miser
                    double mise = getMise(i, tabsoldeActuel[i]);
                    if (mise > 0) {
                        tabMise[i] = mise;
                        tabsoldeActuel[i] -= mise;
                    } else {
                        onlinePlayer[i] = false;
                    }
                }
            }

            // si y'a toujours des joueurs qui veulent jouer : //
            if(alwaysPlayer(onlinePlayer)) {

                // ------------ on applique le jeux ------------ //

                // création des main de chaque player (vide) + croupier
                int[][] playersCards = new int[nbPlayer][23]; // 23 --> consignes
                int[] croupierCards = new int[23];

                // distribution des 2 premières cartes
                int indexDeck = 0;
                for(int i = 0; i < 2; i++) { // nombre de carte
                    for(int j = 0; j < nbPlayer; j++) { // affectation de la carte aux players
                        playersCards[j][i] = deck[indexDeck];
                        indexDeck++;
                    }
                    croupierCards[i] = deck[indexDeck]; // afectation de la carte aux croupiers
                    indexDeck++;
                }

                // affichage
                for(int i = 0; i < nbPlayer; i++) { // infos player
                    if( onlinePlayer[i] ){ // si le player joue
                        String mainPlayer = getMain(playersCards[i], 0);
                        output.println(String.format("\nJoueur %d : solde = %.2f € / mise = %.2f € / cartes : %s \n",i+1, tabsoldeActuel[i], tabMise[i], mainPlayer));
                    }
                }
                String mainCroupier = getMain(croupierCards, 1); // infos croupier
                output.println(String.format("\nLe croupier a les cartes %s \n", mainCroupier) );

                output.println("\nFaites vos jeux !\n"); // globale affiche

                // chaque players joue à son tour

                for(int player = 0; player <nbPlayer; player++) {
                    int nbCards = 2;
                    int nbPoint = getPoints(playersCards[player]);

                    output.println(String.format("\n --> Tour du joueur %d", player+1));
                    String mainPlayer = getMain(playersCards[player], 0);
                    output.println(String.format("\nJoueur %d : solde = %.2f € / mise = %.2f € / cartes : %s ",player+1, tabsoldeActuel[player], tabMise[player], mainPlayer));
                    output.println(String.format("\nTu as %d points.", nbPoint));

                    String reponse;
                    do {
                        output.println("Veux-tu tirer une carte [oui/non] ?");
                        reponse = input.next();

                        while (!reponse.equalsIgnoreCase("oui") && !reponse.equalsIgnoreCase("non")) { // element robuste de upper et lower
                            output.println("Saisie incorrect !");
                            output.println("Veux-tu tirer une carte [oui/non] ?");
                            reponse = input.next();
                            output.println(reponse);
                        }

                        if(reponse.equalsIgnoreCase("oui")) {
                            int card = deck[indexDeck];
                            indexDeck ++;
                            playersCards[player][nbCards] = card;
                            nbCards++;

                            nbPoint = getPoints(playersCards[player]);

                            output.println(String.format("Tu as tiré un %s. Tu as %d points", getStrValueCard(card), nbPoint));
                        }
                    } while(reponse.equalsIgnoreCase("oui") && nbPoint < 21);
                }

                //tour croupier
                int nbPointCroupier = getPoints(croupierCards);
                int nbCards = 2;

                output.println("--> Tour du croupier");
                mainCroupier = getMain(croupierCards, 0);
                output.println(String.format("Le croupier a les cartes %s", mainCroupier));
                output.println(String.format("Il a %d points.", nbPointCroupier));

                while (nbPointCroupier < 17) {
                    int card = deck[indexDeck];
                    indexDeck++;
                    croupierCards[nbCards] = card;
                    nbCards ++;
                    nbPointCroupier = getPoints(croupierCards);

                    output.println(String.format("Le croupier a tiré un %s. Il a %d", getStrValueCard(card), nbPointCroupier));
                }





                // on met à jour le solde actuel en fonction des gagnants / perdant | distribution des gains
                output.println("\n--> Résultats du tour ! <--\n");

                output.println(String.format("Le croupier a %d points\n", nbPointCroupier));


                int ptsCroupier = getPoints(croupierCards);
                boolean isCroupierBlackJack = isBlackJack(croupierCards);
                for(int i = 0; i < nbPlayer; i++) {
                    int ptsPlayer = getPoints(playersCards[i]);
                    boolean isPlayerBlackJack = isBlackJack(playersCards[i]);

                    output.println(String.format("Résultat du joueur n°%d", i+1));
                    String mainPlayer = getMain(playersCards[i], 0);
                    output.println(String.format("solde = %.2f € / mise = %.2f € / cartes : %s ",tabsoldeActuel[i], tabMise[i], mainPlayer));
                    output.println(String.format("Tu as %d points",ptsPlayer));

                    if (isCroupierBlackJack) { // black jack du croupier
                        if (isPlayerBlackJack) { // player black jack
                            tabsoldeActuel[i] += tabMise[i];
                            // le player récupère sa mise

                            //cas 1
                            output.println(String.format("Le croupier et toi avait fait BlackJack, tu récupères ta mise, soit %.2f Euros", tabMise[i]));
                            tabsoldeActuel[i] += tabMise[i];
                            output.println(String.format("Ton solde est de %.2f", tabsoldeActuel[i]));
                        } else {

                            //cas 2
                            output.println("Tu perds contre le croupier, tu ne récupèrs rien.");
                            output.println(String.format("Ton solde est de %.2f", tabsoldeActuel[i]));
                        }
                    } else if (isPlayerBlackJack) {
                        // cas 3
                        output.println(String.format("Tu gagnes, tu récupères 3 fois ta mise, soit %.2f", tabMise[i]*3));
                        tabsoldeActuel[i] += tabMise[i]*3;
                        output.println(String.format("Ton solde est de %.2f", tabsoldeActuel[i]));

                    } else if (ptsPlayer > 21) {
                        //cas 2
                        output.println("Tu perds contre le croupier, tu ne récupèrs rien.");
                        output.println(String.format("Ton solde est de %.2f", tabsoldeActuel[i]));

                    } else if (ptsPlayer <= 21 && !isPlayerBlackJack) {
                        if (ptsCroupier > 21) {
                            //cas 4
                            output.println(String.format("Tu gagnes, tu récupères 2.5 fois ta mise, soit %.2f", tabMise[i]*2.5));
                            tabsoldeActuel[i] += tabMise[i]*2.5;
                            output.println(String.format("Ton solde est de %.2f", tabsoldeActuel[i]));

                        } else {
                            //cas 5
                            if (ptsPlayer == ptsCroupier) { // cas 1
                                output.println(String.format("Le croupier et toi avait fait le meme nombre, tu récupères ta mise, soit %.2f Euros", tabMise[i]));
                                tabsoldeActuel[i] += tabMise[i];
                                output.println(String.format("Ton solde est de %.2f", tabsoldeActuel[i]));
                            } else if (ptsPlayer > ptsCroupier) { // cas 4
                                output.println(String.format("Tu gagnes, tu récupères 2.5 fois ta mise, soit %.2f", tabMise[i]*2.5));
                                tabsoldeActuel[i] += tabMise[i]*2.5;
                                output.println(String.format("Ton solde est de %.2f", tabsoldeActuel[i]));
                            } else { // cas 2
                                output.println("Tu perds contre le croupier, tu ne récupèrs rien.");
                                output.println(String.format("Ton solde est de %.2f", tabsoldeActuel[i]));
                            }
                        }
                    }
                }


                // retrait des player qui n'ont plus de solde
                for(int i = 0; i < tabsoldeActuel.length; i++) {
                    if (tabsoldeActuel[i] == 0) {
                        onlinePlayer[i] = false;
                    }
                }
            }

        } while( toujoursArgentPourMiser(tabsoldeActuel)  && alwaysPlayer(onlinePlayer)); // encore de l'argent à miser - encore des player pour jouer

        output.println();
        output.println("Et le combat cessa faute de combattants.");

        double[][] infos = {tabsoldeStart, tabsoldeActuel};
        return infos;
    }
}
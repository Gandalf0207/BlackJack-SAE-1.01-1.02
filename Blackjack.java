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

        // get somme players
        double[] tabSomme = sommePlayer(nbPlayer);


        // affichage
        output.println();
        output.println("    PREMIÈRE PARTIE");
        output.println();

        int nbPlayerInGame = nbPlayer;
        while (nbPlayerInGame > 0) { // boucle pour jouer tant qu'il y a du monde in game
            // get des mises des players
            output.println("Choix des mises");
            output.println();
            output.println("Pour arrêter de jouer, choisir la mise 0, cet arrêt sera définitif.\nSinon, choisir une mise strictement positive.");
            output.println();

            double[] tabMise = new double[nbPlayer];

            for(int i = 0; i < nbPlayer; i++) {

                if (tabSomme[i] > 0) { // si toujours argent pour miser
                    double mise = getMise(i, tabSomme[i]);
                    if (mise > 0) {
                        tabMise[i] = mise;
                    } else {
                        nbPlayerInGame --;
                    }
                }
            }
        }

        output.println();
        output.println("Et le combat cessa faute de combattants.");
    }

    public static int getPlayer() {
        // input nb players
        int nbPlayer = 0;
        do {
            output.print("Donner le nombre de joueurs (entre 1 et 6) : ");
            nbPlayer = input.nextInt();
            if (nbPlayer < 1) {
                output.println("Réponse incorrecte !");
            }
        } while (nbPlayer < 1);

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

    public static double[] sommePlayer(int nbPlayer) {
        // get somme players
        double[] tabSomme = new double[nbPlayer];
        for(int i = 0; i < nbPlayer; i++) {
            double somme = 0;
            do {
                output.print("Donner la somme en Euros que possède le joueur 1 (entre 1.0 et 1000000.0) : ");
                somme = input.nextDouble();
                if(somme < 1.0 || somme > 1000000.0) {
                    output.println("Saisie incorrecte !");
                }
            } while (somme < 1.0 || somme > 1000000.0);
            tabSomme[i] = somme;
        }

        return  tabSomme;
    }

    public static double getMise(int i, double sommeDuPlayer) {
        // get mise player
        double mise = 0;
        do {
            output.print(String.format("Joueur %d , donne ta mise en Euros (entre 0.0 et %.2f ) : ", i+1, sommeDuPlayer)); // .format
            mise = input.nextDouble();
            if(mise < 0 || mise > sommeDuPlayer) {
                output.println("Saisie incorrecte !");
            }
        } while ( mise < 0 || mise > sommeDuPlayer);

        return mise;
    }
}
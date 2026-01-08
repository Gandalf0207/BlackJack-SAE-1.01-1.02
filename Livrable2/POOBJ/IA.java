package POOBJ;

import java.beans.beancontext.BeanContext;

/**
 * La classe IA définit le comportement intelligent du joueur.
 * Elle repose sur le calcul de l'espérance de gain (gain attendu) en comparant
 * deux stratégies : "Stand" (s'arrêter) vs "Draw" (tirer une carte).
 */
public class IA {

    // Attributs
    private int dealerCardMinValue; // Indicevaleur minimum de la carte visible du croupier (0 à 9)

    // Matrices d'espérance de gain pré-calculées
    // [ScoreJoueur][CarteCroupier]
    private static double[][] gainExpectedIfStands = new double[22][10];
    // [ScoreMinJoueur][PossèdeAs][CarteCroupier]
    private static double[][][] gainExpectedIfDraws = new double[21][2][10];

    /**
     * Constructeur de l'IA pour un tour donné.
     *
     * @param aCardMinValue Valeur faciale de la carte visible du croupier (1 à 10).
     */
    public IA(int aCardMinValue) {
        dealerCardMinValue = aCardMinValue - 1;
    }

    // --- Méthodes d'affichage (Vérification technique) ---

    /**
     * Affiche une matrice 2D et optionnellement la somme de ses lignes.
     */
    public static void displayMat(double[][] m, boolean displaySum) {
        int cpt = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                cpt += m[i][j];
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }

        if(displaySum) {
            System.out.print("Somme des lignes : " + cpt);
        }
    }

    /**
     * Affiche le tableau 3D des espérances de gain si le joueur tire.
     */
    public static void displayArray3D(double[][][] m) {
        for (int i = 0; i < m.length; i++) {
            System.out.println("i=" + i);
            for (int j = 0; j < m[i].length; j++) {
                System.out.print("  j=" + j + ": ");
                for (int k = 0; k < m[i][j].length; k++) {
                    System.out.print(m[i][j][k] + " ");
                }
                System.out.println();
            }
        }
    }

    // --- Logique de Simulation (Monte-Carlo) ---

    /**
     * Simule un tour complet du croupier à partir d'une carte initiale.
     *
     * @return Le résultat codé : 0 si Bust (>21), 1 à 5 pour les scores 17 à 21, 6
     *         pour Blackjack.
     */
    public static int simulation(int i) {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Calcule les probabilités des scores finaux du croupier pour une carte donnée.
     */
    public static double[] computeLineDealerSP(int i, int nbSimul) {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Remplit la matrice globale des probabilités du score du croupier.
     */
    public static double[][] computeDealerScoreProba(int nbSimul) {
        double[][] dealerScoreProba = new double[10][7];


        return dealerScoreProba;
    }

    /**
     * Vérifie si deux matrices de probabilités sont proches à un epsilon près.
     */
    public static boolean checkSameProba(double[][] m1, double[][] m2, double epsilon) {
        for(int i = 0; i < m1.length; i++) {
            for(int j = 0; j < m1[i].length; j++) {
                if (Math.abs(m1[i][j] - m2[i][j]) > epsilon) {
                    return false;
                }
            }
        }

        return true;

    }

    /**
     * Détermine le nombre de simulations nécessaires pour stabiliser les
     * probabilités.
     */
    public static double[][] computeDealerScoreProba(double epsilon) {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    // --- Calcul des Espérances ---

    /**
     * Pre-requis : 0 <= bestScore <= 21 et 0 <= y <= 6
     * Resultat : le gain du joueur (-1, 0 ou 1.5) si son
     * score final est bestScore et celui du croupier est
     * represente par y
     */
    public static double gain(int bestScore, int y) {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Remplit la matrice de gain si le joueur décide de s'arrêter (Stand).
     */
    public static void computeGainExpectedIfStands() {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Calcule le meilleur score théorique après avoir pioché une carte spécifique.
     */
    public static int theBestScore(int minScore, int hasAnAce, int rank) {

        int bestScore = minScore;

        if(bestScore + rank < 22) {
            bestScore += rank;
            if (bestScore < 12 && hasAnAce==1) {
                bestScore += 10;
            }
        }
        else {
            bestScore = 0;
        }

        return bestScore;
    }

    /**
     * Calcule l'espérance de gain moyenne si le joueur tire une carte
     * supplémentaire.
     */
    public static void computeGainExpectedIfDraws() {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    // --- Méthodes de décision (Utilisées par Player) ---

    /**
     * Décision : Tirer si l'espérance de gain en tirant est supérieure à celle de
     * s'arrêter.
     */
    public boolean chooseToDraw(int minScore, boolean hasAnAce, int bestScore) {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * * Décision : Doubler si on a l'intention de tirer ET que l'espérance est
     * positive.
     */
    public boolean chooseDoubleBet(int minScore, boolean hasAnAce, int bestScore) {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * * Décision : Abandonner si s'arrêter est préférable à tirer, mais que
     * l'espérance reste négative.
     */
    public boolean chooseToSurrender(int minScore, boolean hasAnAce, int bestScore) {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Justification mathématique de l'assurance :
     * L'assurance coûte 1/4 de la mise et rapporte 2x si le croupier a un
     * Blackjack.
     * La probabilité que le croupier ait un 10 (10, J, Q, K) est de 4/13.
     * Espérance = (4/13 * Gain) + (9/13 * Perte) = (4/13 * 0.5) + (9/13 * -0.25) =
     * 2/13 - 2.25/13 = -0.25/13.
     * L'espérance est négative, donc l'IA retourne 'false'.
     */
    public boolean chooseInsurance() {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

} // end class IA

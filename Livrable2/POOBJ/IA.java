package POOBJ;

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

    ///
    ///
    private static final UserInterface interfaceUser = new UserInterface(false);
    private static final Shoe shoe = new Shoe(4);
    private static final Dealer dealer = new Dealer(shoe, interfaceUser);

    /**
     * Constructeur de l'IA pour un tour donné.
     *
     * @param aCardMinValue Valeur faciale de la carte visible du croupier (1 à 10).
     */
    public IA(int aCardMinValue) {
        dealerCardMinValue = aCardMinValue-1;
    }

    // --- Méthodes d'affichage (Vérification technique) ---

    /**
     * Affiche une matrice 2D et optionnellement la somme de ses lignes.
     */
    public static void displayMat(double[][] m, boolean displaySum) {
            System.out.println("Gain :");

            for (double[] m1 : m) {
                String line = "";
                double sum = 0.0;
                for (int j = 0; j < m1.length; j++) {
                    sum += m1[j];
                    line += "\nContre " + (j + 1) + " : " + m1[j];
                    if (j < m1.length - 1) {
                        line += ", ";
                    }
                }
                if (displaySum) {
                    double avg = sum / m1.length;
                    line += "\nMoyenne des gains : " + avg;
                }
                System.out.println(line + "\n");
            }
        }

        /**

    Affiche le tableau 3D des espérances de gain si le joueur tire.*/
    public static void displayArray3D(double[][][] m) {
        int maxTab = m.length;
        for (int i = 0; i < maxTab; i++) {
            System.out.println("Score " + (i + 1) + " : ");
            displayMat(m[i], true);

            }
        }

    // --- Logique de Simulation (Monte-Carlo) ---

    /**
     * Simule un tour complet du croupier à partir d'une carte initiale.
     *
     * @return Le résultat codé : 0 si Bust (>21), 1 à 5 pour les scores 17 à 21, 6
     *         pour Blackjack.
     */
    public static int simulation(int i) { // i compris entre 0 et 9
        Card carte = new Card(i+1); // la valeur 10 = 4 xarte différentes, donc proba combiné

        dealer.reset();
        dealer.takeCard(carte);
        dealer.takeCard(shoe.drawCard());
        dealer.playDrawingPhase();

        int bestScore = dealer.bestScore();

        //tableau pdf valeur
        if(bestScore > 21) {
            return 0;
        }
        else if (dealer.hasBlackjack()) {
            return 6;
        }
        else {
            return bestScore- 16;
        }
    }

    /**
     * Calcule les probabilités des scores finaux du croupier pour une carte donnée.
     */
    public static double[] computeLineDealerSP(int i, int nbSimul) {
        double[] tabSimulForI = new double[7];
        for(int j = 0; j < nbSimul; j++ ) {
            tabSimulForI[simulation(i)]++;
        }

        for(int j = 0; j < 7; j++) {
            tabSimulForI[j] /= nbSimul*1.0;
        }

        return tabSimulForI;
    }

    /**
     * Remplit la matrice globale des probabilités du score du croupier.
     */
    public static double[][] computeDealerScoreProba(int nbSimul) {
        double[][] dealerScoreProba = new double[10][7];

        for (int i = 0; i < 10; i++) {
            dealerScoreProba[i] = computeLineDealerSP(i, nbSimul);
        }

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
    public static double[][] computeDealerScoreProba(double epsilon) { // IA
        int n = 10000;
        double[][] prev = new double[10][7];
        double[][] cur = computeDealerScoreProba(n);
        do {
            for (int i = 0; i < prev.length; i++) {
                prev[i] = cur[i];
            }
            n *= 2;
            cur = computeDealerScoreProba(n);
        } while (!checkSameProba(prev, cur, epsilon));
        return cur;
    }

    // --- Calcul des Espérances ---

    /**
     * Pre-requis : 0 <= bestScore <= 21 et 0 <= y <= 6
     * Resultat : le gain du joueur (-1, 0 ou 1.5) si son
     * score final est bestScore et celui du croupier est
     * represente par y
     */
    public static double gain(int bestScore, int y) {
        if (bestScore > 21 || bestScore == 0)
            return -1.0;

        if (y == 6) {
            return -1.0;
        }

        if (y == 0) {
            return 1.5;
        }

        int dealerScore = y + 16;

        if (bestScore > dealerScore)
            return 1.5;
        if (bestScore < dealerScore)
            return -1.0;

        return 0.0;

    }

    /**
     * Remplit la matrice de gain si le joueur décide de s'arrêter (Stand).
     */
    public static void computeGainExpectedIfStands() {
        double[][] dealerScoreProba = computeDealerScoreProba(0.003);

        for(int bestScore = 0; bestScore < 22; bestScore ++) {
            for(int dealerCardMinValue = 0; dealerCardMinValue < 10; dealerCardMinValue++) {
                double esperance = 0;

                for(int y = 0; y < 7; y++) {
                    double gain = gain(bestScore, y);
                    double p = dealerScoreProba[dealerCardMinValue][y];
                    esperance += gain*p;
                }
                gainExpectedIfStands[bestScore][dealerCardMinValue] = esperance;
            }
        }

    }

    /**
     * Calcule le meilleur score théorique après avoir pioché une carte spécifique.
     */
    public static int theBestScore(int minScore, int hasAnAce, int rank) {
        Card newCard = new Card(rank);

        int newMinScore = minScore + newCard.minValue();

        if(newMinScore > 21) {
            return 0;
        }

        if (newMinScore + 10 <= 21 && (hasAnAce==1 || newCard.isAnAce())) {
            return newMinScore += 10;
        }
        else {
            return newMinScore;
        }
    }

    /**
     * Calcule l'espérance de gain moyenne si le joueur tire une carte
     * supplémentaire.
     */
    public static void computeGainExpectedIfDraws() {
        computeGainExpectedIfStands();
        for(int bestScore = 0; bestScore < 21; bestScore ++) {
            for(int as = 0; as < 2; as++) {
                for(int y = 0; y < 10; y++) {
                    double somme = 0;

                    for(int carte = 1; carte < 14; carte ++) {
                        int newScore = theBestScore(bestScore, as, carte);
                        somme += gainExpectedIfStands[newScore][y];
                    }

                    somme /= 13.0;
                    gainExpectedIfDraws[bestScore][as][y] = somme;
                }
            }
        }
    }

    // --- Méthodes de décision (Utilisées par Player) ---

    /**
     * Décision : Tirer si l'espérance de gain en tirant est supérieure à celle de
     * s'arrêter.
     */
    public boolean chooseToDraw(int minScore, boolean hasAnAce, int bestScore) {
        if (bestScore >= 21) {
            return false;
        }
        return gainExpectedIfStands[bestScore][this.dealerCardMinValue] < gainExpectedIfDraws[minScore][hasAnAce ? 1:0][this.dealerCardMinValue];
    }

    /**
     * * Décision : Doubler si on a l'intention de tirer ET que l'espérance est
     * positive.
     */
    public boolean chooseDoubleBet(int minScore, boolean hasAnAce, int bestScore) {
        if (bestScore >= 21) {
            return false;
        }

        double gainDraw = gainExpectedIfDraws[minScore][hasAnAce ? 1:0][this.dealerCardMinValue]*2;
        return gainExpectedIfStands[bestScore][this.dealerCardMinValue] < gainDraw && gainDraw > 0.5;
    }

    /**
     * * Décision : Abandonner si s'arrêter est préférable à tirer, mais que
     * l'espérance reste négative.
     */
    public boolean chooseToSurrender(int minScore, boolean hasAnAce, int bestScore) {
        double gainS = gainExpectedIfStands[bestScore][this.dealerCardMinValue];
        double gainD = gainExpectedIfDraws[minScore][hasAnAce ? 1:0][this.dealerCardMinValue];
        return gainS < -0.5 && gainD < -0.5 ;
    }

    /**
     *
     * Pre-requis : la carte visible du croupier est un as
     * Resultat : true si le joueur choisit de s'assurer et false sinon
     * Mettre en commentaire ici la justification de votre resultat
     *  (calcul d'esperance simple) ==>
     *
     *
     */
    public boolean chooseInsurance() {
        return false;
    }

} // end class IA

/**
 * Classe Contrôleur qui orchestre le déroulement d'une session de Blackjack.
 * Elle gère l'enchaînement des phases (mises, distribution, tours, résultats)
 * et supervise le mode simulation pour tester la stratégie de l'IA.
 *
 * Remarque sur les scores paticuliers 
 * -------------------------------------------
 * Dans le rendu 1, un Blackjack est représenté par le score 22
 * et un dépassement de 21 points par le score 0 pour le croupier,
 * le score -1 pour le joueur
 * Ici (dernier rendu), pour les tableaux IA, un Blackjack est représenté par le score 22
 * et un dépassement de 21 points par le score 0 pour le croupier
 * et le joueur.
 * Pour le jeu de ce dernier rendu, le score est le nombre réel de points obtenus par le croupier 
 * ou le joueur : un Blackjack est représenté par le score 21 et l'attribut hasBlackjack
 * égal à true, et un dépassement de 21 points par ce même score dépassant 21 points.
 */

public class BlackjackGame {

    private Player[] players;       // Tableau des joueurs
    private Dealer dealer;          // Le croupier (la banque)≈Ò
    private int nbActive;           // Nombre de joueurs encore dans le jeu
    private boolean displayRounds;  // true : mode interactif, false :  mode simulation (test de la stratégie)
    private UserInterface ui;
    private double coefBlackjack;   // Multiplicateur de gain pour un Blackjack
    
    // Variables dédiées au suivi statistique (mode simulation)
    private double initialBalance;
    private double currentBalance;
    private int nbRounds;
    private int nbWinningRounds;


    // --- Constructeur ---

    /**
     * Initialise une nouvelle session de jeu.
     * pré-requis : 1 <= coefBlackjack <= 5 et 1 <= initialBalance <= 10000
     */
    public BlackjackGame(Player[] players, Dealer dealer, boolean displayRounds, UserInterface ui,
                         double coefBlackjack, double initialBalance) {
	throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Action : Remet à zéro la main du croupier et de chaque joueur actif 
     *          avant de commencer un nouveau tour.
     */
    public void reset() {
        for (Player p : this.players) {
            if(p.active()){
                p.reset();
            }
        } 
        this.dealer.reset();
    }
  
    /**
     * Action : Sollicite chaque joueur pour sa mise.
     * Si un joueur mise 0, il est retiré des joueurs actifs.
     */
    public void collectBets() {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Action : Distribue les deux premières cartes à tout le monde.
     * @return La première carte visible du croupier (Up-Card).
     */
    public Card dealInitialCards() {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Action : Affiche l'état initial du tour (état du jeu des joueurs et carte visible du croupier).
     */
    public void displayAllVisibleCards(Card upCard) {
	throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /** Action : Gère le tour de parole de chaque joueur, puis celui du croupier.
     *  @param upCardMinValue Valeur de la carte visible du croupier pour la stratégie et l'assurance.
     */
    public void playTurns(int upCardMinValue) {
	throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Action : Orchestre le calcul des gains (Modèle) et leur affichage (Vue).
     * @return Le solde du dernier joueur traité (utile pour la simulation mono-joueur).
     */
    public double processAndDisplayResults(){
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    } 

    /**
     * Action : Détermine si le jeu doit s'arrêter.
     * En mode interactif : s'arrête si plus de joueurs actifs.
     * En mode simulation : délègue à (appelle) roundsSimulation.
     * @return true si une nouvelle manche peut commencer, false sinon
     */
    public boolean endOfRound(double balance) { 
	throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Utilitaire pour arrondir un montant à 2 décimales (format monétaire).
     */
    public static double round2digits(double x){ 
        return ((double) Math.round(x * 100)) / 100;
    }

    /** Logique de test de l'IA : Calcule les statistiques de performance de la stratégie.
     *  Conditions d'arrêt : Faillite, Solde doublé, ou limite de 10 x le solde en nombre de parties.
     *  @return true si une nouvelle partie peut commencer, false sinon.
     */
    public boolean roundsSimulation(double balance) {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Action : Déroule un cycle complet d'une partie.
     * @return true si une nouvelle partie peut commencer, false sinon.
     */
    public boolean playRound() {
        this.reset();
        this.ui.displayMessage("\n--- Nouvelle partie : Réinitialisation effectuée ---\n");

        this.ui.displayMessage("Collecte des mises...");
        this.collectBets();
        
        if(nbActive == 0){
            this.ui.displayMessage("Aucun joueur actif pour commencer la partie.");
            return false;
        }
        
        this.ui.displayMessage("Distribution des cartes...");
        Card upCard = this.dealInitialCards();

        this.displayAllVisibleCards(upCard);
        
        this.ui.displayMessage("Tours des joueurs et du croupier...");
        this.playTurns(upCard.minValue());
        
        this.ui.displayMessage("\n--> Résultats de la partie <--\n");
        double balance = this.processAndDisplayResults(); 
        
        return this.endOfRound(balance);
    }

    /**
     * Point d'entrée pour lancer la session de jeu.
     * Boucle tant que les conditions de fin (endOfRound) ne sont pas remplies.
     */
    public void play() {
        this.ui.displayMessage("\n    PREMIÈRE PARTIE");
        while (this.playRound()) { 
            this.ui.displayMessage("\n    NOUVELLE PARTIE ?");
        } 
    }

} // end class BlackjackGame


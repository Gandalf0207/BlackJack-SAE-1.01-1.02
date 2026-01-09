package POOBJ;

/**
 * Représente le Croupier (la banque) du jeu.
 * Le Croupier suit des règles de jeu automatiques et rigides :
 * il doit tirer des cartes tant qu'il n'a pas atteint un score de 17.
 */
public class Dealer {

    private Shoe shoe; // Le sabot contenant les paquets de cartes
    private UserInterface ui; // L'interface pour afficher les actions du croupier

    private Hand hand; // La main courante du croupier
    private boolean hasBlackjack; // Indicateur de Blackjack (21 points avec les 2 premières cartes)

    /**
     * Constructeur du Croupier.
     *
     * @param shoe Le sabot de cartes à utiliser pour la partie.
     * @param ui   L'interface utilisateur pour les affichages.
     */
    public Dealer(Shoe shoe, UserInterface ui) {
        this.shoe = shoe;
        this.ui = ui;

        this.hand = new Hand(20);
    }

    /**
     * Action : Réinitialise l'état du croupier avant de commencer une nouvelle
     * partie.
     * Vide la main, mélange le sabot et remet le flag Blackjack à faux.
     */
    public void reset() {
        this.hasBlackjack = false;
        this.hand.reset();
        this.shoe.reset();
    }

    /**
     * @return Une représentation textuelle de la main et du score du croupier.
     */
    public String toString() {
        return String.format("Les cartes : %S. Le croupier a %s points", this.hand.cardsAsString(), this.bestScore());
    }

    /**
     * @return Vrai si le croupier possède un Blackjack, faux sinon.
     */
    public boolean hasBlackjack() {
        return this.hasBlackjack;
    }

    /**
     * @return Le meilleur score possible de la main du croupier (gestion de l'As).
     */
    public int bestScore() {
        return this.hand.bestScore();
    }

    /**
     * Action : Pioche la carte suivante directement depuis le sabot.
     *
     * @return La carte piochée.
     */
    public Card drawCard() {
        return this.shoe.drawCard();
    }

    /**
     * Action : Ajoute physiquement une carte dans la main du Croupier.
     *
     * @param card La carte à ajouter.
     */
    public void takeCard(Card card) {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Action : Phase de pioche automatique du croupier.
     * La règle est stricte : tant que le score est inférieur à 17, le croupier
     * tire.
     */
    public void playDrawingPhase() {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Action : Gère l'intégralité du tour du croupier.
     * 1. Révèle sa main complète (incluant la carte cachée).
     * 2. Vérifie s'il y a un Blackjack immédiat.
     * 3. Sinon, lance la phase de pioche du dealer.
     */
    public void playTurn() {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

} // end class Dealer

package POOBJ;

/**
 * Représente la main d'un joueur ou du croupier durant une partie.
 * Gère dynamiquement le calcul des scores (minimal et optimal).
 */
public class Hand {

    private CardSequence cards; // La séquence de cartes contenue dans la main
    private int minScore;       // Somme des points en comptant l'As pour 1
    private boolean hasAnAce;   // Indique si la main contient au moins un As
    private int bestScore;      // Meilleur score possible sans dépasser 21 (As valant 1 ou 11)

    /**
     * Constructeur : crée une main vide pouvant contenir jusqu'à nbCardsMax.
     * @param nbCardsMax Nombre maximum de cartes (typiquement 21 pour un joueur).
     */
    public Hand(int nbCardsMax) {
        this.cards = new CardSequence(nbCardsMax, true);
    }

    /**
     * Action : Vide la main et réinitialise les scores à zéro.
     */
    public void reset() {
        this.minScore = 0;
        this.bestScore = 0;
        this.hasAnAce = false;
        this.cards.reset();
    }

    /**
     * @return Une représentation textuelle des cartes présentes dans la main.
     */
    public String cardsAsString(){
        return String.format("Les cartes dans la main son : %s", this.cards.toString());
    }

    /**
     * Action : Ajoute une carte à la main et met à jour les scores.
     * @param c La carte tirée du sabot.
     */
    public void addCard(Card c) {
        this.cards.addCard(c);

        if(c.minValue() == 1) {
            this.hasAnAce = true;
        }

        if(this.minScore + c.minValue() < 22) {
            this.minScore += c.minValue();

            if (this.hasAnAce && this.minScore + 10 < 22) {
                this.bestScore = this.minScore + 10;
            }
            else {
                this.bestScore = this.minScore;
            }
        }
        else {
            this.minScore = 0;
            this.bestScore = 0;
        }


        this.minScore = this.minScore();
        this.hasAnAce = this.hasAnAce ? this.hasAnAce:this.hasAnAce();
        this.bestScore = this.hasAnAce ? this.bestScore():this.minScore;
    }

    /** @return Le score minimal (As = 1). */
    public int minScore() {
        return this.minScore;
    }

    /** @return Vrai si la main possède au moins un As. */
    public boolean hasAnAce(){
        return this.hasAnAce;
    }

    /** @return Le score optimal (As = 11 si possible). */
    public int bestScore() {
        return this.bestScore;
    }

} // end class Hand

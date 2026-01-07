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
        this.minScore = this.minScore();
        this.hasAnAce = this.hasAnAce ? this.hasAnAce:this.hasAnAce();
        this.bestScore = this.hasAnAce ? this.bestScore():this.minScore;
    }

    /** @return Le score minimal (As = 1). */
    public int minScore() {
        String[] allCards = cards.toString().split(" ");
        int cpt = 0;
        for(int i = 0; i < allCards.length; i++) {
            cpt += Integer.parseInt(allCards[i]);
        }

        return cpt > 21 ? 0:cpt;
    }

    /** @return Vrai si la main possède au moins un As. */
    public boolean hasAnAce(){
        String[] allCards = cards.toString().split(" ");
        for(int i = 0; i < allCards.length; i++) {
            if(Integer.parseInt(allCards[i]) == 1) {
                return true;
            }
        }
        return false;
    }

    /** @return Le score optimal (As = 11 si possible). */
    public int bestScore() {
        return (this.minScore + 10) > 21 ? this.minScore:this.minScore+10;
    }

} // end class Hand

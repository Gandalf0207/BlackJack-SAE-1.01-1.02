package POOBJ;

/**
 * Représente le sabot (ensemble de cartes) utilisé par le croupier.
 * Remarque :
 * Dans le rendu 1, le mot deck désignait le sabot,
 * ici, il désigne un paquet de 52 cartes... et c'est mieux en anglais ! ;-)
 */

public class Shoe {

    private CardSequence cards; // Ensemble des cartes disponibles
    private int numberOfDecks; // Nombre de paquets de 52 cartes utilisés

    /**
     * Constructeur : Remplit le sabot avec n paquets de 52 cartes.
     * 
     * @param n Nombre de paquets (entre 1 et 8).
     */
    public Shoe(int n) {
        this.numberOfDecks = n;
        this.cards = new CardSequence(n * 52, false);
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < 14; j++) {
                for (int k = 0; k < 4; k++) {
                    this.cards.addCard(new Card(j));
                }
            }
        }
    }

    /**
     * Action : Remet toutes les cartes initiales dans le sabot et les mélange.
     */
    public void reset() {
        this.cards.reset();
    }

    /**
     * Action : Retire la carte du dessus du sabot.
     * 
     * @return La carte tirée.
     */
    public Card drawCard() {
        return this.cards.removeCard();
    }

} // end class Shoe

import java.util.ArrayList;
import java.util.Collections;

/**
 * Représente le sabot (ensemble de cartes) utilisé par le croupier.
 * Remarque :
 * Dans le rendu 1, le mot deck désignait le sabot,
 *  ici, il désigne un paquet de 52 cartes... et c'est mieux en anglais ! ;-)
 */

public class Shoe { 

    private CardSequence cards;   // Ensemble des cartes disponibles
    private int numberOfDecks;    // Nombre de paquets de 52 cartes utilisés
    
    /**
     * Constructeur : Remplit le sabot avec n paquets de 52 cartes.
     * @param n Nombre de paquets (entre 1 et 8).
     */
    public Shoe(int n) {
	throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Action : Remet toutes les cartes initiales dans le sabot et les mélange.
     */
    public void reset() {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }
    
    /**
     * Action : Retire la carte du dessus du sabot.
     * @return La carte tirée.
     */
    public Card drawCard() {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

} // end class Shoe

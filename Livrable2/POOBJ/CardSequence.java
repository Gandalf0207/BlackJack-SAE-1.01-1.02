import java.util.Random;

/**
 * Structure de données de base stockant une suite de cartes.
 * Utilisée à la fois pour les mains (Hand) et pour le sabot (Shoe).
 */
public class CardSequence {
    
    private Card[] seq;         // Tableau de stockage des objets Card
    private int nbCards;        // Nombre effectif de cartes actuellement dans la séquence
    private boolean isInAHand;  // Distingue le comportement (Main vs Sabot)

    public static Random random = new Random();

    /**
     * Pré-requis : nbCardsMax >= 0
     * @param nbCardsMax Capacité maximale du tableau.
     * @param isInAH Indique si la séquence appartient à une main (true) ou un sabot (false).
     */
    public CardSequence(int nbCardsMax, boolean isInAH){
	throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Action : Réinitialise la séquence.
     * Si c'est une main, elle devient vide.
     * Si c'est un sabot, elle redevient pleine et est mélangée.
     */
    public void reset(){
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * @return Chaîne listant le nom de toutes les cartes présentes.
     */
    public String toString(){
	throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Pré-requis : this.nbCards < this.seq.length
     * Action : Ajoute une carte à la fin de la séquence.
     * @param newCard La carte à ajouter.
     */
    public void addCard(Card newCard){
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Pré-requis : this.nbCards > 0
     * Action : Retire et renvoie la dernière carte de la séquence.
     * @return La carte retirée.
     */
    public Card removeCard(){
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }
    
    /**
     * Pré-requis : this.isInAHand = false
     * Action : Mélange aléatoirement les cartes du tableau (algorithme de permutation).
     */
    public void shuffleCards(){
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

} // end class CardSequence


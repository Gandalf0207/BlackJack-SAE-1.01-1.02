package POOBJ;

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
        this.seq = new Card[nbCardsMax];
        this.isInAHand = isInAH;
        this.nbCards = 0;
    }

    /**
     * Action : Réinitialise la séquence.
     * Si c'est une main, elle devient vide.
     * Si c'est un sabot, elle redevient pleine et est mélangée.
     */
    public void reset(){
        if(this.isInAHand) {
            this.nbCards = 0;
        }
        else {
            this.nbCards = this.seq.length;
            this.shuffleCards();
        }
    }

    /**
     * @return Chaîne listant le nom de toutes les cartes présentes.
     */
    public String toString(){
        String list = "";
        for(int i = 0; i < this.nbCards; i++) {
            list += this.seq[i].toString() + " ";
        }
        return list;
    }

    /**
     * Pré-requis : this.nbCards < this.seq.length
     * Action : Ajoute une carte à la fin de la séquence.
     * @param newCard La carte à ajouter.
     */
    public void addCard(Card newCard){
        this.seq[this.nbCards] = newCard;
        this.nbCards ++;
    }

    /**
     * Pré-requis : this.nbCards > 0
     * Action : Retire et renvoie la dernière carte de la séquence.
     * @return La carte retirée.
     */
    public Card removeCard(){
        if(this.nbCards == 0 && !this.isInAHand) {
            this.reset();
        }
        this.nbCards --;
        return this.seq[this.nbCards];
    }
    
    /**
     * Pré-requis : this.isInAHand = false
     * Action : Mélange aléatoirement les cartes du tableau (algorithme de permutation).
     */
    public void shuffleCards(){
        if(!this.isInAHand) {
            for(int i = 0; i < this.seq.length; i++) {
                int nb1 = random.nextInt(0, this.seq.length);
                int nb2 = random.nextInt(0, this.seq.length);
                Card tempo = this.seq[nb1];
                this.seq[nb1] = this.seq[nb2];
                this.seq[nb2] = tempo;
            }
        }
    }

} // end class CardSequence


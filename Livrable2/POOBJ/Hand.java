import java.util.ArrayList;

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
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire"); 
    }

    /**
     * Action : Vide la main et réinitialise les scores à zéro.
     */
    public void reset() { 
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * @return Une représentation textuelle des cartes présentes dans la main.
     */
    public String cardsAsString(){
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /**
     * Action : Ajoute une carte à la main et met à jour les scores.
     * @param c La carte tirée du sabot.
     */
    public void addCard(Card c) {
	throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire"); 
    }

    /** @return Le score minimal (As = 1). */
    public int minScore() {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire"); 
    }

    /** @return Vrai si la main possède au moins un As. */
    public boolean hasAnAce(){
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

    /** @return Le score optimal (As = 11 si possible). */
    public int bestScore() {
        throw new RuntimeException("Méthode non implémentée ! Effacez cette ligne et écrivez le code nécessaire");
    }

} // end class Hand

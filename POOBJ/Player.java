package POOBJ;

/**
 * Représente un joueur de Blackjack, qu'il soit humain ou contrôlé par l'IA.
 * Gère le solde, la mise, la main de cartes et les décisions stratégiques
 * (tirer, s'arrêter, doubler sa mise, s'assurer ou abandonner).
 */
public class Player {

    private int num; // Identifiant unique du joueur (1 à nbPlayers)
    private boolean active = true; // Indique si le joueur est toujours dans le jeu (solde > 0 et le joueur n'a
                                   // jamais annoncé une mise nulle)
    private boolean human; // true si le joueur est humain, false si c'est une IA
    private double balance; // Solde courant du joueur en Euros
    private UserInterface ui; // Lien vers l'interface utilisateur

    private double bet; // Mise engagée pour le tour actuel
    private Hand hand = new Hand(20); // Main du joueur (capacité max de 20 cartes)

    // Indicateurs d'état pour le tour en cours
    private boolean hasBlackjack;
    private boolean doubleBet;
    private boolean insurance;
    private boolean surrender;
    private IA strategy; // Instance de l'IA pour les décisions automatiques

    /**
     * Constructeur du joueur.
     *
     * @param num     Numéro d'ordre du joueur.
     * @param human   Nature du joueur.
     * @param balance Capital de départ.
     * @param ui      Interface de communication.
     */
    public Player(int num, boolean human, double balance, UserInterface ui) {
        this.num = num;
        this.human = human;
        this.balance = balance;
        this.ui = ui;
    }

    /**
     * Action : Réinitialisation avant une nouvelle partie.
     * Vide la main et remet à zéro les drapeaux de décision (Blackjack, Double,
     * Assurance, Abandon).
     */
    public void reset() {
        this.hand.reset();
        this.hasBlackjack = false;
        this.doubleBet = false;
        this.insurance = false;
        this.surrender = false;
    }

    /**
     * Fonction rajoutée pour l'affichage du score.
     *
     * @param isFinalScore true si le score affiché est le score définitif du tour.
     * @return true si les deux valeurs (minScore et bestScore) doivent être
     *         affichées, false si seule bestScore est à affichée.
     *         Note : Les deux scores sont affichés ssi ils sont différents et que
     *         le joueur peut encore choisir de tirer une carte ou non
     *         (il n'a pas doublé sa mise, n'a pas atteint ni dépassé 21 points et
     *         son score n'est pas définitif).
     */
    public boolean displayMinScore(boolean isFinalScore) {
        return (this.minScore() != this.bestScore())
                && !isFinalScore && !this.doubleBet
                && this.minScore() < 21 && this.bestScore() < 21;
    }

    /**
     * Formate le score pour l'affichage.
     *
     * @param isFinalScore Indique si c'est le s core final.
     * @return Une chaîne type ", tu as 7 ou 17 points." ou ", tu as 17 points."
     */
    public String scoreToString(boolean isFinalScore) {
        return displayMinScore(isFinalScore) ? String.format("tu as %s ou %s points", this.minScore(), this.bestScore())
                : String.format("tu as %s points", this.bestScore());
    }

    /**
     * @return État complet du joueur (numéro, solde, mise, assurance éventuelle,
     *         cartes et score).
     */
    public String playerToString(boolean isFinalScore) {
        return String.format("Joueurs : n°%s, solde : %s€, mise : %s€, assurence : %s, cartes : %s, score : %s",
                this.num, this.balance, this.bet, this.insurance, this.hand.cardsAsString(),
                this.scoreToString(isFinalScore));
    }

    // --- Accesseurs de score basés sur la classe Hand ---
    public int minScore() {
        return this.hand.minScore();
    }

    public boolean hasAnAce() {
        return this.hand.hasAnAce();
    }

    public int bestScore() {
        return this.hand.bestScore();
    }

    /** @return true si le joueur participe encore au jeu. */
    public boolean active() {
        return this.active;
    }

    /**
     * Action : Ajoute une carte à la main du joueur.
     */
    public void takeCard(Card card) {
        this.hand.addCard(card);
    }

    /**
     * Action : Retire définitivement le joueur du jeu.
     */
    public void eliminate() {
        this.active = false;
    }

    /**
     * Action : Demande la mise initiale au joueur.
     * Si la mise est de 0, le joueur est éliminé. Sinon, la mise est déduite du
     * solde.
     *
     * @return true si le joueur quitte la table (mise nulle).
     */
    public boolean eliminatedWhenCollectingBet() {
        double computerBet = Math.min(1.0, this.balance); // indication file blackjack main
        this.bet = ui.askForBet(this.human, this.num, this.balance, computerBet);
        if(this.bet == 0.0) {
            this.eliminate();
            return true;
        }
        else {
            this.balance -= this.bet;
            return false;
        }
    }

    /**
     * Action : Propose au joueur de doubler sa mise.
     * Si oui, une seconde mise identique est prélevée.
     * Note : Doubler sa mise limite le joueur à piocher une seule et unique carte
     * supplémentaire.
     */
    public void chooseDoubleBet() {
        if(this.balance >= this.bet) {
            if(ui.askForDoubleBet(this.human, this.strategy.chooseDoubleBet(this.minScore(), this.hasAnAce(), this.bestScore()))) {
                this.balance -= this.bet;
                this.bet*= 2;
                this.doubleBet = true;
            }
        }
    }

    /**
     * Action : Gère l'option d'assurance contre le Blackjack du croupier.
     * Pré-requis : La carte visible du croupier doit être un As.
     * Le coût est fixé au quart (1/4.0) de la mise actuelle.
     */
    public void chooseInsurance() {
        if(this.ui.askForInsurance(this.human, this.bet / 4.0, this.strategy.chooseInsurance())) {
            this.insurance = true;
            this.balance -= this.bet / 4.0;
        }
    }

    /**
     * Action : Demande au joueur s'il souhaite abandonner le tour (Surrender).
     */
    public void chooseToSurrender() {
        if (ui.askForSurrender(this.human, this.bet, this.strategy.chooseToSurrender(this.minScore(), this.hasAnAce(), this.bestScore()))) {
            this.surrender = true;
        }
    }

    /**
     * Action : Gère la phase où le joueur demande des cartes (Hit) ou s'arrête
     * (Stand).
     * Si le joueur a doublé sa mise, il ne reçoit qu'une seule carte et la boucle
     * s'arrête.
     */
    public void playDrawingPhase(Dealer dealer) {
        boolean draw = true;
        if (!this.doubleBet) {
            draw = this.ui.askForHitOrStand(this.human,
                    this.strategy.chooseToDraw(this.minScore(), this.hasAnAce(), this.bestScore()));
        }
        while (draw) {
            Card newCard = dealer.drawCard();
            this.takeCard(newCard);
            this.ui.displayCardDrawnAndScorePlayer(newCard, this.scoreToString(false));
            draw = false; // Par défaut, s'arrête (cas de double mise ou de score >= 21)

            // Si pas de double mise et score < 21, on redemande au joueur
            if (!this.doubleBet && (this.bestScore() < 21)) {
                draw = this.ui.askForHitOrStand(this.human,
                        this.strategy.chooseToDraw(this.minScore(), this.hasAnAce(), this.bestScore()));
            }
        }
        if (this.bestScore() > 21) {
            this.ui.displayMessage("Tu as dépassé 21 points !");
        } else if (this.displayMinScore(false)) {
            this.ui.displayMessage("Tu as finalement " + this.bestScore() + " points.");
        }
    }

    /**
     * Action : Orchestre le tour complet du joueur.
     * Séquence : Initialisation stratégie IA -> Vérification Blackjack -> Double
     * mise -> Assurance -> Abandon -> Pioche.
     *
     * @param dealer         Le croupier (pour piocher des cartes).
     * @param upCardMinValue Valeur de la carte visible du croupier.
     */
    public void playTurn(Dealer dealer, int upCardMinValue) {
        this.strategy = new IA(upCardMinValue);

        // blackjack
        if (this.bestScore() == 21) {
            this.hasBlackjack = true;
            return;
        }

        this.chooseDoubleBet();

        if(upCardMinValue == 1) {
            this.chooseInsurance();
        }

        if(!this.doubleBet) {
            this.chooseToSurrender();
        }

        if (!this.surrender) {
            this.playDrawingPhase(dealer);
        }
    }

    /**
     * Action : calcule le gain brut selon le score du joueur face au croupier,
     * c'est-à-dire ce que reçoit
     * le joueur à la fin de la partie y compris la récupération éventuelle de sa
     * mise
     *
     * @param dealer        Le croupier (pour connaître son score et s'il a un
     *                      Blackjack)
     * @param coefBlackjack Le coefficient Blackjack (le coefficient pour une
     *                      victoire sans Blackjack
     *                      est coefBlackjack - 0.5)
     * @return le gain du joueur
     *         Exemples :
     *         ---------------
     *         - this.bestScore() = 22 ---> retourne 0.0 (le joueur a dépassé 21
     *         points)
     *
     *         - this.bestScore() = 21, this.hasBlackjack = true, dealer.bestScore()
     *         = 20,
     *         this.bet = 10 et coefBlackjack = 3 ---> retourne 3 * 10 = 30
     *         (le joueur a gagné avec un Blackjack)
     *
     *         - this.bestScore() = 21, this.hasBlackjack = false,
     *         dealer.bestScore() = 20,
     *         this.bet = 5 et coefBlackjack = 2.5 ---> retourne (2.5 - 0.5) * 5 =
     *         10
     *         (le joueur a gagné sans Blackjack)
     */
    public double calculateGain(Dealer dealer, double coefBlackjack) {
            if (surrender) {
                return bet / 2.0;
            }

            if (bestScore() > 21) {
                return 0.0;
            }

            if (dealer.bestScore() > 21) {
                return hasBlackjack ? coefBlackjack * bet : (coefBlackjack - 0.5) * bet;
            }

            if (hasBlackjack && !dealer.hasBlackjack()) {
                return coefBlackjack * bet;
            }
            if (dealer.hasBlackjack() && !hasBlackjack) {
                return 0.0;
            }

            if (bestScore() > dealer.bestScore()) {
                return (coefBlackjack - 0.5) * bet;
            }

            if (bestScore() == dealer.bestScore()) {
                return bet;
            }

            return 0.0;
        }

    /**
     * Action : calcule le gain brut lié à l'assurance, r, c'est-à-dire ce que
     * reçoit le joueur
     * concernant l'assurance à la fin de la partie y compris la récupération
     * éventuelle
     * de sa prime d'assurance.
     *
     * @param dealerHasBlackjack true ssi le croupier a un Blackjack
     * @return le gain du joueur lié à l'assurance
     */
    public double calculateGainInsur(boolean dealerHasBlackjack) {
        return this.insurance && dealerHasBlackjack ? this.bet/2.0 : 0.0;
    }

    /**
     * Action : Calcule les gains finaux, met à jour le solde et affiche le bilan du
     * tour.
     * Élimine le joueur si son solde tombe à zéro.
     *
     * @param dealer        Le croupier
     * @param coefBlackjack Le coefficient Blackjack
     * @return le nouveau solde du joueur
     */
    public double processAndDisplayResult(Dealer dealer, double coefBlackjack) {
        double gain = this.calculateGain(dealer, coefBlackjack);
        double gainInsur = this.calculateGainInsur(dealer.hasBlackjack());
        this.ui.displayPlayerResult(gain, gainInsur, this.balance, this.bet, this.insurance);
        this.balance += gain + gainInsur;
        return this.balance;
    }

} // end class Player

import subprocess


def execution(listCmd):
    for cmd in listCmd:

        try:
            resultat = subprocess.run(cmd, shell=True, capture_output=True, text=True)
            print(resultat.stdout.strip())
        except Exception as e:
            print("Execption : ", e)


listCmd = [

    # mail du 29 / 10 à 20h10
    r'''find . -name Blackjack.java -exec bash -c "echo '0' | java {} | grep -q 'Réponse incorrecte \!' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 30 / 10 à 21h28
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< $'double[][] résultat = Blackjack.playGame(3, 1); 32 16.5 100 0 0 0' | grep -q 'Error' && echo {} ÉCHEC || echo {} OK" \; 2>/dev/null | sort''',

    # mail du 31 / 10 à 11h07
    r'''find . -name Blackjack.java -exec bash -c "echo '2 3 0.99' | java {} | grep -q 'Réponse incorrecte' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 01 / 11 à 22h44
    r'''find . -name Blackjack.java -exec bash -c "javac {} -d . && javap Blackjack.class | grep -q 'boolean playRound(boolean\[\], double\[\], int\[\]);' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 02 / 11 à 13h40
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< 'Blackjack.playersRemain(new boolean[]{false,true,false})' | grep -q '==> true$' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 05 / 11 à 12h21
    r'''find . -name Blackjack.java -exec bash -c "echo -e 'boolean[] playerIsActive = new boolean[]{true, true, true}; double[] playerMoney = {16, 32, 64}; double[] playerBet = new double[3]; Blackjack.collectBets(playerIsActive, playerMoney, playerBet);\n 4 8 16\n Arrays.equals(playerMoney, new double[]{12, 24, 48});' | jshell {} | grep -q '==> true' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 05 / 11 à 21h00
    r'''find . -name Blackjack.java -exec bash -c "echo '2 3 512.5 256.8' | java {} | grep -q 'PREMIÈRE PARTIE' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 06 / 11 à 19h13
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< 'int[] T = new int[16]; T[0] = 9; Blackjack.cardsNumber(T) == 9' | grep -q '==> true' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 05/11 à 13h41
    r'''find . -name Blackjack.java -exec bash -c "grep -H 'import .*;' {} | grep -q -v -e Scanner -e Random -e Locale -e PrintStream && echo {} ÉCHEC" \; | sort''',

    # mail du 07 / 11 à 22h02
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< 'int[] T = Blackjack.generateCards(1); var compteur = 0; for (var i = 0; i < T.length; i++) {if (T[i] == 7) compteur++;} T[0] == 52 && compteur == 4 && T.length == 53;' | grep -q '==> true' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 09 / 11 à 14h27
    r'''find . -name Blackjack.java -exec bash -c "timeout 15 jshell {} <<< 'int[] deck = new int[]{6,1,2,3,4,5,6}; int[] dHand = new int[]{0,0,0}; Blackjack.dealInitialCards(new boolean[]{true,true}, new int[][]{{0, 0, 0}, {0, 0, 0}}, dHand, deck); Blackjack.cardsNumber(deck) == 0 && Blackjack.cardsNumber(dHand) == 2;' | grep -q '==> true' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 10/11 à 22h14
    r'''find . -name Blackjack.java -exec bash -c "checkstyle -c blackjack-style.xml {} && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | grep -e OK -e ÉCHEC | sort''',

    # mail du 11/11 à 19h11
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< 'Blackjack.cardName(11)' | grep -q 'valet' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 12/11 à 10h21
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< 'Blackjack.playerNewMoney(100,20,18,18)' | grep -q '==> 120' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 12/11 à 20h51
    r'''find . -name Blackjack.java -exec bash -c "echo '2 3 100.9 205.5 50.4 128' | java {} | grep -q 'Faites vos jeux \!' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 13/11 à 10h17
    r'''find . -name Blackjack.java -exec bash -c "echo -e 'Blackjack.playRound(new boolean[]{true}, new double[]{100}, new int[]{5,7,7,7,7,7});\n 50 non' | jshell {} | grep -c -e 'Tu as 14 points.' -e 'Le croupier a les cartes 7 et 7.' -e 'Le croupier a 21 points.' -e 'Tu perds contre le croupier, tu ne récupères rien.' -e 'Ton solde est de 50.0 €' -e '==> true' | grep -q 7 && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 13/11 à 19h42
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< 'Blackjack.playDrawingPhase(new int[]{2,1,3,0,0}, 4, true, 14, false, new int[]{3,2,2,2})' | grep -c -e 'Le croupier a tiré un 2. Il a 16 points.' -e 'Le croupier a tiré un 2. Il a 18 points.' -e '==> 18' | grep -q '^3$' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 14 / 11 à 11h22
    r'''find . -name Blackjack.java -exec bash -c "javac {} -d . && javap Blackjack.class | grep -qF 'public static void shuffleCards(int[]);' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 14 / 11 à 12h59
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< 'Blackjack.displayGameInit(new boolean[]{true,true}, new double[]{63.5,156}, new double[]{64.5, 100}, new int[][]{{2,11,4},{2,2,13}}, 5)' | grep -e 'Joueur 1 : solde = 63.5 € / mise = 64.5 € / cartes : valet et 4' -e 'Joueur 2 : solde = 156.0 € / mise = 100.0 € / cartes : 2 et roi' -e 'Le croupier a les cartes 5 et ? .' | wc -l | grep -q 3 && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 14 / 11 à 15h19
    r'''find . -name Blackjack.java -exec bash -c "echo -e 'Blackjack.playerPlayTurn(0, 150, 150, new int[]{2,1,7,0,0}, new int[]{4,8,1,1,8}); \n oui oui non' | jshell {} | grep -e 'solde = 150.0 € / mise = 150.0 € / cartes : as et 7' -e 'Tu as 8 ou 18 points.' -e 'Tu as tiré un 8. Tu as 16 points.' -e 'Tu as tiré un as. Tu as 17 points.' -e '==> 17' | wc -l | grep -q 5 && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 16 / 11 à 14h15
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< 'int i = 0; double pMoney = 500.6; double pBet = 33.5 ; int[] pHand = new int[]{2,1,10}; int[] deck = new int[]{0}; Blackjack.playerPlayTurn(i, pMoney, pBet, pHand, deck)' | grep -e '--> Tour du joueur 1' -e 'solde = 500.6 € / mise = 33.5 € / cartes : as et 10' -e 'Black Jack \!' | wc -l | grep -q 3 && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 16 / 11 à 21h28
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< $'double[][] résultat = Blackjack.playGame(3, 1); \n 32 16.5 100 0 0 0 \n Arrays.deepEquals(résultat, new double[][]{{32, 16.5, 100}, {32, 16.5, 100}});' | grep -q '==> true' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 17 / 11 à 10h09
    r'''find . -name Blackjack.java -exec bash -c "timeout 4 jshell {} <<< 'int[] deck = new int[]{6,1,2,3,4,5,6}; int[] dHand = new int[]{0,0,0}; Blackjack.dealInitialCards(new boolean[]{true,true}, new int[][]{{0, 0, 0}, {0, 0, 0}}, dHand, deck); Blackjack.cardsNumber(deck) == 0 && Blackjack.cardsNumber(dHand) == 2;' | grep -q '==> true' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null''',

    # mail du 17 / 11 à 18h28
    r'''find . -name Blackjack.java -exec bash -c "echo -e 'int i = 0; double pMoney = 2500; double pBet = 2500 ; int[] pHand = new int[]{2,9,3,0,0,0}; int[] deck = new int[]{6,1,5,12,12,5,1}; Blackjack.playerPlayTurn(i, pMoney, pBet, pHand, deck);\n oui oui oui' | jshell {} | grep -q 'Tu as dépassé 21 points \!' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 18 / 11 à 22h49
    r'''find . -name Blackjack.java -exec bash -c "echo -e 'boolean[] playerIsActive = new boolean[]{true, true, true}; double[] playerMoney = {16, 32, 64}; double[] playerBet = new double[3]; Blackjack.collectBets(playerIsActive, playerMoney, playerBet);\n 4 8 16\n Arrays.equals(playerMoney, new double[]{12, 24, 48});' | jshell {} | grep -q '==> true' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort | grep OK''',

    # mail du 19 / 11 à 20h46
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< 'int[] dealerMain = {2,4,5,0,0,0}; int[] desk = {5,4,6,7,6,4}; Blackjack.dealerPlayTurn(dealerMain, desk);' | grep -q '==> 19' && echo {} OK || echo {} ÉCHEC " \; 2>/dev/null | sort''',

    # mil du 20 / 11 à 16h06
    r'''find . -name Blackjack.java -exec bash -c "grep -H 'import .*;' {} | grep -q -v -e Scanner -e Random -e Locale -e PrintStream && echo {} ÉCHEC" \; | sort''',

    # mail du 20 / 11 à 20h36
    r'''find . -name Blackjack.java -exec bash -c "echo -e 'boolean playerIsActive[] = new boolean[]{true}; double[] money = new double[]{100}; double[] bet = new double[]{50}; int[][] playerHand = new int[][]{{2, 1, 2, 0}}; int[] dealerHand = new int[]{2, 1, 2, 0, 0, 0}; int[] playerScore = new int[]{10}; int[] deck = new int[]{5, 3, 2, 1, 2, 3}; Blackjack.playTurn(playerIsActive, money, bet, playerHand, dealerHand, playerScore, deck); \n oui non \n var pHand = playerHand[0]; dealerHand;' | jshell {} | grep -F -e 'pHand ==> int[4] { 3, 1, 2, 3 }' -e 'dealerHand ==> int[6] { 5, 1, 2, 2, 1, 2 }' | wc -l | grep -q 2 && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 21 / 11 à 15h55
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< 'Blackjack.cardName(1).equals(\"as\") && Blackjack.cardName(2).equals(\"2\") && Blackjack.cardName(12).equals(\"dame\")' | grep -q '==> true' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 21 / 11 à 16h20
    r'''find . -name Blackjack.java -exec bash -c "jshell {} <<< 'Blackjack.playerNewMoney(50,10.5,22,20)' | grep -q '==> 81.5' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 22 / 11 à 15h15
    r'''find . -name Blackjack.java -exec bash -c "echo -e 'Blackjack.playRound(new boolean[]{true}, new double[]{100.5}, new int[]{6,6,6,6,6,6,6});\n 50.6 oui non \n' | timeout 2 jshell {} | grep -c -e 'Tu es à égalité avec le croupier, tu récupères ta mise, soit 50.6 €' -e 'Ton solde est de 100.5 €' | grep -q 2 && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 22 / 11 à 15h57
    r'''find . -name Blackjack.java -exec bash -c "echo '2 3 512.5 256.8' | timeout 15 java {} | grep -q 'PREMIÈRE PARTIE' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | grep OK | sort''',

    # mail du 23 / 11 à 10h23
    r'''find . -name Blackjack.java -exec bash -c "echo '2 3 0.99' | timeout 15 java {} | grep -q 'Réponse incorrecte' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort | grep OK''',

    # mail du 23 / 11 à 15h58
    r'''find . -name Blackjack.java -exec bash -c "java ExtractMethodText.java {} 'boolean playRound' | grep -q collectBets && echo {} OK || echo {} ÉCHEC" \; | sort'''
]

if __name__ == "__main__":
    execution(listCmd)

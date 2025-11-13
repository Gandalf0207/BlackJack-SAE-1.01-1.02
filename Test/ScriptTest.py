import subprocess

def execution(listCmd):
     
    for cmd in listCmd:

        try : 
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
]


if __name__ == "__main__":
    execution(listCmd)
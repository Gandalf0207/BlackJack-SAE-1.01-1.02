import subprocess


def execution(listCmd):
    for cmd in listCmd:

        try:
            resultat = subprocess.run(cmd, shell=True, capture_output=True, text=True)
            print(resultat.stdout.strip())
        except Exception as e:
            print("Execption : ", e)


listCmd = [

    # mail du 25 /11 à 11h11
    r'''find . -name ExtendedBlackjack.java -exec bash -c "javac {} -d . && javap ExtendedBlackjack.class | grep -qF 'boolean chooseDoubleBet(int, double[], double[], int[]);' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 25 / 11 à 11h24
    r'''find . -name ExtendedBlackjack.java -exec bash -c "javac {} -d . && javap ExtendedBlackjack.class | grep -qF 'boolean chooseInsurance(int, double[], double, int[]);' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 25 / 11 à 11h34
    r'''find . -name ExtendedBlackjack.java -exec bash -c "javac {} -d . && javap ExtendedBlackjack.class | grep -qF 'boolean chooseSurrender(double);' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 26 / 11 à 9h05
    r'''find . -name ExtendedBlackjack.java -exec bash -c " echo 'ExtendedBlackjack.displayNewCardAndPoints(1,5,15,true,true)' | jshell {} | grep -q 'Tu as tiré un as. Tu as 15 points.' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 27 / 11 à 21h22
    r'''find . -name ExtendedBlackjack.java -exec bash -c "echo 'var pHand = new int[]{3, 1, 2, 1}; var boolPlayerInsurance = true; ExtendedBlackjack.displayPlayerGameState(56.25, 16.8, pHand, boolPlayerInsurance)' | jshell {} | grep -q 'solde = 56.25 € / mise = 16.8 € / assurance = 4.2 € / cartes : as, 2 et as' && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',

    # mail du 28 / 11 à 14h02
    r'''find . -name ExtendedBlackjack.java -exec bash -c "echo 'var indiceJoueur = 1; var solde = 150.75; var mise = 32; ExtendedBlackjack.displayPlayerResult(indiceJoueur, solde, mise, new int[]{3, 1, 2, 4}, 17, 22, 166.75, true)' | jshell {} | grep -e 'Résultat du joueur 2' -e 'solde = 150.75 € / mise = 32.0 € / assurance = 8.0 € / cartes : as, 2 et 4' -e 'Tu as 17 points.' -e 'Tu perds contre le croupier, tu ne récupères rien.' -e 'Tu t.es assuré contre un Black Jack du croupier.' | wc -l | grep -q 5 && echo {} OK || echo {} ÉCHEC" \; 2>/dev/null | sort''',


]

if __name__ == "__main__":
    execution(listCmd)

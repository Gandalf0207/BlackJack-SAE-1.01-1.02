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

]

if __name__ == "__main__":
    execution(listCmd)

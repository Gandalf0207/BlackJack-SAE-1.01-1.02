# BlackJack-SAE-1.01-1.02

### Résumé du projet – SAE 1.1 & 1.2 : Jeu de BlackJack en Java

Ce projet consiste à développer en Java un jeu de cartes inspiré du BlackJack, opposant un ou plusieurs joueurs à un croupier. Le programme simule une succession de parties où chaque joueur mise, reçoit des cartes, choisit de tirer ou non, puis gagne ou perd de l’argent selon son résultat face au croupier.

#

### Les différents Livrables

> **Le premier rendu** comprend deux versions du code :
- une version normale,
- une version avec extensions, qui ajoute des options supplémentaires pour les joueurs.

> **Le second rendu** met en application les notions de class Object POO vu en cours. ls est composé de 10 fichiers que nous avons du completer afin de remplir les conditons demandées.

Ainsi qu'une ananlyse : Analyse des résultats – IA Blackjack

#### 1. Méthodologie

Le nombre de simulations nécessaires a été déterminé à l’aide des fonctions
`checkSameProba` et `computeDealerScoreProba` (avec ε).

- ε utilisé : **0.003** (bon compromis précision / temps)
- ε = 0.001 jugé plus précis mais trop long à exécuter

L’objectif est d’obtenir des probabilités empiriques équivalentes entre matrices.


#### 2. Paramètres de simulation

- Mise : **1 €** par tour
- Solde initial : **10 000 €**
- Tours max par partie : **100 000**
- Nombre de parties : **10 ou 100** selon les tests


#### 3. Résultats – 10 simulations (précision faible)

###### Blackjack réel (coef = 2.5)

- Ratio moyen du solde final : **0.84**
- Meilleur ratio : **0.93**
- Pire ratio : **≈ 0.80**
- Taux de victoire moyen : **≈ 0.432**
- Exemple : solde final **8 268,5 €** après 100 000 tours

➡️ L’IA survit mais perd de l’argent à long terme.



###### Blackjack SAE (coef = 3.0)

- Ratio moyen du solde final : **2.00**
- Ratio min / max : **1.0 / 2.0**
- Taux de victoire moyen : **≈ 0.432**
- Solde doublé en moyenne en **≈ 44 435 tours**

➡️ L’IA devient clairement gagnante.



#### 4. Résultats – 100 simulations (précision correcte)

##### coef = 2.5

- Ratio moyen : **0.83**
- Meilleur ratio : **0.92**
- Pire ratio : **0.73**
- Taux de victoire : **≈ 0.432**

➡️ Confirmation du caractère perdant de la stratégie.



##### coef = 3.0

- Ratio moyen : **2.00**
- Ratio min / max : **1.0 / 2.0**
- Taux de victoire : **≈ 0.429**
- Solde doublé en moyenne en **44 770 tours**


#### 5. Recherche du coefficient minimal gagnant

##### Protocole
- `coefBlackjack` incrémenté de **0.001**
- Arrêt si :
  - ratio minimum ≥ 1.0
  - ratio moyen > 1.0
- 10 parties, 100 000 tours max

##### Résultats
- coef = **2.545** → ratio moyen **1.05**
- coef = **2.542** → ratio moyen **1.063**
- Ratio min : **1.0**, taux de victoire : **≈ 0.43**



##### 6. Conclusion

- À **2.5**, l’IA est perdante (jeu réel)
- À partir de **≈ 2.54**, l’IA devient rentable
- À **3.0** (valeur SAE), l’IA est gagnante de manière systématique

Ces résultats valident la cohérence probabiliste de l’IA et l’impact déterminant du coefficient de Blackjack.

#


### Résumé
Le jeu s’appuie sur plusieurs règles clés du BlackJack : gestion des mains, valeur des cartes, tirage automatisé du croupier, calcul des gains, ainsi que des fonctionnalités avancées comme la double mise, l’assurance ou l’abandon (selon la version du rendu).

Ce projet introduit les bases de la programmation en Java, la gestion de tableaux, la création d’un petit moteur de jeu, et l'organisation d’un code modulaire. Le deuxième rendu étendra ce travail vers une version orientée objet et intégrera une IA capable de décider ses actions.


#
### Implication :

- Quentin PLADEAU : 99.5 %
- Louis Ragout : 0.5%

#### Précisions :
- 3 petite méthodes dans le rendu 1 réalisées par Louis avec Quentin. Quentin a par la suite implémenté ces meme méthodes
- Intégralité restante du projet réalisé par Quetin

#

[Projet sur GitHub](https://github.com/Gandalf0207/BlackJack-SAE-1.01-1.02) <br>
[Projet sur GitLab](https://gitlabinfo.iutmontp.univ-montp2.fr/devinitiatique/etu/pladeauq-ragouatl/sae_blackjack)

#

*by Quentin*

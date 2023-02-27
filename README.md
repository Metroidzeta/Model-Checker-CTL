# Model-Checker-CTL
Projet entièrement réalisé par Metroidzeta.
Le Model Checker CTL que j'ai fait en M2 Informatique parcours Programmation et Logiciels Sûrs

Le but du projet était de réaliser un model checker CTL permettant 
de vérifier des formules, en les évaluant, pour déterminer si elles 
satisfassent le système (true ou false), et cela pour chaque état du 
système.

## POUR DEMARRER LE PROGRAMME :

Tout d’abord compiler : ```javac *.java```
Puis exécuter : ```java CTL_Main```

La structure du fichier XXX.txt chargée doit être comme celle ci-dessous :

ICI graph1.txt :
```
	states:
	1;
	2;
	3;
	4;
	5;
	6;
	7;
	8;

	transitions:
	1 -> 1;
	1 -> 2;
	2 -> 3;
	2 -> 5;
	2 -> 6;
	3 -> 6;
	4 -> 3;
	4 -> 4;
	5 -> 1;
	5 -> 5;
	6 -> 5;
	6 -> 7;
	7 -> 8;
	8 -> 4;

	labels:
	1 : "q";
	2 : "p,q";
	3 : "q";
	4 : "r";
	5 : "p,r";
	6 : "p,r";
	7 : "p,q";
```
On peut créer ses propres automates (fichiers) qui seront lu par 
le programme.

Attention, pour qu’un fichier soit valide, il faut qu’elle respecte cette 
structure, à savoir :

- Une ligne « states: » puis, à chaque ligne suivante, le nom de 
l’état suivi d’un point-virgule, comme ceci : « e1; »
- Une ligne « transitions: » puis, à chaque ligne suivante, le nom 
de l’état de départ, puis une flèche ->, suivi du nom de l’état 
d’arrivée, un point-virgule à la fin comme ceci : « e1 -> e2; »
- Une ligne « labels: » puis, le nom de l’état, suivi de deux points, 
suivi entre guillemets du nom des labels, séparés par des 
virgules si y en plusieurs, un point-virgule à la fin comme ceci : 
« 1 : "q"; » ou « 2 : "p,q"; »

## TAPER UNE FORMULE
Pour taper une formule c’est simple, il suffit de respecter la syntaxe :
```
p = propostion, nom d’une étiquette
- = Not (Non)
& = And (Et)
| = Or (Ou)
> = Implication
? = Equivalence
E = Il existe
A = Pour Tout
X = Next (suivant)
F = Future (Futur)
G = Globally (Global)
U = Until
```

La syntaxe reconnue :

```
p = proposition (ou étiquette), exemple : a, b, p, q, r
-f = non formule
(f1&f2) = AND (Attention : il faut obligatoirement mettre des 
parenthèses ‘( & )’ pour le ET, sinon la syntaxe ne sera pas reconnue, 
à savoir : (a&b), pour éviter toute confusion. Exemple : (EXp&r)
(f1|f2) = OR (Attention : la même chose que AND)
(f1>f2) = IMPLICATION (Attention : la même chose que AND)
(f1?f2) = EQUIVALENCE (Attention : la même chose que AND)
EXf = il existe Next formule
EFf = il existe futur formule
EGf = il existe global formule
Ef1Uf2 = il existe Until, ex: « ExUy » (pas besoin de parenthèses)
AXf = pour tout Next formule
AFf = pour tout futur formule
AGf = pour tout global formule
Af1Uf2 = pour tout Until, ex: « AxUy » (pas besoin de parenthèses)
```
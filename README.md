# Model-Checker-CTL
Projet entièrement réalisé par Metroidzeta.
Le Model Checker CTL est un projet d'études réalisé pour le cours "Systèmes Complexes" en M2 Informatique parcours Programmation et Logiciels Sûrs.

Le but du projet est de créé à partir de 0 un model checker CTL permettant 
de vérifier des formules, en les évaluant (true ou false), pour déterminer si elles 
satisfassent un système, et cela pour chaque état de l'automate (structure de Kripke).

Le parseur est fait maison.

## POUR DEMARRER LE PROGRAMME

Tout d’abord compiler : ```javac *.java```
Puis exécuter : ```java CTL_Main```

A ce stade vous devez entrer le nom du fichier « XXX.txt » (qui se trouve dans le dossier "automates") à utiliser 
(la structure de Kripke), avec l’automate correspondant. Par exemple, 
le fichier de base (par défaut) : « graph1.txt » qui correspond à la structure ci-dessous :
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

- Une ligne « labels: » puis, à chaque ligne suivante, le nom de l’état, 
suivi de deux points, suivi entre guillemets du nom des labels, séparés par des 
virgules si y en plusieurs, un point-virgule à la fin comme ceci : 
« 1 : "q"; » ou « 2 : "p,q"; »

Vérifier que les informations récupérées par le programme (états, 
transitions et étiquettes) sont bonnes, ensuite, on peut enfin écrire 
une formule.

## TAPER UNE FORMULE
Pour taper une formule c’est simple, il suffit de respecter la syntaxe :
```
p = propostion, nom d’une étiquette (ex: a,b,p,q,r,x)
- = Not (Non)
& = And (Et)
| = Or (Ou)
> = Implication
? = Equivalence
E = Il existe
A = Pour Tout
X = Next (suivant)
F = Future (dans le futur)
G = Globally (Global)
U = Until
```

La syntaxe reconnue (f = formule):

```
p = proposition (ou étiquette), ex : « a,b,p,q,r,x,y,z »
-f = NOT f, ex : « -a,-b,-p,-q,-r,-x,-y,-z »
(f1&f2) = f1 AND f2, ex: « (a&b) » (Attention: parenthèses extérieures obligatoires)
(f1|f2) = f1 OR f2, ex: « (a|b) » (Attention: parenthèses extérieures obligatoires)
(f1>f2) = f1 IMPLIQUE f2, ex: « (a>b) » (Attention: parenthèses extérieures obligatoires)
(f1?f2) = f1 EQUIVALENT f2, ex: « (a?b) » (Attention: parenthèses extérieures obligatoires)
EXf = il existe NEXT f, ex: « EXa »
EFf = il existe FUTUR f, ex: « EFa »
EGf = il existe GLOBAL f, ex: « EGa »
E(f1Uf2) = il existe f1 UNTIL f2, ex: « E(aUb) » (Attention : parenthèses après le E obligatoires)
AXf = pour tout NEXT f, ex: « AXa »
AFf = pour tout FUTUR f, ex: « AFa »
AGf = pour tout GLOBAL f, ex: « AGa »
A(f1Uf2) = pour tout f1 UNTIL f2, ex: « A(aUb) » (Attention : parenthèses après le A obligatoires)
```

On peut également voir les détails en tapant : « voirDetails »

« voirDetails » est pratique pour voir toutes les évaluations précédentes (les marquages récursifs intermédiaires inclus). 
/!\Attention -> les détails fournis concernent toutes les anciennes formules tapées depuis le début (il faut quitter le programme pour vider cela).
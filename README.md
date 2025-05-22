# Model-Checker-CTL
Projet entièrement réalisé par Alain Barbier (Metroidzeta).  
Le Model Checker [CTL](https://en.wikipedia.org/wiki/Computation_tree_logic) est un projet d'études réalisé dans le cadre du cours "Systèmes Complexes" en M2 Informatique parcours Programmation et Logiciels Sûrs.  

Le but du projet est de créer à partir de 0 un model checker CTL permettant de vérifier des formules, en les évaluant (true ou false), pour déterminer si elles satisfont le système, et cela pour chaque état de la [structure de Kripke](https://fr.wikipedia.org/wiki/Structure_de_Kripke).  

Le parseur est fait maison.  

## POUR DEMARRER LE PROGRAMME

Tout d’abord compiler : ```javac formules/unaires/*.java formules/binaires/*.java formules/*.java *.java```  
Puis exécuter : ```java CTLMain```  

A ce stade vous devez entrer le nom du fichier « XXX.txt » (qui se trouve dans le dossier "automates") à utiliser (la structure de Kripke), avec l’automate correspondant.  
Par exemple, le fichier de base (par défaut) : « graph1.txt » qui correspond à la structure ci-dessous :  
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
On peut créer ses propres structure de Kripke (fichiers) qui seront lu par le programme.  

Attention, pour qu’un fichier soit valide, il faut qu’elle respecte cette structure, à savoir :  

- Une ligne « states: » puis, à chaque ligne suivante, le nom de l’état suivi d’un point-virgule, comme ceci : « e1; »  

- Une ligne « transitions: » puis, à chaque ligne suivante, le nom de l’état de départ, puis une flèche ->, suivi du nom de l’état d’arrivée, un point-virgule à la fin comme ceci : « e1 -> e2; »  

- Une ligne « labels: » puis, à chaque ligne suivante, le nom de l’état, suivi de deux points, suivi entre guillemets du nom des labels, séparés par des virgules si y en plusieurs, un point-virgule à la fin comme ceci : « 1 : "q"; » ou « 2 : "p,q"; »  

Vérifier que les informations récupérées par le programme (états, transitions et labels) sont bonnes, ensuite on peut écrire des formules.  

## TAPER UNE FORMULE
Pour taper une formule c’est simple, il suffit de respecter la syntaxe :  
```
true = toujours vraie
p = propostion, nom d’un label (ex: a,b,p,q,r,x)
- = Not (Non)
& = And (Et)
| = Or (Ou)
> = Implies (Implication)
? = Equivalence
E = ∃ (Il existe)
A = ∀ (Pour tout)
X = Next (Suivant)
F = Future (Dans le futur)
G = Globally (Global)
U = Until
```

La syntaxe reconnue (f = formule):  

```
false = toujours faux
true = toujours vraie
p = proposition (ou label), ex : « a,b,p,q,r,x,y,z »
-f = NOT f, ex : « -a,-b,-p,-q,-r,-x,-y,-z »
(f1&f2) = f1 AND f2, ex: « (p&q) » (Attention: parenthèses extérieures obligatoires)
(f1|f2) = f1 OR f2, ex: « (p|q) » (Attention: parenthèses extérieures obligatoires)
(f1>f2) = f1 IMPLIES f2, ex: « (p>q) » (Attention: parenthèses extérieures obligatoires)
(f1?f2) = f1 EQUIVALENT f2, ex: « (p?q) » (Attention: parenthèses extérieures obligatoires)
EXf = ∃ NEXT f, ex: « EXp »
EFf = ∃ FUTUR f, ex: « EFp »
EGf = ∃ GLOBAL f, ex: « EGp »
E(f1Uf2) = ∃ f1 UNTIL f2, ex: « E(pUq) » (Attention : parenthèses après le E obligatoires)
AXf = ∀ NEXT f, ex: « AXp »
AFf = ∀ FUTUR f, ex: « AFp »
AGf = ∀ GLOBAL f, ex: « AGp »
A(f1Uf2) = ∀ f1 UNTIL f2, ex: « A(pUq) » (Attention : parenthèses après le A obligatoires)
```
On peut également voir les détails en tapant : « voirDetails »  

« voirDetails » est pratique pour voir toutes les évaluations précédentes (les marquages récursifs intermédiaires inclus).  
Les détails concernent toutes les anciennes formules tapées depuis le début (il faut quitter le programme pour vider cela).  
# Model-Checker-CTL
Projet entièrement réalisé par Metroidzeta.
Le Model Checker CTL que j'ai fait en M2 Informatique parcours Programmation et Logiciels Sûrs

Le but du projet était de réaliser un model checker CTL permettant 
de vérifier des formules, en les évaluant, pour déterminer si elles 
satisfassent le système (true ou false), et cela pour chaque état du 
système.

##POUR DEMARRER LE PROGRAMME :

Tout d’abord compiler : ````javac *.java```
Puis exécuter : ```java CTL_Main```

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
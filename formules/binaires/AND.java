/*
 * @author Alain Barbier alias "Metroidzeta"
 *
 * Pour compiler avec Windows, GNU/Linux et MacOS :
 *     > javac formules/unaires/*.java formules/binaires/*.java formules/*.java *.java
 *
 * Pour exécuter :
 *     > java CTLMain
 */

package formules.binaires;

import formules.Formule;

public class AND extends FormuleBinaire {

	public AND(Formule f1, Formule f2) { super(f1, f2); }

	/*** Autres méthodes ***/
	@Override
	protected char getSymboleMilieu() { return '&'; }
}
/*
 * @author Alain Barbier alias "Metroidzeta"
 *
 * Pour compiler avec Windows, GNU/Linux et MacOS :
 *     > javac formules/unaires/*.java formules/binaires/*.java formules/*.java *.java
 *
 * Pour exécuter :
 *     > java CTLMain
 *
 */

package formules.binaires;

import formules.Formule;

public class EQUIV extends FormuleBinaire {

	public EQUIV(Formule f1, Formule f2) { super(f1, f2); }

	/*** Autres méthodes ***/
	@Override
	protected String getSymboleDebut() { return ""; }

	@Override
	protected String getSymboleMilieu() { return "?"; }
}
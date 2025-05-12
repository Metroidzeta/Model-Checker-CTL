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

public class EU extends FormuleBinaire {

	public EU(Formule f1, Formule f2) { super(f1, f2); }

	/*** Autres méthodes ***/
	@Override
	protected String getSymboleDebut() { return "E"; }

	@Override
	protected String getSymboleMilieu() { return "U"; }
}
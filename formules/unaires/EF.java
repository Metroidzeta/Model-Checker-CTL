/*
 * @author Alain Barbier alias "Metroidzeta"
 *
 * Pour compiler avec Windows, GNU/Linux et MacOS :
 *     > javac formules/unaires/*.java formules/binaires/*.java formules/*.java *.java
 *
 * Pour exécuter :
 *     > java CTLMain
 */

package formules.unaires;

import formules.Formule;

public class EF extends FormuleUnaire {

	public EF(Formule f) { super(f); }

	/*** Autres méthodes ***/
	@Override
	protected String getSymboleDebut() { return "EF"; }
}
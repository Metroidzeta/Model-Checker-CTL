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
import java.util.Objects;

public abstract class FormuleBinaire extends Formule {

	private Formule gauche, droite;

	public FormuleBinaire(Formule f1, Formule f2) {
		gauche = f1;
		droite = f2;
	}

	/*** Getters ***/
	public Formule getGauche() { return gauche; }
	public Formule getDroite() { return droite; }

	/*** Autres méthodes ***/
	protected char getSymboleDebut() { return '\0'; } // vide par défaut
	protected abstract char getSymboleMilieu();

	@Override
	public String toString() {
		return getSymboleDebut() + "(" + gauche + ")" + getSymboleMilieu() + "(" + droite + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FormuleBinaire fb = (FormuleBinaire) o;
		return gauche.equals(fb.gauche) && droite.equals(fb.droite);
	}

	@Override
	public int hashCode() { return Objects.hash(getClass(), gauche, droite); }
}
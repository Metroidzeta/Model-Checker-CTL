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

package formules.unaires;

import formules.Formule;
import java.util.Objects;

public abstract class FormuleUnaire extends Formule {

	protected final Formule droite;

	public FormuleUnaire(Formule f) { droite = f; }

	/*** Getters ***/
	public Formule getDroite() { return droite; }

	/*** Autres méthodes ***/
	protected abstract String getSymboleDebut();

	@Override
	public String toString() {
		return getSymboleDebut() + "(" + droite + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FormuleUnaire fu)) return false;

		return droite.equals(fu.droite);
	}

	@Override
	public int hashCode() { return Objects.hash(getClass(), droite); }
}
/*
 * @author Alain Barbier alias "Metroidzeta"
 *
 * Pour compiler avec Windows, GNU/Linux et MacOS :
 *     > javac formules/unaires/*.java formules/binaires/*.java formules/*.java *.java
 *
 * Pour exécuter :
 *     > java CTLMain
 */

package formules;

import java.util.Objects;

public class PROPOSITION extends Formule {

	private final String nom;

	public PROPOSITION(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() { return nom; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PROPOSITION proposition)) return false;

		return nom.equals(proposition.nom);
	}

	@Override
	public int hashCode() { return Objects.hash(getClass(), nom); }
}
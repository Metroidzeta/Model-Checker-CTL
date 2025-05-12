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

package formules;

import java.util.Objects;

public class PROP extends Formule {

	private final String nom;

	public PROP(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() { return nom; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PROP prop)) return false;

		return nom.equals(prop.nom);
	}

	@Override
	public int hashCode() { return Objects.hash(getClass(), nom); }
}
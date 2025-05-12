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

public class TRUE extends Formule {

	@Override
	public String toString() { return "true"; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TRUE tr)) return false;

		return true;
	}

	@Override
	public int hashCode() { return 1; }
}
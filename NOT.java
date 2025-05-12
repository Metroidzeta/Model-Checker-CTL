// Projet réalisé par Metroidzeta

import java.util.Objects;

public class NOT extends Unaire {

	NOT(Formule f) { super(f); }

	@Override
	public String toString() {
		return "-(" + droite.toString() + ")";
	}
}
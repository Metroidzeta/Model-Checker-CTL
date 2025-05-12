// Projet réalisé par Metroidzeta

public class AX extends Unaire {

	AX(Formule f) { super(f); }

	@Override
	public String toString() {
		return "AX(" + droite.toString() + ")";
	}
}
// Projet réalisé par Metroidzeta

public class EX extends Unaire {

	EX(Formule f) { super(f); }

	@Override
	public String toString() {
		return "EX(" + droite.toString() + ")";
	}
}
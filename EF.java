// Projet réalisé par Metroidzeta

public class EF extends Unaire {

	EF(Formule f) { super(f); }

	@Override
	public String toString() {
		return "EF(" + droite.toString() + ")";
	}
}
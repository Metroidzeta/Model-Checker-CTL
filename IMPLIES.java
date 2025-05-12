// Projet réalisé par Metroidzeta

public class IMPLIES extends Binaire {

	public IMPLIES(Formule f1, Formule f2) { super(f1, f2); }

	@Override
	public String toString() {
		return "(" + gauche + ")>(" + droite + ")";
	}
}
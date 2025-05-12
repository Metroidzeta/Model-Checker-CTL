// Projet réalisé par Metroidzeta

public class OR extends Binaire {

	public OR(Formule f1, Formule f2) { super(f1, f2); }

	@Override
	public String toString() {
		return "(" + gauche + ")|(" + droite + ")";
	}
}
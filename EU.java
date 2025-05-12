// Projet réalisé par Metroidzeta

public class EU extends Binaire {

	public EU(Formule f1, Formule f2) { super(f1, f2); }

	@Override
	public String toString() {
		return "E(" + gauche + ")U(" + droite + ")";
	}
}
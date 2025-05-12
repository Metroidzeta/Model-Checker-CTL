// Projet réalisé par Metroidzeta

public class AU extends Binaire {

	public AU(Formule f1, Formule f2) { super(f1, f2); }

	@Override
	public String toString() {
		return "A(" + gauche + ")U(" + droite + ")";
	}
}
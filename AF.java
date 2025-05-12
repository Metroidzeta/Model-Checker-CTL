// Projet réalisé par Metroidzeta

public class AF extends Unaire {

	AF(Formule f) { super(f); }

	@Override
	public String toString() {
		return "AF(" + droite.toString() + ")";
	}
}
// Projet réalisé par Metroidzeta

public class AG extends Unaire {

	AG(Formule f) { super(f); }

	@Override
	public String toString() {
		return "AG(" + droite.toString() + ")";
	}
}
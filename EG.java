// Projet réalisé par Metroidzeta

public class EG extends Unaire {

	EG(Formule f) { super(f); }

	@Override
	public String toString() {
		return "EG(" + droite.toString() + ")";
	}
}
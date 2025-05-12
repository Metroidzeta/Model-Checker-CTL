// Projet réalisé par Metroidzeta

public class EQUIV extends Binaire {

	public EQUIV(Formule f1, Formule f2) { super(f1, f2); }

	@Override
	public String toString() {
		return "(" + gauche + ")?(" + droite + ")";
	}
}
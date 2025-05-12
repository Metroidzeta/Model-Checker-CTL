// Projet réalisé par Metroidzeta

public class TRUE extends Formule {

	@Override
	public String toString() { return "true"; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TRUE tr)) return false;

		return true;
	}

	@Override
	public int hashCode() { return 1; }
}
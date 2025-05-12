// Projet réalisé par Metroidzeta

public class FALSE extends Formule {

	@Override
	public String toString() { return "false"; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FALSE fa)) return false;

		return true;
	}

	@Override
	public int hashCode() { return 0; }
}
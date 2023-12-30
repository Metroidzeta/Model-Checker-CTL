// Projet réalisé par Metroidzeta

public class TRUE extends Formule {

	@Override
	public String toString() { return "true"; }

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;

		return true;
	}

	@Override
	public int hashCode() { return 1; }
}
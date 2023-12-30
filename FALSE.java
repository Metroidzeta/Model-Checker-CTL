// Projet réalisé par Metroidzeta

public class FALSE extends Formule {

	@Override
	public String toString() { return "false"; }

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;

		return true;
	}

	@Override
	public int hashCode() { return 0; }
}
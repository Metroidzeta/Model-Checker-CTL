// Projet réalisé par Metroidzeta

import java.util.Objects;

public class PROP extends Formule {

	private final String nom;

	public PROP(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() { return nom; }

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;

		PROP prop = (PROP) obj;

		return this.nom.equals(prop.nom);
	}

	@Override
	public int hashCode() { return Objects.hash(nom); }
}
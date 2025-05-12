// Projet réalisé par Metroidzeta

import java.util.Objects;

public abstract class Unaire extends Formule {

	protected final Formule droite;

	public Unaire(Formule f) { droite = f; }

	/*** Getters ***/
	public Formule getDroite() { return droite; }

	/*** Autres méthodes ***/
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Unaire unaire)) return false;

		return droite.equals(unaire.droite);
	}

	@Override
	public int hashCode() { return Objects.hash(getClass(), droite); }
}
// Projet réalisé par Metroidzeta

import java.util.Objects;

public abstract class Binaire extends Formule {

	protected Formule gauche, droite;

	public Binaire(Formule f1, Formule f2) {
		gauche = f1;
		droite = f2;
	}

	/*** Getters ***/
	public Formule getGauche() { return gauche; }
	public Formule getDroite() { return droite; }

	/*** Autres méthodes ***/
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Binaire binaire)) return false;

		return gauche.equals(binaire.gauche) && droite.equals(binaire.droite);
	}

	@Override
	public int hashCode() { return Objects.hash(getClass(), gauche, droite); }
}
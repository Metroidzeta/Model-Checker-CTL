// Projet réalisé par Metroidzeta

import java.util.Objects;

public class FormlTwoArg extends Formule {

	private final TwoArg type;
	private final Formule gauche;
	private final Formule droite;

	public FormlTwoArg(TwoArg type, Formule gauche, Formule droite) {
		this.type = type;
		this.gauche = gauche;
		this.droite = droite;
	}

	public String getSymboleDebut() {
		switch(type) {
			case EU: return "E"; // Il existe
			case AU: return "A"; // Pour tout
			default: return "";
		}
	}

	public String getSymboleMilieu() {
		switch(type) {
			case AND: return "&"; // Et
			case OR: return "|"; // Ou
			case EU:
			case AU: return "U"; // Until
			case IMPLIES: return ">"; // Implication
			case EQUIV: return "?"; // Equivalence
			default: return "";
		}
	}

	public TwoArg getType() { return type; }
	public Formule getGauche() { return gauche; }
	public Formule getDroite() { return droite; }

	@Override
	public String toString() { return getSymboleDebut() + "(" + gauche.toString() + ")" + getSymboleMilieu() + "(" + droite.toString() + ")"; }

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || !(obj instanceof FormlTwoArg)) return false;

		FormlTwoArg fta = (FormlTwoArg) obj;

		return this.type.equals(fta.type)
			&& this.gauche.equals(fta.gauche)
			&& this.droite.equals(fta.droite);
	}

	@Override
	public int hashCode() { return Objects.hash(type,gauche,droite); }
}
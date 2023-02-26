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

	public String getSymboleMilieu() {
		if(type.equals(TwoArg.AND)) { return "&"; }
		else if(type.equals(TwoArg.OR)) { return "|"; }
		else if(type.equals(TwoArg.EU) || type.equals(TwoArg.AU)) { return "U"; }
		else if(type.equals(TwoArg.IMPLIES)) { return ">"; }
		else if(type.equals(TwoArg.EQUIV)) { return "?"; }
		else { return ""; }
	}

	public String getSymboleDebut() {
		if(type.equals(TwoArg.EU)) { return "E"; }
		else if(type.equals(TwoArg.AU)) { return "A"; }
		else { return ""; }
	}

	public TwoArg getType() { return type; }
	public Formule getGauche() { return gauche; }
	public Formule getDroite() { return droite; }

	@Override
	public String toString() { return getSymboleDebut() + "(" + gauche.toString() + ")" + getSymboleMilieu() + "(" + droite.toString() + ")"; }

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if(obj == null || !(obj instanceof FormlTwoArg)) { return false; }

		FormlTwoArg fta = (FormlTwoArg) obj;

		if(!(this.type.equals(fta.type))) { return false; }
		if(!(this.gauche.equals(fta.gauche)) || !(this.droite.equals(fta.droite))) { return false; }

		return true;
	}

	@Override
	public int hashCode() { return Objects.hash(type,gauche,droite); }
}
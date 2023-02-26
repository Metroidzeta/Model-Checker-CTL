// Projet réalisé par Metroidzeta

import java.util.Objects;

public class FormlOneArg extends Formule {

	private final OneArg type;
	private final Formule finFormule;

	public FormlOneArg(OneArg type,Formule finFormule) {
		this.type = type;
		this.finFormule = finFormule;
	}

	public OneArg getType() { return type; }
	public Formule getFinFormule() { return finFormule; }
	public String getSymboleDebut() {
		if(type.equals(OneArg.NOT)) { return "-"; }
		else { return type.toString(); }
	}

	@Override
	public String toString() { return getSymboleDebut() + "(" + finFormule.toString() + ")"; }

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if(obj == null || !(obj instanceof FormlOneArg)) { return false; }

		FormlOneArg foa = (FormlOneArg) obj;

		if(!(this.type.equals(foa.type))) { return false; }
		if(!(this.finFormule.equals(foa.finFormule))) { return false; }

		return true;
	}

	@Override
	public int hashCode() { return Objects.hash(type,finFormule); }
}
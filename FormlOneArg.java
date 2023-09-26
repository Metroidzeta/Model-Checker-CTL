// Projet réalisé par Metroidzeta

import java.util.Objects;

public class FormlOneArg extends Formule {

	private final OneArg type;
	private final Formule finFormule;

	public FormlOneArg(OneArg type,Formule finFormule) {
		this.type = type;
		this.finFormule = finFormule;
	}

	public String getSymboleDebut() {
		switch(type) {
			case NOT: return "-";
			default: return type.toString();
		}
	}

	public OneArg getType() { return type; }
	public Formule getFinFormule() { return finFormule; }

	@Override
	public String toString() { return getSymboleDebut() + "(" + finFormule.toString() + ")"; }

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || !(obj instanceof FormlOneArg)) return false;

		FormlOneArg foa = (FormlOneArg) obj;

		return this.type.equals(foa.type)
			&& this.finFormule.equals(foa.finFormule);
	}

	@Override
	public int hashCode() { return Objects.hash(type,finFormule); }
}
// Projet réalisé par Metroidzeta

import java.util.Arrays;
import java.util.Objects;

public class FormuleXArgs extends Formule {

	private final FType type;
	private final Formule[] formules;

	public FormuleXArgs(FType type, Formule finFormule) { // 1 argument
		this.type = type;
		this.formules = new Formule[1];
		this.formules[0] = finFormule;
	}

	public FormuleXArgs(FType type, Formule gauche, Formule droite) { // 2 arguments
		this.type = type;
		this.formules = new Formule[2];
		this.formules[0] = gauche;
		this.formules[1] = droite;
	}

	public String getSymboleDebut() {
		switch(type) {
			case NOT: return "-";
			case EX: case EF: case EG: case AX: case AF: case AG: return type.toString();
			case EU: return "E";
			case AU: return "A";
			default: return "";
		}
	}

	public String getSymboleMilieu() { // Si y a 2 arguments (gauche et droite)
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

	public FType getType() { return type; }
	public Formule[] getFormules() { return formules; }
	public int getTaille() { return formules.length; }

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(getSymboleDebut() + "(" + formules[0].toString() + ")");
		if(formules.length == 2) {
			 result.append(getSymboleMilieu() + "(" + formules[1].toString() + ")");
		}
		return result.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || !(obj instanceof FormuleXArgs)) return false;

		FormuleXArgs fxa = (FormuleXArgs) obj;

		return this.type.equals(fxa.type)
			&& Arrays.equals(this.formules,fxa.formules);
	}

	@Override
	public int hashCode() { return 31 * Objects.hash(type) + Arrays.hashCode(formules); }
}
// Projet réalisé par Metroidzeta

import java.util.Arrays;
import java.util.Objects;

public class FormuleXArgs extends Formule {

	private final FType type;
	private final Formule[] formules;

	public FormuleXArgs(FType type, Formule droite) { // 1 argument
		if(type == FType.AND || type == FType.OR || type == FType.EU || type == FType.AU || type == FType.IMPLIES || type == FType.EQUIV) {
			throw new IllegalArgumentException("Le type pour 1 argument ne peut pas être AND, OR, EU, AU, IMPLIES ou EQUIV");
		}
		this.type = type;
		this.formules = new Formule[]{droite};
	}

	public FormuleXArgs(FType type, Formule gauche, Formule droite) { // 2 arguments
		if(type == FType.NOT || type == FType.EX || type == FType.EF || type == FType.EG || type == FType.AX || type == FType.AF || type == FType.AG) {
			throw new IllegalArgumentException("Le type pour 2 arguments ne peut pas être NOT, EX, EF, EG, AX, AF ou AG");
		}
		this.type = type;
		this.formules = new Formule[]{gauche, droite};
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
			case EU: case AU: return "U"; // Until
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
		if(obj == null || getClass() != obj.getClass()) return false;

		FormuleXArgs fxa = (FormuleXArgs) obj;

		return this.type == fxa.type
			&& Arrays.equals(this.formules,fxa.formules);
	}

	@Override
	public int hashCode() { return 31 * Objects.hash(type) + Arrays.hashCode(formules); }
}
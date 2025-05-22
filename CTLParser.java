/*
 * @author Alain Barbier alias "Metroidzeta"
 *
 * Pour compiler avec Windows, GNU/Linux et MacOS :
 *     > javac formules/unaires/*.java formules/binaires/*.java formules/*.java *.java
 *
 * Pour exécuter :
 *     > java CTLMain
 */

import formules.*;
import formules.unaires.*;
import formules.binaires.*;

import java.util.Set;

public class CTLParser {

	private static final char[] OPERATEURS_BINAIRES = {'&', '|', '>', '?'};
	private static final char[] OPERATEUR_UNTIL = {'U'};

	private final Set<String> labelsReconnus;

	public CTLParser(KripkeStructure kripke) {
		labelsReconnus = kripke.getEnsembleLabels();
	}

	public Formule parse(String str) { // transforme un string en une formule CTL (null = mauvaise syntaxe)
		if (str == null || str.isBlank()) return null;
		if (str.equalsIgnoreCase("false")) return new FALSE(); // "false/FALSE"
		if (str.equalsIgnoreCase("true")) return new TRUE(); // "true/TRUE"
		if (labelsReconnus.contains(str)) return new PROPOSITION(str); // proposition

		if (str.charAt(0) == '-') return parseNOT(str); // NOT
		if (str.length() < 3) return null;

		if (estUnaireAE(str)) return parseUnaireAE(str); // str = A/E:X/F/G..
		if (estBinaireAE(str)) return parseBinaireAE(str); // str = A/E(..U..)
		if (estParenthesesBinaireOp(str)) return parseParenthesesBinaireOp(str); // str = (..S..), S € '&,|,>,?'

		return null;
	}

	private Formule parseNOT(String str) {
		Formule droite = parse(str.substring(1)); // parse(strDroite)
		return droite == null ? null : new NOT(droite); // null ou NOT(droite)
	}

	private Formule parseUnaireAE(String str) {
		char first = str.charAt(0), second = str.charAt(1);
		Formule droite = parse(str.substring(2)); // parse(strDroite)
		if (droite == null) return null;

		String op = "" + first + second;
		return switch (op) {
			case "AX" -> new AX(droite);
			case "AF" -> new AF(droite);
			case "AG" -> new AG(droite);
			case "EX" -> new EX(droite);
			case "EF" -> new EF(droite);
			case "EG" -> new EG(droite);
			default -> null;
		};
	}

	private Formule parseBinaireAE(String str) {
		int indexU = trouverIndex(str, OPERATEUR_UNTIL);
		if (indexU <= 2 || indexU >= str.length() - 2) return null; // U mal placé

		Formule gauche = parse(str.substring(2, indexU)); // parse(strGauche)
		Formule droite = parse(str.substring(indexU + 1, str.length() - 1)); // parse(strDroite)
		if (gauche == null || droite == null) return null;

		return str.startsWith("A") ? new AU(gauche, droite) : new EU(gauche, droite);
	}

	private Formule parseParenthesesBinaireOp(String str) {
		int indexOp = trouverIndex(str, OPERATEURS_BINAIRES);
		if (indexOp <= 1 || indexOp >= str.length() - 2) return null; // opérande mal placé

		Formule gauche = parse(str.substring(1, indexOp));
		Formule droite = parse(str.substring(indexOp + 1, str.length() - 1));
		if (gauche == null || droite == null) return null;

		return switch (str.charAt(indexOp)) {
			case '&' -> new AND(gauche, droite);
			case '|' -> new OR(gauche, droite);
			case '>' -> new IMPLIES(gauche, droite);
			case '?' -> new EQUIV(gauche, droite);
			default -> null;
		};
	}

	private static boolean estUnaireAE(String str) {
		char first = str.charAt(0), second = str.charAt(1);
		return (first == 'A' || first == 'E') && (second == 'X' || second == 'F' || second == 'G');
	}

	private static boolean estBinaireAE(String str) {
		char first = str.charAt(0), second = str.charAt(1);
		return (first == 'A' || first == 'E') && second == '(' && str.endsWith(")")
			&& parenthesesEquilibrees(str)
			&& str.contains("U"); // contient UNTIL
	}

	private static boolean estParenthesesBinaireOp(String str) {
		return str.startsWith("(") && str.endsWith(")")
			&& parenthesesEquilibrees(str)
			&& str.matches(".*[&|>?].*"); // contient AND/OR/IMPLIES/EQUIV
	}

	private static int nbOccurrences(String str, char targetChar) {
		return (int) str.chars().filter(c -> c == targetChar).count();
	}

	private static boolean parenthesesEquilibrees(String strFormule) {
		return nbOccurrences(strFormule, '(') == nbOccurrences(strFormule, ')');
	}

	private static int trouverIndex(String str, char[] caracteresRecherches) {
		int nbParenthesesOuvertes = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '(') nbParenthesesOuvertes++;
			else if (c == ')') nbParenthesesOuvertes--;
			else if (nbParenthesesOuvertes == 1) {
				for (char caractere : caracteresRecherches) {
					if (c == caractere) return i;
				}
			}
		}
		return -1;
	}
}
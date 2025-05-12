// Projet réalisé par Metroidzeta

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Automate {

	private Set<String> etats; // l'ensemble des états de l'automate
	private Set<String> ensembleLabels; // l'ensemble des étiquettes (propositions atomiques) reconnues par l'automate
	private Map<String, Set<String>> transitions; // on associe à chaque état -> une ou plusieurs transitions vers d'autres états
	private Map<String, Set<String>> labels; // on associe à chaque état -> un ensemble de propositions atomiques vraies dans l'état
	private Map<String, Map<Formule, Boolean>> evaluations; // on associe à chaque état --> une formule évaluée qui est true ou false

	public Automate() {
		etats = new HashSet<>();
		ensembleLabels = new HashSet<>();
		transitions = new HashMap<>();
		labels = new HashMap<>();
		evaluations = new HashMap<>();
	}

	public void ajouterEtat(String e) {
		if (e == null || e.isBlank()) throw new IllegalArgumentException("Nom de l'état null ou vide");
		etats.add(e);
	}

	private void verifierExistenceEtat(String e) {
		if (e == null || e.isBlank()) throw new IllegalArgumentException("Nom de l'état null ou vide");
		if (!etats.contains(e)) throw new IllegalArgumentException("L'état " + e + " n'existe pas");
	}

	public void ajouterTransition(String e1, String e2) { // Ajouter une transition : (-> e2) à el
		verifierExistenceEtat(e1);
		verifierExistenceEtat(e2);
		transitions.computeIfAbsent(e1, key -> new HashSet<>()).add(e2); // ajoute la transition à e1
	}

	public void ajouterLabel(String e, String l) { // Ajouter un label dans l'état
		verifierExistenceEtat(e);
		if (l == null || l.isBlank()) throw new IllegalArgumentException("Label null ou vide");
		if (l.matches(".*[A-Z].*") || l.matches(".*[-&|>?()].*")) {
			throw new IllegalArgumentException("label interdit : contient majuscules ou symboles : -, &, |, >, ?, (, )");
		}
		labels.computeIfAbsent(e, key -> new HashSet<>()).add(l); // ajoute le label dans cet état
		ensembleLabels.add(l);
	}

	private void ajouterEtatsDepuisFichier(String cheminFichier) {
		Path chemin = Paths.get(cheminFichier);
		try (BufferedReader reader = Files.newBufferedReader(chemin)) { // try-with-resources
			String ligne;
			while ((ligne = reader.readLine()) != null) {
				ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
				if (ligne.startsWith("states:")) {
					while ((ligne = reader.readLine()) != null) {
						ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
						if (ligne.isEmpty()) break;
						if (ligne.contains(";")) {
							String[] tab_etat = ligne.split(";");
							for (String e : tab_etat) {
								ajouterEtat(e);
							}
						}
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void ajouterTransitionsDepuisFichier(String cheminFichier) {
		Path chemin = Paths.get(cheminFichier);
		try (BufferedReader reader = Files.newBufferedReader(chemin)) { // try-with-resources
			String ligne;
			while ((ligne = reader.readLine()) != null) {
				ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
				if (ligne.startsWith("transitions:")) {
					while ((ligne = reader.readLine()) != null) {
						ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
						if (ligne.isEmpty()) { break; }
						if (ligne.contains("->") && ligne.contains(";")) {
							String[] tab_transitions = ligne.split(";");
							for (String t : tab_transitions) {
								String[] transition = t.split("->");
								if (transition.length > 1) { // faut qu'il y ait ou moins 2 cases dans le tableau (source et destination)
									ajouterTransition(transition[0],transition[1]); // ajouterTransition(source,destination)
								}
							}
						}
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void ajouterLabelsDepuisFichier(String cheminFichier) {
		Path chemin = Paths.get(cheminFichier);
		try (BufferedReader reader =  Files.newBufferedReader(chemin)) { // try-with-resources
			String ligne;
			while ((ligne = reader.readLine()) != null) {
				ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
				if (ligne.startsWith("labels:")) {
					while ((ligne = reader.readLine()) != null) {
						ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
						if (ligne.isEmpty()) { break; }
						if (ligne.contains(":") && ligne.contains(";")) {
							String[] tab_etats_labels = ligne.split(";");
							for (String etat_labels : tab_etats_labels) {
								String[] parts = etat_labels.split(":");
								if (parts.length > 1) { // faut qu'il y ait ou moins 2 parties (gauche et droite)
									String etat = parts[0]; // ce qu'il y a à gauche de ':'
									String[] tab_labels = parts[1].replaceAll("\"","").split(","); // ce qu'il y a à droite de ':'
									for (String l : tab_labels) {
										ajouterLabel(etat,l);
									}
								}
							}
						}
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ajouterDonneesDepuisFichier(String chemin) {
		ajouterEtatsDepuisFichier(chemin);
		ajouterTransitionsDepuisFichier(chemin);
		ajouterLabelsDepuisFichier(chemin);	
	}

	private Set<String> getPredecesseurs(String e) {
		Set<String> predecesseurs = new HashSet<>();
		transitions.forEach((etat, successeurs) -> {
			if (successeurs.contains(e)) predecesseurs.add(etat);
		});
		return predecesseurs;
	}

	private Set<String> getSuccesseurs(String e) {
		return transitions.getOrDefault(e, Collections.emptySet());
	}

	private int nbOccurrences(String str, char targetChar) {
		return (int) str.chars().filter(c -> c == targetChar).count();
	}

	private boolean parenthesesEquilibrees(String strFormule) {
		return nbOccurrences(strFormule, '(') == nbOccurrences(strFormule, ')');
	}

	private int trouverIndex(String str, char[] caracteresRecherches) {
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

	private Formule parseNOT(String str) {
		Formule droite = parse(str.substring(1)); // parse(strDroite)
		return droite == null ? null : new NOT(droite); // null ou NOT(droite)
	}

	public Formule parse(String str) { // transforme un string en une formule CTL (null = mauvaise syntaxe)
		if (str == null || str.isBlank()) return null;
		if (str.equalsIgnoreCase("false")) return new FALSE(); // "false/FALSE"
		if (str.equalsIgnoreCase("true")) return new TRUE(); // "true/TRUE"
		if (ensembleLabels.contains(str)) return new PROP(str); // proposition

		char firstChar = str.charAt(0);

		if (firstChar == '-') { // NOT
			parseNOT(str);
		}

		if (str.length() > 2) {
			char secondChar = str.charAt(1);
			if (firstChar == 'A' || firstChar == 'E') { // si premier caractère est A(pour tout) ou E(il existe)
				if (secondChar == 'X' || secondChar == 'F' || secondChar == 'G') { // si second caractère est X(Next), F(Future) ou G(Global)
					Formule droite = parse(str.substring(2)); // parse(strDroite)
					if (droite == null) return null;
					return switch ("" + firstChar + secondChar) {
						case "AX" -> new AX(droite);
						case "AF" -> new AF(droite);
						case "AG" -> new AG(droite);
						case "EX" -> new EX(droite);
						case "EF" -> new EF(droite);
						case "EG" -> new EG(droite);
						default -> null;
					};
				}
				if (secondChar == '(' && str.endsWith(")") // si second caractère est '(' et que le str termine par ')'
					&& parenthesesEquilibrees(str) // si nbParenthesesOuvrantes = nbParenthèsesFermantes
					&& str.contains("U")) { // si il contient un UNTIL
					char[] charU = {'U'};
					int indexU = trouverIndex(str, charU);
					if (indexU != -1 && indexU != 2 && indexU != str.length() - 2) { // rappel: index(0) = 'A' ou 'E', index(1) = '(' et index(length() - 1) = ')', si il y a du texte à gauche et à droite du symbole 'U'
						Formule gauche = parse(str.substring(2, indexU)); // parse(strGauche)
						Formule droite = parse(str.substring(indexU + 1, str.length() - 1)); // parse(strDroite)
						if (gauche != null && droite != null) {
							return firstChar == 'A' ? new AU(gauche, droite) : new EU(gauche, droite);
						}
					}
					return null;
				}
			}
			if (firstChar == '(' && str.endsWith(")") // si str commence par '(' et termine par ')'
				&& parenthesesEquilibrees(str) // si nbParenthesesOuvrantes = nbParenthesesFermantes
				&& str.matches(".*[&|>?].*")) { // si contient un AND, OR, IMPLIES ou EQUIV
				char[] caracteres = {'&', '|', '>', '?'};
				int index = trouverIndex(str, caracteres);
				if (index != -1 && index != 1 && index != str.length() - 2) { // rappel: index(0) = '(' et index(length() - 1) = ')', si il y a du texte à gauche et à droite du symbole '&','|','>' ou '?'
					Formule gauche = parse(str.substring(1, index));
					Formule droite = parse(str.substring(index + 1, str.length() - 1));
					if (gauche != null && droite != null) {
						char c = str.charAt(index);
						return switch (c) {
							case '&' -> new AND(gauche, droite);
							case '|' -> new OR(gauche, droite);
							case '>' -> new IMPLIES(gauche, droite);
							case '?' -> new EQUIV(gauche, droite);
							default -> null;
						};
					}
				}
				return null;
			}
		}
		return null;
	}

	private void afficherEtats() { System.out.println("Liste des etats : " + etats); }

	private void afficherTransitions() {
		System.out.println("Liste des transitions : ");
		transitions.forEach((e1, e2Set) -> e2Set.forEach(e2 -> System.out.println(e1 + " -> " + e2)));
	}

	private void afficherLabels() {
		System.out.println("Liste des labels : ");
		labels.forEach((etat, labelSet) -> System.out.println("label de l'etat " + etat + " : " + labelSet));
	}

	public void afficherInformations() { // affiche la liste des états, transitions et labels de l'automate
		afficherEtats();
		afficherTransitions();
		afficherLabels();
	}

	public void afficherEvaluation(Formule formule) { // afficher l'évaluation de la formule pour chaque état (si elle existe)
		for (String etat : evaluations.keySet()) {
			if (evaluations.get(etat).containsKey(formule)) {
				System.out.println("Pour l'etat " + etat + ", " + formule.toString() + " -> " + evaluations.get(etat).get(formule));
			}
		}
	}

	public void afficherToutesEvaluations() { // afficher toutes les évaluations des formules déjà évaluées de chaque état
		for (Map.Entry<String, Map<Formule, Boolean>> outerMap : evaluations.entrySet()) {
			System.out.println("Pour l'etat " +  outerMap.getKey() + " :");
			for (Map.Entry<Formule, Boolean> innerMapEntry : outerMap.getValue().entrySet()) {
				System.out.println(innerMapEntry.getKey().toString() + " -> " + innerMapEntry.getValue());
			}
			System.out.println();
		}
	}

	private boolean getEvaluation(String e, Formule formule) {
		return evaluations.getOrDefault(e, Collections.emptyMap()).getOrDefault(formule, false);
	}

	private void setEvaluation(String e, Formule formule, boolean b) {
		evaluations.computeIfAbsent(e, key -> new HashMap<>()).put(formule, b);
	}

	private void marquerEtEvaluer(Formule formule, Formule formlEquiv) {
		marquage(formlEquiv); // marque formule équivalente
		etats.forEach(e -> setEvaluation(e, formule, getEvaluation(e, formlEquiv))); // évalue la formule de base pour chaque état à partir de la formule équivalente
	}

	private boolean estEvalue(Formule formule) {
		for (String etat : evaluations.keySet()) {
			return evaluations.get(etat).containsKey(formule);
		}
		return false;
		//return evaluations.values().stream().anyMatch(etatMap -> etatMap.containsKey(formule));
	}

	private void marquerGD(Formule gauche, Formule droite) {
		marquage(gauche);
		marquage(droite);
	}

	public void marquage(Formule formule) {
		if (!estEvalue(formule)) {
			if (formule instanceof FALSE) etats.forEach(e -> setEvaluation(e, formule, false)); // φ = FALSE
			else if (formule instanceof TRUE) etats.forEach(e -> setEvaluation(e, formule, true)); // φ = TRUE
			else if (formule instanceof PROP) { // PROPOSITION : φ = label
				PROP f_prop = (PROP) formule;
				etats.forEach(e -> setEvaluation(e, formule,
				labels.containsKey(e) && labels.get(e).contains(f_prop.toString()))); // pour tous les états, si p € L(e) = true, sinon false
			}
			else if (formule instanceof NOT) { // NEGATION : φ = -φ'
				NOT f_not = (NOT) formule;
				Formule droite = f_not.getDroite();
				marquage(droite); // On marque φ'
				etats.forEach(e -> setEvaluation(e, formule, !getEvaluation(e, droite))); 
			}
			else if (formule instanceof EX) { // IL EXISTE NEXT : φ = EXφ'
				EX f_ex = (EX) formule;
				Formule droite = f_ex.getDroite();
				marquage(droite); // On marque φ'
				etats.forEach(e -> setEvaluation(e, formule, false)); // f pour chaque état évalué false
				for (String e : etats) {
					for (String successeur : getSuccesseurs(e)) {
						if (getEvaluation(successeur, droite)) { // si successeur.φ' == true
							setEvaluation(e, formule, true);
							break; // sortir de la boucle successeurs
						}
					}
				}
			}
			else if (formule instanceof EF) { // IL EXISTE FUTUR : φ = EFφ'
				EF f_ef = (EF) formule;
				Formule droite = f_ef.getDroite();
				marquage(droite); // On marque φ'
				marquerEtEvaluer(formule, new EU(new TRUE(), droite)); // EFφ' = E(trueUφ')
			}
			else if (formule instanceof EG) { // IL EXISTE GLOBAL : φ = EGφ'
				EG f_eg = (EG) formule;
				Formule droite = f_eg.getDroite();
				marquage(droite); // On marque φ'
				marquerEtEvaluer(formule, new NOT(new AF(new NOT(droite)))); // EGφ' = -(AF-(φ'))
			}
			else if (formule instanceof AX) { // POUR TOUT NEXT : φ = AXφ'
				AX f_ax = (AX) formule;
				Formule droite = f_ax.getDroite();
				marquage(droite); // On marque φ'
				etats.forEach(e -> setEvaluation(e, formule, false)); // f pour chaque état évalué false
				for (String e : etats) {
					boolean resultat = true;
					for (String successeur : getSuccesseurs(e)) {
						if (!getEvaluation(successeur, droite)) { // si successeur.φ' == false
							resultat = false;
							break; // on sort de la boucle des successeurs
						}
					}
					if (resultat) setEvaluation(e, formule, true);
				}
			}
			else if (formule instanceof AF) { // POUR TOUT FUTUR : φ = AFφ'
				AF f_af = (AF) formule;
				Formule droite = f_af.getDroite();
				marquage(droite); // On marque φ'
				marquerEtEvaluer(formule, new AU(new TRUE(), droite)); // AFφ' = A(trueUφ')
			}
			else if (formule instanceof AG) { // POUR TOUT GLOBAL : φ = AGφ'
				AG f_ag = (AG) formule;
				Formule droite = f_ag.getDroite();
				marquage(droite); // On marque φ'
				marquerEtEvaluer(formule, new NOT(new EF(new NOT(droite)))); // AGφ' = -(EF-(φ'))
			}
			else if (formule instanceof AND) { // AND : φ = (φ' & φ'')
				AND f_and = (AND) formule;
				Formule gauche = f_and.getGauche(), droite = f_and.getDroite();
				marquerGD(gauche, droite); // on marque φ' et φ''
				etats.forEach(e -> setEvaluation(e, formule, getEvaluation(e, gauche) && getEvaluation(e, droite))); // les états, φ == (φ' & φ'')
			}
			else if (formule instanceof OR) { // OR : φ = (φ' | φ'')
				OR f_or = (OR) formule;
				Formule gauche = f_or.getGauche(), droite = f_or.getDroite();
				marquerGD(gauche, droite); // on marque φ' et φ''
				etats.forEach(e -> setEvaluation(e, formule, getEvaluation(e, gauche) || getEvaluation(e, droite))); // les états, φ == (φ' | φ'')
			}
			else if (formule instanceof EU) { // IL EXISTE UNTIL : φ = E(φ' U φ'')
				EU f_eu = (EU) formule;
				Formule gauche = f_eu.getGauche(), droite = f_eu.getDroite();
				marquerGD(gauche, droite); // on marque φ' et φ''
				Map<String, Boolean> seenBefore = new HashMap<>(); // chaque état sera associé à un booléen (si il a déjà été visité)
				for (String e : etats) { // pour tous les états
					setEvaluation(e, formule, false); // la formule est évalué false par défaut
					seenBefore.put(e, false);
				}
				ArrayList<String> L = new ArrayList<>();
				for (String e : etats) { // pour tous les états
					if (getEvaluation(e, droite)) { // si s.φ'' == true
						L.add(e);
					}
				}
				while (!L.isEmpty()) {
					String s = L.get(0);
					L.remove(s);
					setEvaluation(s, formule, true);
					for (String predecesseur : getPredecesseurs(s)) { // pour tous les prédécesseurs de cet état
						if (!seenBefore.get(predecesseur)) {
							seenBefore.put(predecesseur, true);
							if (getEvaluation(predecesseur, gauche)) { // si s.φ' == true
								L.add(predecesseur);
							}
						}
					}
				}
			}
			else if (formule instanceof AU) { // POUR TOUT UNTIL : φ = A(φ' U φ'')
				AU f_au = (AU) formule;
				Formule gauche = f_au.getGauche(), droite = f_au.getDroite();
				marquerGD(gauche, droite); // on marque φ' et φ''
				ArrayList<String> L2 = new ArrayList<>();
				Map<String, Integer> nb = new HashMap<>();
				for (String e : etats) {
					nb.put(e, getSuccesseurs(e).size()); // chaque état associé à son nombre de successeurs
					setEvaluation(e, formule, false); // la formule est évalué false par défaut
					if (getEvaluation(e, droite)) { // si s.φ'' == true
						L2.add(e);
					}
				}
				while (!L2.isEmpty()) {
					String s = L2.get(0);
					L2.remove(s);
					setEvaluation(s, formule, true);
					for (String predecesseur : getPredecesseurs(s)) { // pour tous les prédécesseurs de cet état
						int valeurNb = nb.get(predecesseur) - 1;
						nb.put(predecesseur, valeurNb);
						if (valeurNb == 0 && getEvaluation(predecesseur, gauche) && !getEvaluation(predecesseur, formule)) {
							L2.add(predecesseur);
						}
					}
				}
			}
			else if (formule instanceof IMPLIES) { // IMPLICATION : φ = (φ' => φ'')
				IMPLIES f_implies = (IMPLIES) formule;
				Formule gauche = f_implies.getGauche(), droite =  f_implies.getDroite();
				marquerGD(gauche, droite); // on marque φ' et φ''
				marquerEtEvaluer(formule, new OR(new NOT(gauche), droite)); // (φ' => φ'') = (-φ' | φ'')
			}
			else if (formule instanceof EQUIV) { // EQUIVALENCE : φ = (φ' <=> φ'')
				EQUIV f_equiv = (EQUIV) formule;
				Formule gauche = f_equiv.getGauche(), droite = f_equiv.getDroite();
				marquerGD(gauche, droite); // on marque φ' et φ''
				marquerEtEvaluer(formule, new AND(new IMPLIES(gauche, droite), new IMPLIES(droite, gauche))); // (φ' <=> φ'') = (φ' => φ'') & (φ'' => φ')
			}
		}
	}
}
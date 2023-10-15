// Projet réalisé par Metroidzeta

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Automate {

	private Set<String> ensembleEtats; // L'ensemble des états de l'automate
	private Set<String> ensembleLabels; // L'ensemble des étiquettes (propositions) reconnues par l'automate
	private Map<String, Set<String>> transitions; // on associe à chaque état -> une ou plusieurs transitions vers d'autres états
	private Map<String, Set<String>> labels; // on associe à chaque état -> un ensemble de propositions atomiques vraies dans l'état
	private Map<String, Map<Formule, Boolean>> evaluations; // on associe à chaque état --> une formule évaluée qui est true ou false

	public Automate() {
		this.ensembleEtats = new HashSet<>();
		this.ensembleLabels = new HashSet<>();
		this.transitions = new HashMap<>();
		this.labels = new HashMap<>();
		this.evaluations = new HashMap<>();
	}

	public void ajouterEtat(String e) { // Ajouter un état
		ensembleEtats.add(e);
	}

	public void ajouterTransition(String e1, String e2) { // Ajouter une transition entre 2 états : e1 -> e2, avec e1 = départ et e2 = arrivé
		if(!ensembleEtats.contains(e1) || !ensembleEtats.contains(e2)) { // si l'état e1 ou e2 n'existe pas
			System.out.println("Impossible d'ajouter la transition entre l'état " + e1 + " et " + e2 + " car l'état " + (!ensembleEtats.contains(e1) ? "e1" : "e2") + " n'existe pas");
			return;
		}
		transitions.computeIfAbsent(e1,key -> new HashSet<>()).add(e2); // ajoute la transition -> e2 aux transitions de l'état e1 (si l'ensemble des transitions de e1 n'existe pas, on le crée)
	}

	public void ajouterLabel(String etat, String l) { // Ajouter une étiquette dans un état
		if(l == null || l.isEmpty() || l.matches(".*[A-Z].*") || l.matches(".*[-&|>?()].*")) { // les labels interdits
			System.out.println("Impossible d'ajouter l'étiquette \"" + l + "\" pour l'état " + etat + " car c'est une étiquette interdite : [vide,contientMajuscule (!= 0),-,&,|,>,?,(,)]");
			return;
		}
		if(!ensembleEtats.contains(etat)) { // si l'état n'existe pas
			System.out.println("Impossible d'ajouter l'étiquette \"" + l + "\" pour l'état " + etat + " car cet état n'existe pas");
			return;
		}
		labels.computeIfAbsent(etat,key -> new HashSet<>()).add(l); // on ajoute l'étiquette dans cet état (si les labels de e1 n'existent pas, on le crée)
		ensembleLabels.add(l); // on ajoute cette étiquette à l'ensemble des labels reconnus de l'automate
	}

	public void ajouterEtatsDepuisFichier(String cheminFichier) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(cheminFichier));
			String ligne;
			while((ligne = reader.readLine()) != null) {
				ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
				if(ligne.startsWith("states:")) {
					while((ligne = reader.readLine()) != null) {
						ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
						if(ligne.isEmpty()) { break; }
						if(ligne.contains(";")) { // si la ligne contient le symbole ';'
							String[] tab_etat = ligne.split(";");
							for(String e : tab_etat) {
								if(!e.isEmpty()) {
									ajouterEtat(e);
								}
							}
						}
					}
					break;
				}
			}
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void ajouterTransitionsDepuisFichier(String cheminFichier) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(cheminFichier));
			String ligne;
			while((ligne = reader.readLine()) != null) {
				ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
				if(ligne.startsWith("transitions:")) {
					while((ligne = reader.readLine()) != null) {
						ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
						if(ligne.isEmpty()) { break; }
						if(ligne.contains("->") && ligne.contains(";")) { // si la ligne contient le symbole '->' et ';'
							String[] tab_transitions = ligne.split(";");
							for(String t : tab_transitions) {
								String[] transition = t.split("->");
								if(transition.length > 1) { // faut qu'il y ait ou moins 2 cases dans le tableau (source et destination)
									ajouterTransition(transition[0],transition[1]); // ajouterTransition(source,destination)
								}
							}
						}
					}
					break;
				}
			}
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void ajouterLabelsDepuisFichier(String cheminFichier) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(cheminFichier));
			String ligne;
			while((ligne = reader.readLine()) != null) {
				ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
				if(ligne.startsWith("labels:")) {
					while((ligne = reader.readLine()) != null) {
						ligne = ligne.replaceAll("[\\t\\s]+",""); // ignore les tabulations et les espaces
						if(ligne.isEmpty()) { break; }
						if(ligne.contains(":") && ligne.contains(";")) { // si la ligne contient le symbole ':' et ';'
							String[] tab_etats_labels = ligne.split(";");
							for(String etat_labels : tab_etats_labels) {
								String[] parts = etat_labels.split(":");
								if(parts.length > 1) { // faut qu'il y ait ou moins 2 parties (gauche et droite)
									String etat = parts[0]; // ce qu'il y a à gauche de ':'
									String[] tab_labels = parts[1].replaceAll("\"","").split(","); // ce qu'il y a à droite de ':'
									for(String l : tab_labels) {
										ajouterLabel(etat,l);
									}
								}
							}
						}
					}
					break;
				}
			}
			reader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void ajouterDonneesDepuisFichier(String cheminFichier) {
		ajouterEtatsDepuisFichier(cheminFichier);
		ajouterTransitionsDepuisFichier(cheminFichier);
		ajouterLabelsDepuisFichier(cheminFichier);	
	}

	public Set<String> getPredecesseurs(String etat) {
		Set<String> predecesseurs = new HashSet<>();
		for(Map.Entry<String, Set<String>> entry : transitions.entrySet()) {
			if(entry.getValue().contains(etat)) { // si cet état a un prédécesseur
				predecesseurs.add(entry.getKey()); // on ajoute le prédécesseur
			}
		}
		return predecesseurs;
	}

	public Set<String> getSuccesseurs(String etat) {
		return transitions.getOrDefault(etat,Collections.emptySet());
	}

	public int nbOccurrences(String str, char targetChar) {
		int compteur = 0;
		for(char c : str.toCharArray()) {
			if(c == targetChar) {
				compteur++;
			}
		}
		return compteur;
	}

	public int trouverIndex(String str, char[] caracteresRecherches) {
		int nbParenthesesOuvertes = 0;
		for(int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if(c == '(') {
				nbParenthesesOuvertes++;
			} else if(c == ')') {
				nbParenthesesOuvertes--;
			} else if(nbParenthesesOuvertes == 1) {
				for(char caractere : caracteresRecherches) {
					if(c == caractere) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	public Formule parse(String strFormule) { // Le parseur CTL qui permet de transformer un string en une formule CTL (return null = mauvaise syntaxe)
		if(strFormule == null || strFormule.isEmpty()) { // si le texte est null ou vide
			return null;
		}
		if(strFormule.equalsIgnoreCase("true")) { // si le texte est "true"
			return new TRUE();
		}
		if(ensembleLabels.contains(strFormule)) { // si le texte est une proposition
			return new PROP(strFormule);
		}
		char firstChar = strFormule.charAt(0);
		if(firstChar == '-') { // si le texte commence par un NOT (avec un '-')
			String strFinFormule = strFormule.substring(1);
			Formule finFormule = parse(strFinFormule);
			return (finFormule != null) ? new FormuleXArgs(FType.NOT,finFormule) : null; // NOT(finFormule) ou null
		}
		if(strFormule.length() > 2) { // si le texte fait au moins 3 caractères
			char secondChar = strFormule.charAt(1);
			if(firstChar == 'A' || firstChar == 'E') { // si le premier caractère est A (POUR TOUT) ou un E (IL EXISTE)
				if(secondChar == 'X' || secondChar == 'F' || secondChar == 'G') { // si le second caractère est X (NEXT), F (Future) ou G (Global)
					String strFinFormule = strFormule.substring(2);
					Formule finFormule = parse(strFinFormule);
					return (finFormule != null) ? new FormuleXArgs(FType.valueOf("" + firstChar + secondChar),finFormule) : null; //Ax(finFormule) ou Ex(finFormule) avec x = X/F/G ou null
				}
				if(secondChar == '(' && strFormule.endsWith(")") // si le second caractère est '(' et que le string termine par ')'
				&& nbOccurrences(strFormule,'(') == nbOccurrences(strFormule,')') // et si nb de parenthèses ouvrantes = nb de parenthèses fermantes
				&& strFormule.contains("U")) { // et qu'il contient un UNTIL ('U')
					char[] caractereU = {'U'};
					int indexU = trouverIndex(strFormule,caractereU);
					if(indexU != -1 && indexU != 2 && indexU != strFormule.length() - 2) { // rappel: index(0) = 'A' ou 'E', index(1) = '(' et index(length() - 1) = ')', si il y a du texte à gauche et à droite du symbole 'U'
						String strGauche = strFormule.substring(2,indexU);
						String strDroite = strFormule.substring(indexU + 1,strFormule.length() - 1);
						Formule gauche = parse(strGauche);
						Formule droite = parse(strDroite);
						if(gauche != null && droite != null) {
							return new FormuleXArgs(FType.valueOf(firstChar + "U"),gauche,droite); // AU(gauche,droite) ou EU(gauche,droite)
						}
					}
					return null;
				}
			}
			if(firstChar == '(' && strFormule.endsWith(")") // si le texte commence par '(' et termine par ')'
			&& nbOccurrences(strFormule,'(') == nbOccurrences(strFormule,')') // et si nb de parenthèses ouvrantes = nb de parenthèses fermantes			
			&& strFormule.matches(".*[&|>?].*")) { // et qu'il contient un AND, OR, IMPLIES ou EQUIV ('&','|','>','?')
				char[] caracteres = {'&','|','>','?'};
				int index = trouverIndex(strFormule,caracteres);
				if(index != -1 && index != 1 && index != strFormule.length() - 2) { // rappel: index(0) = '(' et index(length() - 1) = ')', si il y a du texte à gauche et à droite du symbole '&','|','>' ou '?'
					String strGauche = strFormule.substring(1,index);
					String strDroite = strFormule.substring(index + 1,strFormule.length() - 1);
					Formule gauche = parse(strGauche);
					Formule droite = parse(strDroite);
					if(gauche != null && droite != null) {
						char c = strFormule.charAt(index);
						switch(c) {
							case '&': return new FormuleXArgs(FType.AND,gauche,droite); // AND(gauche,droite)
							case '|': return new FormuleXArgs(FType.OR,gauche,droite); // OR(gauche,droite)
							case '>': return new FormuleXArgs(FType.IMPLIES,gauche,droite); // IMPLIES(gauche,droite)
							case '?': return new FormuleXArgs(FType.EQUIV,gauche,droite); // EQUIV(gauche,droite)
						}
					}
				}
				return null;
			}
		}
		return null;
	}

	public void afficherEtats() { System.out.println("Voici la liste des etats : " + ensembleEtats); }

	public void afficherTransitions() {
		System.out.println("Voici la liste des transitions : ");
		transitions.forEach((e1, e2Set) -> e2Set.forEach(e2 -> System.out.println(e1 + " -> " + e2)));
	}

	public void afficherLabels() {
		System.out.println("Voici la liste des labels : ");
		labels.forEach((etat, labelSet) -> System.out.println("label de l'etat " + etat + " : " + labelSet));
	}

	public void afficherInformations() { // Affiche les états, les transitions et les étiquettes (labels) de l'automate
		afficherEtats();
		afficherTransitions();
		afficherLabels();
	}

	public void afficherEvaluation(Formule formule) { // Pour tous les états, afficher l'évaluation de la formule passé en paramètre (si elle est déjà évalué)
		for(String etat : evaluations.keySet()) {
			if(evaluations.get(etat).containsKey(formule)) {
				System.out.println("Pour l'etat " + etat + ", " + formule.toString() + " -> " + evaluations.get(etat).get(formule));
			}
		}
	}

	public void afficherToutesEvaluations() { // Pour tous les états, afficher toutes les évaluations de toutes les formules déjà évaluées
		for(Map.Entry<String, Map<Formule, Boolean>> outerMap : evaluations.entrySet()) {
			System.out.println("Pour l'etat " +  outerMap.getKey() + " :");
			for(Map.Entry<Formule, Boolean> innerMapEntry : outerMap.getValue().entrySet()) {
				System.out.println(innerMapEntry.getKey().toString() + " -> " + innerMapEntry.getValue());
			}
			System.out.println();
		}
	}

	public boolean getEvaluation(String etat, Formule formule) {
		return evaluations.getOrDefault(etat,Collections.emptyMap()).getOrDefault(formule,false);
	}

	public void setEvaluation(String etat, Formule formule, boolean b) {
		evaluations.computeIfAbsent(etat,key -> new HashMap<>()).put(formule,b);
	}

	public void marquerEtEvaluer(Formule formule, Formule formlEquiv) {	
		marquage(formlEquiv); // on marque sa formule équivalente
		ensembleEtats.forEach(e -> setEvaluation(e,formule,getEvaluation(e,formlEquiv))); // on évalue la formule de base pour tous les états à partir de la formule équivalente
	}

	public boolean estDejaEvalue(Formule formule) {
		for(String etat : evaluations.keySet()) {
			return evaluations.get(etat).containsKey(formule);
		}
		return false;
		//return evaluations.values().stream().anyMatch(etatMap -> etatMap.containsKey(formule));
	}

	public void marquage(Formule formule) {
		if(!estDejaEvalue(formule)) {
			if(formule instanceof TRUE) { // TRUE : φ = true
				ensembleEtats.forEach(e -> setEvaluation(e,formule,true)); // pour tous les états, Formule(true) = true
			}
			else if(formule instanceof PROP) { // PROPOSITION : φ = p
				PROP prop = (PROP) formule;
				ensembleEtats.forEach(e -> setEvaluation(e,formule,labels.containsKey(e) && labels.get(e).contains(prop.toString()))); // pour tous les états, si p € L(e): formule evaluée true, sinon false
			}
			else if(formule instanceof FormuleXArgs) { // si c'est une formule avec X args (1 ou 2)
				FormuleXArgs fxa = (FormuleXArgs) formule;
				if(fxa.getTaille() == 1) { // si c'est une formule avec 1 argument (sous-formule droite)
					Formule finFormule = fxa.getFormules()[0];
					marquage(finFormule); // On marque φ'
					switch(fxa.getType()) {
						case NOT: // NEGATION : φ = -φ'
							ensembleEtats.forEach(e -> setEvaluation(e,formule,!getEvaluation(e,finFormule))); // pour tous les états φ == !φ'
							break;
						case EX: // IL EXISTE NEXT : φ = EXφ'
							ensembleEtats.forEach(e -> setEvaluation(e,formule,false)); // pour tous les états, la formule est évalué false par défaut
							for(String e : ensembleEtats) { // pour tous les états
								for(String successeur : getSuccesseurs(e)) { // pour tous les sucesseurs de cet état
									if(getEvaluation(successeur,finFormule)) { // si successeur.φ' == true
										setEvaluation(e,formule,true);
										break; // on sort de la boucle des successeurs
									}
								}
							}
							break;
						case EF: // IL EXISTE FUTUR : φ = EFφ'
							marquerEtEvaluer(formule,new FormuleXArgs(FType.EU,new TRUE(),finFormule)); // on sait que EFφ' = E(true U φ')
							break;
						case EG: // IL EXISTE GLOBAL : φ = EGφ'
							marquerEtEvaluer(formule,new FormuleXArgs(FType.NOT,new FormuleXArgs(FType.AF,new FormuleXArgs(FType.NOT,finFormule)))); // On sait que EGφ' = -(AF-(φ'))
							break;
						case AX: // POUR TOUT NEXT : φ = AXφ'
							ensembleEtats.forEach(e -> setEvaluation(e,formule,false)); // pour tous les états, la formule est évalué false par défautt
							for(String e : ensembleEtats) { // pour tous les états
								boolean resultat = true;
								for(String successeur : getSuccesseurs(e)) { // pour tous les sucesseurs de cet état
									if(!getEvaluation(successeur,finFormule)) { // si successeur.φ' == false
										resultat = false;
										break; // on sort de la boucle des successeurs
									}
								}
								if(resultat) {
									setEvaluation(e,formule,true);
								}
							}
							break;
						case AF: // POUR TOUT FUTUR : φ = AFφ'
							marquerEtEvaluer(formule,new FormuleXArgs(FType.AU,new TRUE(),finFormule)); // on sait que AFφ' = A(true U φ')
							break;
						case AG: // POUR TOUT GLOBAL : φ = AGφ'
							marquerEtEvaluer(formule,new FormuleXArgs(FType.NOT,new FormuleXArgs(FType.EF,new FormuleXArgs(FType.NOT,finFormule)))); // on sait que AGφ' = -(EF-(φ'))
							break;
					}
				}
				else if(fxa.getTaille() == 2) { // si c'est une formule avec 2 arguments (sous-formule gauche et sous-formule droite)
					Formule gauche = fxa.getFormules()[0];
					Formule droite = fxa.getFormules()[1];
					marquage(gauche); // on marque φ'
					marquage(droite); // on marque φ''
					switch(fxa.getType()) {
						case AND: // AND : φ = (φ'&φ'')
							ensembleEtats.forEach(e -> setEvaluation(e,formule,getEvaluation(e,gauche) && getEvaluation(e,droite))); // pour tous les états, φ == (φ'&φ'')
							break;
						case OR: // OR : φ = (φ'|φ'')
							ensembleEtats.forEach(e -> setEvaluation(e,formule,getEvaluation(e,gauche) || getEvaluation(e,droite))); // pour tous les états, φ == (φ'|φ'')
							break;
						case EU: // IL EXISTE UNTIL : φ = E(φ'Uφ'')
							Map<String, Boolean> seenbefore = new HashMap<>(); // chaque état sera associé à un booleen (pour savoir si il a déjà été visité)
							for(String e : ensembleEtats) { // pour tous les états
								setEvaluation(e,formule,false); // la formule est évalué false par défaut
								seenbefore.put(e,false);
							}
							ArrayList<String> L = new ArrayList<>();
							for(String e : ensembleEtats) { // pour tous les états
								if(getEvaluation(e,droite)) { // si s.φ'' == true
									L.add(e);
								}
							}
							while(!L.isEmpty()) {
								String s = L.get(0);
								L.remove(s);
								setEvaluation(s,formule,true);
								for(String predecesseur : getPredecesseurs(s)) { // pour tous les prédécesseurs de cet état
									if(!seenbefore.get(predecesseur)) {
										seenbefore.put(predecesseur,true);
										if(getEvaluation(predecesseur,gauche)) { // si s.φ' == true
											L.add(predecesseur);
										}
									}
								}
							}
							break;
						case AU: // POUR TOUT UNTIL : φ = A(φ'Uφ'')
							ArrayList<String> L2 = new ArrayList<>();
							Map<String, Integer> nb = new HashMap<>(); // chaque état sera associé à son nombre de successeurs
							for(String e : ensembleEtats) { // pour tous les états
								nb.put(e,getSuccesseurs(e).size());
								setEvaluation(e,formule,false); // la formule est évalué false par défaut
								if(getEvaluation(e,droite)) { // si s.φ'' == true
									L2.add(e);
								}
							}
							while(!L2.isEmpty()) {
								String s = L2.get(0);
								L2.remove(s);
								setEvaluation(s,formule,true);
								for(String predecesseur : getPredecesseurs(s)) { // pour tous les prédécesseurs de cet état
									int valeurNb = nb.get(predecesseur) - 1;
									nb.put(predecesseur,valeurNb);
									if(valeurNb == 0 && getEvaluation(predecesseur,gauche) && !getEvaluation(predecesseur,formule)) {
										L2.add(predecesseur);
									}
								}
							}
							break;
						case IMPLIES: // IMPLICATION : φ = (φ'=>φ'')
							marquerEtEvaluer(formule,new FormuleXArgs(FType.OR,new FormuleXArgs(FType.NOT,gauche),droite)); // on sait que (φ'=>φ'') = (-φ'|φ'')
							break;
						case EQUIV: // EQUIVALENCE : φ = (φ'<=>φ'')
							marquerEtEvaluer(formule,new FormuleXArgs(FType.AND,new FormuleXArgs(FType.IMPLIES,gauche,droite),new FormuleXArgs(FType.IMPLIES,droite,gauche))); // on sait que (φ'<=>φ'') = (φ'=>φ'')&(φ''=>φ')
							break;
					}
				}
			}
		}
	}
}
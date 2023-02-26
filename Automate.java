// Projet réalisé par Metroidzeta

import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class Automate {

	private Set<String> ensembleEtats; // L'ensemble des états de l'automate
	private Set<String> ensembleLabels; // L'ensemble des étiquettes (propositions) reconnues par l'automate
	private Map<String, Set<String>> transitions; // on associé à chaque état -> une ou plusieurs transitions vers d'autres états
	private Map<String, Set<String>> labels; // on associe à chaque état -> un ensemble de propositions atomiques vraies dans l'état
	private Map<String, Map<Formule, Boolean>> evaluations; // on associe à chaque état --> une formule évaluée qui est true ou false

	public Automate() {
		this.ensembleEtats = new HashSet<>();
		this.ensembleLabels = new HashSet<>();
		this.transitions = new HashMap<String,Set<String>>();
		this.labels = new HashMap<String,Set<String>>();
		this.evaluations = new HashMap<String,Map<Formule, Boolean>>();
	}

	public void ajouterEtat(String e) { ensembleEtats.add(e); } // Ajouter un état

	public void ajouterTransition(String e1, String e2) { // Ajouter une transition entre 2 états : e1 = Départ, e2 = Arrivée
		if(!ensembleEtats.contains(e1)) { // Si l'état e1 n'existe pas
			System.out.println("Impossible d'ajouter la transition entre l'état " + e1 + " et " + e2 + " car l'état " + e1 + " n'existe pas");
			return;
		}
		if(!ensembleEtats.contains(e2)) { // Si l'état e2 n'existe pas
			System.out.println("Impossible d'ajouter la transition entre l'état " + e1 + " et " + e2 + " car l'état " + e2 + " n'existe pas");
			return;
		}
		Set<String> transitionsDepuisE1 = transitions.get(e1); // On récupère l'ensemble des transitions partant de cet état
		if(transitionsDepuisE1 == null) {
			transitionsDepuisE1 = new HashSet<>();
			transitions.put(e1,transitionsDepuisE1);
		}
		transitionsDepuisE1.add(e2);
	}

	public int nbr_maj(String str) {
		int compteur = 0;
		for(Character c : str.toCharArray()) {
			if((c > 64 && c < 91) || (c > 191 && c < 224)) { compteur++; } // si le caractère est une majuscule
		}
		return compteur;
	}

	public void ajouterLabel(String etat, String l) { // Ajouter une étiquette dans un état
		if(l.isEmpty() || nbr_maj(l) != 0 || l.equals("-") || l.equals("&") || l.equals("|") || l.equals(">") || l.equals("?") || l.equals("(") || l.equals(")")) { // Les labels interdits
			System.out.println("Impossible d'ajouter l'étiquette " + l + " pour l'état " + etat + " car c'est une étiquette interdite : [vide,Contient lettre Majuscule (!= 0),-,&,|,>,?,(,)]");
			return;
		}
		if(!ensembleEtats.contains(etat)) { // Si l'état n'existe pas
			System.out.println("Impossible d'ajouter l'étiquette " + l + " pour l'état " + etat + " car cet état n'existe pas");
			return;
		}
		Set<String> etiquettesDepuisEtat = labels.get(etat); // On récupère l'ensemble des étiquettes de cet état
		if(etiquettesDepuisEtat == null) {
			etiquettesDepuisEtat = new HashSet<>();
			labels.put(etat,etiquettesDepuisEtat);
		}
		etiquettesDepuisEtat.add(l); // On ajoute l'étiquette dans cet état
		ensembleLabels.add(l); // On ajoute cette étiquette à l'ensemble des labels existants
	}

	public void getEtatsDepuisFichier(String cheminFichier) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(cheminFichier));
			String ligne;
			while((ligne = reader.readLine()) != null) {
				ligne = ligne.replaceAll("\t","").replaceAll(" ",""); // Ignore les tabulations et les espaces
				if(ligne.startsWith("states:")) {
					while((ligne = reader.readLine()) != null) {
						ligne = ligne.replaceAll("\t","").replaceAll(" ",""); // Ignore les tabulations et les espaces
						if(ligne.isEmpty()) { break; }
						if(ligne.contains(";")) { // Si la ligne contient le symbole ';'
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

	public void getTransitionsDepuisFichier(String cheminFichier) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(cheminFichier));
			String ligne;
			while((ligne = reader.readLine()) != null) {
				ligne = ligne.replaceAll("\t","").replaceAll(" ",""); // Ignore les tabulations et les espaces
				if(ligne.startsWith("transitions:")) {
					while((ligne = reader.readLine()) != null) {
						ligne = ligne.replaceAll("\t","").replaceAll(" ",""); // Ignore les tabulations et les espaces
						if(ligne.isEmpty()) { break; }
						if(ligne.contains("->") && ligne.contains(";")) { // Si la ligne contient le symbole '->' et ';'
							String[] tab_transitions = ligne.split(";");
							for(String t : tab_transitions) {
								String[] transition = t.split("->");
								if(transition.length > 1) { // Faut qu'il y ait ou moins 2 cases (source et destination)
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

	public void getLabelsDepuisFichier(String cheminFichier) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(cheminFichier));
			String ligne;
			while((ligne = reader.readLine()) != null) {
				ligne = ligne.replaceAll("\t","").replaceAll(" ",""); // Ignore les tabulations et les espaces
				if(ligne.startsWith("labels:")) {
					while((ligne = reader.readLine()) != null) {
						ligne = ligne.replaceAll("\t","").replaceAll(" ",""); // Ignore les tabulations et les espaces
						if(ligne.isEmpty()) { break; }
						if(ligne.contains(":") && ligne.contains(";")) { // Si la ligne contient le symbole ':' et ';'
							String[] tab_etats_labels = ligne.split(";");
							for(String etat_labels : tab_etats_labels) {
								String[] parts = etat_labels.split(":");
								if(parts.length > 1) { // Faut qu'il y ait ou moins 2 cases (gauche et droite)
									String etat = parts[0]; // ce qu'il y a à gauche
									String[] tab_labels = parts[1].replaceAll("\"","").split(",");
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

	public boolean FichierPeutEtreLu(String cheminFichier) {
		File f = new File(cheminFichier);
		return f.exists() && f.isFile() && f.canRead();
	}

	public void getDonneesDepuisFichier(String cheminFichier) {
		getEtatsDepuisFichier(cheminFichier);
		getTransitionsDepuisFichier(cheminFichier);
		getLabelsDepuisFichier(cheminFichier);	
	}

	public boolean estDejaEvalue(Formule formule) {
		for(String etat : evaluations.keySet()) {
			return evaluations.get(etat).containsKey(formule);
		}
		return false;
	}

	public boolean getEvaluation(String etat, Formule formule) {
		if(evaluations.containsKey(etat)) {
			return evaluations.get(etat).get(formule);
		}
		return false;
	}

	public void setEvaluation(String etat, Formule formule, boolean b) {
		if(evaluations.containsKey(etat)) {
			evaluations.get(etat).put(formule,b);
		} else {
			Map<Formule, Boolean> formules = new HashMap<>();
			formules.put(formule,b);
			evaluations.put(etat,formules);
		}
	}

	public Set<String> getPredecesseurs(String etat) {
		Set<String> resultat = new HashSet<>();
		for(Map.Entry<String, Set<String>> entry : transitions.entrySet()) {
			if(entry.getValue().contains(etat)) { // Si l'état contient un prédécesseur
				resultat.add(entry.getKey()); // On ajoute son prédécesseur
			}
		}
		return resultat;
	}

	public Set<String> getSuccesseurs(String etat) {
		Set<String> resultat = new HashSet<>();
		if(transitions.containsKey(etat)) { // Si l'état contient des successeurs
			resultat = transitions.get(etat); // On récupère ses successeurs
		}
		return resultat;
	}

	public int compteNbOccurrences(String str, char c1) {
		int compteur = 0;
		for(Character c2 : str.toCharArray()) {
			if(c1 == c2) { compteur++; }
		}
		return compteur;
	}

	public int trouverIndex_And_Or_Implies_Equiv(String str) {
		int nbParenthesesOuvertes = 0;
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == '(') { nbParenthesesOuvertes++; }
			else if(str.charAt(i) == ')') { nbParenthesesOuvertes--; }
			else if(nbParenthesesOuvertes == 1 && (str.charAt(i) == '&' || str.charAt(i) == '|' || str.charAt(i) == '>' || str.charAt(i) == '?')) {
				return i;
			}
		}
		return -1;
	}

	public Formule parse(String strFormule) { // Le parseur CTL qui permet de transformer un string en une formule CTL (null = mauvaise syntaxe)
		if(strFormule.isEmpty() || strFormule == null) { return null; }
		else if(ensembleLabels.contains(strFormule)) { // Si c'est une proposition (label reconnnue par l'automate)
			return new PROP(strFormule);
		} else if(strFormule.charAt(0) == '-') { // Si y a un - (NEGATION)
			String strFinFormule = strFormule.substring(1);
			Formule finFormule = parse(strFinFormule);
			if(finFormule == null) { return null; }
			return new FormlOneArg(OneArg.NOT,finFormule); // NOT(finFormule)
		} else if((strFormule.contains("&") || strFormule.contains("|") || strFormule.contains(">") || strFormule.contains("?")) // Si il contient un AND, un OR, un IMPLIES ou un EQUIV
			&& strFormule.charAt(0) == '(' && strFormule.substring(strFormule.length() - 1).charAt(0) == ')' // le symbole '&','|','>' ou le '?' doit obligatoirement être précédé de '(' et suivi de ')'
			&& compteNbOccurrences(strFormule,'(') == compteNbOccurrences(strFormule,')')) {
			int index = trouverIndex_And_Or_Implies_Equiv(strFormule);
			char c = strFormule.charAt(index);
			if(index == -1 || index == 1 || index == strFormule.length() - 2) { return null; } // Si y a rien à gauche ou à droite du symbole ('&','|','>' ou le '?')
			String strGauche = strFormule.substring(1,index);
			String strDroite = strFormule.substring(index + 1,strFormule.length() - 1);
			Formule gauche = parse(strGauche);
			Formule droite = parse(strDroite);
			if(gauche == null || droite == null) { return null; }
			if(c == '&') { return new FormlTwoArg(TwoArg.AND,gauche,droite); } // AND(gauche,droite)
			else if(c == '|') { return new FormlTwoArg(TwoArg.OR,gauche,droite); } // OR(gauche,droite)
			else if(c == '>') { return new FormlTwoArg(TwoArg.IMPLIES,gauche,droite); } // IMPLIES(gauche,droite)
			else if(c == '?') { return new FormlTwoArg(TwoArg.EQUIV,gauche,droite); } // EQUIV(gauche,droite)
		} else if(strFormule.charAt(0) == 'A') { // Si la première lettre est A (POUR TOUT)
			if(strFormule.charAt(1) == 'X') {
				String strFinFormule = strFormule.substring(2);
				Formule finFormule = parse(strFinFormule);
				if(finFormule == null) { return null; }
				return new FormlOneArg(OneArg.AX,finFormule); // AX(finFormule)
			} else if(strFormule.charAt(1) == 'F') {
				String strFinFormule = strFormule.substring(2);
				Formule finFormule = parse(strFinFormule);
				if(finFormule == null) { return null; }
				return new FormlOneArg(OneArg.AF,finFormule); // AF(finFormule)
			} else if(strFormule.charAt(1) == 'G') {
				String strFinFormule = strFormule.substring(2);
				Formule finFormule = parse(strFinFormule);
				if(finFormule == null) { return null; }
				return new FormlOneArg(OneArg.AG,finFormule); // AG(finFormule)
			} else if (strFormule.contains("U")) {
				int indexU = strFormule.indexOf("U");
				if(indexU == 1 || indexU == strFormule.length() - 1) { return null; } // Si y a rien à gauche ou à droite du U
				String strGauche = strFormule.substring(1,indexU);
				String strDroite = strFormule.substring(indexU + 1,strFormule.length());
				Formule gauche = parse(strGauche);
				Formule droite = parse(strDroite);
				if(gauche == null || droite == null) { return null; }
				return new FormlTwoArg(TwoArg.AU,gauche,droite); // AU(gauche,droite)
			}
		} else if(strFormule.charAt(0) == 'E') { // Si la première lettre est E (IL EXISTE)
			if(strFormule.charAt(1) == 'X') {
				String strFinFormule = strFormule.substring(2);
				Formule finFormule = parse(strFinFormule);
				if(finFormule == null) { return null; }
				return new FormlOneArg(OneArg.EX,finFormule); // EX(finFormule)
			} else if(strFormule.charAt(1) == 'F') {
				String strFinFormule = strFormule.substring(2);
				Formule finFormule = parse(strFinFormule);
				if(finFormule == null) { return null; }
				return new FormlOneArg(OneArg.EF,finFormule); // EF(finFormule)
			} else if(strFormule.charAt(1) == 'G') {
				String strFinFormule = strFormule.substring(2);
				Formule finFormule = parse(strFinFormule);
				if(finFormule == null) { return null; }
				return new FormlOneArg(OneArg.EG,finFormule); // EG(finFormule)
			} else if(strFormule.contains("U")) {
				int indexU = strFormule.indexOf("U");
				if(indexU == 1 || indexU == strFormule.length() - 1) { return null; } // Si y a rien à gauche ou à droite du U
				String strGauche = strFormule.substring(1,indexU);
				String strDroite = strFormule.substring(indexU + 1,strFormule.length());
				Formule gauche = parse(strGauche);
				Formule droite = parse(strDroite);
				if(gauche == null || droite == null) { return null; }
				return new FormlTwoArg(TwoArg.EU,gauche,droite); // EU(gauche,droite)
			}
		}
		return null;
	}

	public void afficherEtats() { System.out.println("Voici la liste des etats : " + ensembleEtats); }

	public void afficherTransitions() {
		System.out.println("Voici la liste des transitions : ");
		for(Map.Entry<String, Set<String>> entree : transitions.entrySet()) {
			String e1 = entree.getKey();
			for(String e2 : entree.getValue()) {
				System.out.println(e1 + " -> " + e2);
			}
		}
	}

	public void afficherLabels() {
		System.out.println("Voici la liste des labels : ");
		for(Map.Entry<String, Set<String>> entree : labels.entrySet()) {
			System.out.println("label de l'etat " + entree.getKey() + " : " + entree.getValue());
		}
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

	public void afficherInformations() { // Affiche les états, les transitions et les étiquettes (labels)
		afficherEtats();
		afficherTransitions();
		afficherLabels();
	}

	public boolean verifierFormule(Formule formule) {
		for(Map.Entry<String, Map<Formule, Boolean>> outerMap : evaluations.entrySet()) {
			if(outerMap.getValue().containsKey(formule)) {
				if(outerMap.getValue().get(formule)) {
					return true;
				}
			}
		}
		return false;
	}

	public void marquage(Formule formule) {
		if(!estDejaEvalue(formule)) {
			//System.out.println("Verification " + formule.toString() + " en cours..");

			if(formule instanceof TRUE) { // TRUE : φ = true
				for(String e : ensembleEtats) { // Pour tous les états
					setEvaluation(e,formule,true);
				}
			}

			else if(formule instanceof PROP) { // PROPOSITION : φ = p
				PROP prop = (PROP) formule;
				for(String e : ensembleEtats) { // Pour tous les états
					if(labels.containsKey(e)) { // Si un ou plusieurs label(s) existe(nt) pour cet état
						if(labels.get(e).contains(prop.getNom())) { // si p € L(e)
							setEvaluation(e,formule,true);
						} else {
							setEvaluation(e,formule,false);
						}
					} else { // Si aucun label n'existe pour cet etat
						setEvaluation(e,formule,false);
					}
				}
			}

			else if(formule instanceof FormlOneArg) { // Si c'est une formule avec 1 argument à droite (1 sous-formule)
				FormlOneArg foa = (FormlOneArg) formule;

				if(foa.getType().equals(OneArg.NOT)) { // NEGATION : φ = -φ'
					marquage(foa.getFinFormule()); // On marque φ'
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,!getEvaluation(e,foa.getFinFormule())); // φ == !φ'
					}
				}

				else if(foa.getType().equals(OneArg.EX)) { // IL EXITE NEXT : φ = EXφ'
					marquage(foa.getFinFormule()); // On marque φ'
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,false);
					}
					for(String e : ensembleEtats) { // Pour tous les états
						for(String successeur : getSuccesseurs(e)) { // Pour tous les sucesseurs de cet état
							if(getEvaluation(successeur,foa.getFinFormule())) { // Si successeur.φ' == true
								setEvaluation(e,formule,true);
								break; // On sort de la boucle des successeurs
							}
						}
					}
				}

				else if(foa.getType().equals(OneArg.EF)) { // IL EXITE FUTUR : φ = EFφ'
					marquage(new FormlTwoArg(TwoArg.EU,new TRUE(),foa.getFinFormule())); // On marque l'équivalence EFφ' = E(true U φ')
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,getEvaluation(e,new FormlTwoArg(TwoArg.EU,new TRUE(),foa.getFinFormule())));
					}
				}

				else if(foa.getType().equals(OneArg.EG)) { // IL EXITE FUTUR : φ = EGφ'
					marquage(new FormlOneArg(OneArg.NOT,new FormlOneArg(OneArg.AF,new FormlOneArg(OneArg.NOT,foa.getFinFormule())))); // On marque l'équivalence EGφ' = -(AF-(φ'))
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,getEvaluation(e,new FormlOneArg(OneArg.NOT,new FormlOneArg(OneArg.AF,new FormlOneArg(OneArg.NOT,foa.getFinFormule())))));
					}
				}

				else if(foa.getType().equals(OneArg.AX)) { // POUR TOUT NEXT : φ = AXφ'
					marquage(foa.getFinFormule()); // On marque φ'
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,false);
					}
					for(String e : ensembleEtats) { // Pour tous les états
						boolean resultat = true;
						for(String successeur : getSuccesseurs(e)) { // Pour tous les sucesseurs de cet état
							if(!getEvaluation(successeur,foa.getFinFormule())) { // Si successeur.φ' == false
								resultat = false;
								break; // On sort de la boucle des successeurs
							}
						}
						if(resultat) {
							setEvaluation(e,formule,true);
						}
					}
				}

				else if(foa.getType().equals(OneArg.AF)) { // POUR TOUT FUTUR : φ = AFφ'
					marquage(new FormlTwoArg(TwoArg.AU,new TRUE(),foa.getFinFormule())); // On marque l'équivalence AFφ' = A(true U φ')
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,getEvaluation(e,new FormlTwoArg(TwoArg.AU,new TRUE(),foa.getFinFormule())));
					}
				}

				else if(foa.getType().equals(OneArg.AG)) { // POUR TOUT FUTUR : φ = AGφ'
					marquage(new FormlOneArg(OneArg.NOT,new FormlOneArg(OneArg.EF,new FormlOneArg(OneArg.NOT,foa.getFinFormule())))); // On marque l'équivalence AGφ' = -(EF-(φ'))
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,getEvaluation(e,new FormlOneArg(OneArg.NOT,new FormlOneArg(OneArg.EF,new FormlOneArg(OneArg.NOT,foa.getFinFormule())))));
					}
				}
			}

			else if(formule instanceof FormlTwoArg) { // Si c'est une formule avec 2 arguments [FormuleGauche et FormuleDroite]
				FormlTwoArg fta = (FormlTwoArg) formule;

				if(fta.getType().equals(TwoArg.AND)) { // AND : φ = (φ' & φ'')
					marquage(fta.getGauche()); // On marque φ'
					marquage(fta.getDroite()); // On marque φ''
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,getEvaluation(e,fta.getGauche()) && getEvaluation(e,fta.getDroite()));
					}
				}

				else if(fta.getType().equals(TwoArg.OR)) { // OR : φ = (φ' | φ'')
					marquage(fta.getGauche()); // On marque φ'
					marquage(fta.getDroite()); // On marque φ''
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,getEvaluation(e,fta.getGauche()) || getEvaluation(e,fta.getDroite()));
					}
				}

				else if(fta.getType().equals(TwoArg.EU)) { // IL EXISTE UNTIL : φ = E φ' U φ''
					marquage(fta.getGauche()); // On marque φ'
					marquage(fta.getDroite()); // On marque φ''
					Map<String, Boolean> seenbefore = new HashMap<>(); // chaque état sera associé à un boolean (pour savoir si il est déjà visité)
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,false);
						seenbefore.put(e,false);
					}
					ArrayList<String> L = new ArrayList<>();
					for(String e : ensembleEtats) { // Pour tous les états
						if(getEvaluation(e,fta.getDroite())) { // if s.φ'' == true
							L.add(e);
						}
					}
					while(!L.isEmpty()) {
						String s = L.get(0);
						L.remove(s);
						setEvaluation(s,formule,true);
						for(String predecesseur : getPredecesseurs(s)) { // Pour tous les prédécesseurs de cet état
							if(!seenbefore.get(predecesseur)) {
								seenbefore.put(predecesseur,true);
								if(getEvaluation(predecesseur,fta.getGauche())) { // if s.φ' == true
									L.add(predecesseur);
								}
							}
						}
					}
				}

				else if(fta.getType().equals(TwoArg.AU)) { // POUR TOUT UNTIL : φ = A φ' U φ''
					marquage(fta.getGauche()); // On marque φ'
					marquage(fta.getDroite()); // On marque φ''
					ArrayList<String> L = new ArrayList<>();
					Map<String, Integer> nb = new HashMap<>(); // Chaque état sera associé à son nombre de successeurs
					for(String e : ensembleEtats) { // Pour tous les états
						nb.put(e,getSuccesseurs(e).size());
						setEvaluation(e,formule,false);
						if(getEvaluation(e,fta.getDroite())) { //if s.φ'' == true
							L.add(e);
						}
					}
					while(!L.isEmpty()) {
						String s = L.get(0);
						L.remove(s);
						setEvaluation(s,formule,true);
						for(String predecesseur : getPredecesseurs(s)) { // Pour tous les prédécesseurs de cet état
							int valeurNb = nb.get(predecesseur) - 1;
							nb.put(predecesseur,valeurNb);
							if(valeurNb == 0 && getEvaluation(predecesseur,fta.getGauche()) && !getEvaluation(predecesseur,formule)) {
								L.add(predecesseur);
							}
						}
					}
				}

				else if(fta.getType().equals(TwoArg.IMPLIES)) { // IMPLIES : φ = (φ' => φ'')
					marquage(new FormlTwoArg(TwoArg.OR,new FormlOneArg(OneArg.NOT,fta.getGauche()),fta.getDroite())); // On marque l'équivalence (φ' => φ'') = (-φ' | φ'')
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,getEvaluation(e,new FormlTwoArg(TwoArg.OR,new FormlOneArg(OneArg.NOT,fta.getGauche()),fta.getDroite())));
					}
				}

				else if(fta.getType().equals(TwoArg.EQUIV)) { // EQUIV : φ = (φ' <=> φ'')
					marquage(new FormlTwoArg(TwoArg.AND,new FormlTwoArg(TwoArg.IMPLIES,fta.getGauche(),fta.getDroite()),new FormlTwoArg(TwoArg.IMPLIES,fta.getDroite(),fta.getGauche()))); // On marque l'équivalence (φ' <=> φ'') = (φ' => φ'') AND (φ'' => φ')
					for(String e : ensembleEtats) { // Pour tous les états
						setEvaluation(e,formule,getEvaluation(e,new FormlTwoArg(TwoArg.AND,new FormlTwoArg(TwoArg.IMPLIES,fta.getGauche(),fta.getDroite()),new FormlTwoArg(TwoArg.IMPLIES,fta.getDroite(),fta.getGauche()))));
					}
				}
			}
		}
	}
}
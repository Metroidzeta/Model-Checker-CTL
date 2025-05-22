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
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class KripkeStructure {

	private static final Pattern ESPACES_ET_TABULATIONS = Pattern.compile("[\\t\\s]+");

	private final Set<String> etats; // l'ensemble des états de l'automate
	private final Map<String, Set<String>> transitions; // associe à chaque état -> une ou plusieurs transitions vers d'autres états
	private final Map<String, Set<String>> labels; // associe à chaque état -> un ensemble de propositions atomiques vraies dans l'état
	private final Map<String, Map<Formule, Boolean>> evaluations; // associe à chaque état -> une formule évaluée true ou false

	private Set<String> ensembleLabels; // l'ensemble des labels (étiquettes/propositions atomiques) reconnues par l'automate

	public KripkeStructure() {
		etats = new HashSet<>();
		transitions = new HashMap<>();
		labels = new HashMap<>();
		evaluations = new HashMap<>();
		ensembleLabels = new HashSet<>();
	}

	public void ajouterEtat(String e) {
		if (e == null || e.isBlank()) throw new IllegalArgumentException("Etat null ou vide");
		etats.add(e);
	}

	private void verifierExistenceEtat(String e) {
		if (e == null || e.isBlank()) throw new IllegalArgumentException("Etat null ou vide");
		if (!etats.contains(e)) throw new IllegalArgumentException("Etat inexistant : " + e);
	}

	public void ajouterTransition(String e1, String e2) { // Ajouter une transition : (-> e2) à el
		verifierExistenceEtat(e1);
		verifierExistenceEtat(e2);
		transitions.computeIfAbsent(e1, key -> new HashSet<>()).add(e2); // ajoute la transition à e1
	}

	public void ajouterLabel(String e, String l) { // Ajouter un label dans l'état
		verifierExistenceEtat(e);
		if (l == null || l.isBlank()) throw new IllegalArgumentException("Label null ou vide");
		if (l.matches(".*[A-Z\\-&|>?()]+.*")) {
			throw new IllegalArgumentException("Label interdit : contient des majuscules ou symboles interdits : -, &, |, >, ?, (, )");
		}
		labels.computeIfAbsent(e, key -> new HashSet<>()).add(l); // ajoute le label dans cet état
		ensembleLabels.add(l);
	}

	public Set<String> getEnsembleLabels() { return ensembleLabels; }

	public void ajouterDonneesDepuisFichier(String cheminFichier) {
		Path chemin = Paths.get(cheminFichier);
		try (BufferedReader reader = Files.newBufferedReader(chemin)) {
			String ligne;
			while ((ligne = reader.readLine()) != null) {
				ligne = supprimerEspacesEtTabs(ligne);
				if (ligne.startsWith("states:")) {
					traiterBloc(reader, this::traiterEtats);
				} else if (ligne.startsWith("transitions:")) {
					traiterBloc(reader, this::traiterTransitions);
				} else if (ligne.startsWith("labels:")) {
					traiterBloc(reader, this::traiterLabels);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void traiterBloc(BufferedReader reader, Consumer<String> traitement) throws IOException {
		String ligne;
		while ((ligne = reader.readLine()) != null) {
			ligne = supprimerEspacesEtTabs(ligne);
			if (ligne.isEmpty()) break;
			traitement.accept(ligne);
		}
	}

	private void traiterEtats(String ligne) {
		for (String etat : ligne.split(";")) ajouterEtat(etat);
	}

	private void traiterTransitions(String ligne) {
		for (String t : ligne.split(";")) {
			String[] transition = t.split("->");
			if (transition.length >= 2) {
				ajouterTransition(transition[0], transition[1]); // source -> destination
			}
		}
	}

	private void traiterLabels(String ligne) {
		for (String etatLabels : ligne.split(";")) {
			String[] parts = etatLabels.split(":");
			if (parts.length >= 2) {
				String etat = parts[0];
				String[] labels = parts[1].replace("\"", "").split(",");
				for (String label : labels) ajouterLabel(etat, label);
			}
		}
	}

	private String supprimerEspacesEtTabs(String ligne) {
		return ESPACES_ET_TABULATIONS.matcher(ligne).replaceAll(""); // supprimer tabulations et espaces
	}

	private Set<String> getPredecesseurs(String e) {
		Set<String> predecesseurs = new HashSet<>();
		transitions.forEach((etat, successeurs) -> {
			if (successeurs.contains(e)) predecesseurs.add(etat);
		});
		return predecesseurs;
	}

	private Set<String> getSuccesseurs(String e) {
		return transitions.getOrDefault(e, Set.of());
	}

	private void afficherEtats() { System.out.println("Etats : " + etats); }

	private void afficherTransitions() {
		System.out.println("Transitions : ");
		transitions.forEach((src, dests) -> dests.forEach(dest -> System.out.println(src + " -> " + dest)));
	}

	private void afficherLabels() {
		System.out.println("Labels : ");
		labels.forEach((etat, labs) -> System.out.println(etat + " : " + labs));
	}

	public void afficherInformations() { // affiche la liste des états, transitions et labels de l'automate
		afficherEtats();
		afficherTransitions();
		afficherLabels();
	}

	public void afficherEvaluation(Formule formule) { // afficher l'évaluation de la formule pour chaque état (si elle existe)
		for (String etat : evaluations.keySet()) {
			if (evaluations.get(etat).containsKey(formule)) {
				System.out.println("Pour l'etat " + etat + ", " + formule + " -> " + evaluations.get(etat).get(formule));
			}
		}
	}

	public void afficherEvaluations() { // afficher toutes les évaluations des formules déjà évaluées de chaque état
		for (Map.Entry<String, Map<Formule, Boolean>> outerMap : evaluations.entrySet()) {
			System.out.println("Pour l'etat " +  outerMap.getKey() + " :");
			for (Map.Entry<Formule, Boolean> innerMapEntry : outerMap.getValue().entrySet()) {
				System.out.println(innerMapEntry.getKey() + " -> " + innerMapEntry.getValue());
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

	private void marquerAvecEquivalence(Formule formule, Formule formlEquiv) {
		marquage(formlEquiv); // marque formule équivalente
		etats.forEach(e -> setEvaluation(e, formule, getEvaluation(e, formlEquiv))); // pour tous les états, évalue la formule de base à partir de la formule équivalente
	}

	private void marquerGD(Formule gauche, Formule droite) {
		marquage(gauche);
		marquage(droite);
	}

	private boolean estEvalue(Formule formule) {
		return evaluations.values().stream().anyMatch(map -> map.containsKey(formule));
	}

	public void marquage(Formule formule) {
		if (!estEvalue(formule)) {
			if (formule instanceof FALSE) etats.forEach(e -> setEvaluation(e, formule, false)); // φ = FALSE
			else if (formule instanceof TRUE) etats.forEach(e -> setEvaluation(e, formule, true)); // φ = TRUE
			else if (formule instanceof PROPOSITION) checkPROPOSITION((PROPOSITION) formule); // PROPOSITION : φ = label
			else if (formule instanceof NOT) checkNOT((NOT) formule); // NEGATION : φ = -φ'
			else if (formule instanceof EX) checkEX((EX) formule); // IL EXISTE NEXT : φ = EXφ'
			else if (formule instanceof EF) checkEF((EF) formule); // IL EXISTE FUTUR : φ = EFφ'
			else if (formule instanceof EG) checkEG((EG) formule); // IL EXISTE GLOBAL : φ = EGφ'
			else if (formule instanceof AX) checkAX((AX) formule); // POUR TOUT NEXT : φ = AXφ'
			else if (formule instanceof AF) checkAF((AF) formule); // POUR TOUT FUTUR : φ = AFφ'
			else if (formule instanceof AG) checkAG((AG) formule); // POUR TOUT GLOBAL : φ = AGφ'
			else if (formule instanceof AND) checkAND((AND) formule); // ET : φ = (φ' & φ'')
			else if (formule instanceof OR) checkOR((OR) formule); // OU : φ = (φ' | φ'')
			else if (formule instanceof EU) checkEU((EU) formule); // IL EXISTE UNTIL : φ = E(φ' U φ'')
			else if (formule instanceof AU) checkAU((AU) formule); // POUR TOUT UNTIL : φ = A(φ' U φ'')
			else if (formule instanceof IMPLIES) checkIMPLIES((IMPLIES) formule); // IMPLICATION : φ = (φ' => φ'')
			else if (formule instanceof EQUIV) checkEQUIV((EQUIV) formule); // EQUIVALENCE : φ = (φ' <=> φ'')
		}
	}

	private void checkPROPOSITION(PROPOSITION f_prop) {
		etats.forEach(e -> setEvaluation(e, f_prop,
		labels.containsKey(e) && labels.get(e).contains(f_prop.toString()))); // pour tous les états, si p € L(e) = true, sinon false
	}

	private void checkNOT(NOT f_not) {	
		Formule droite = f_not.getDroite();
		marquage(droite); // On marque φ'
		etats.forEach(e -> setEvaluation(e, f_not, !getEvaluation(e, droite)));
	}

	private void checkEX(EX f_ex) {
		Formule droite = f_ex.getDroite();
		marquage(droite); // On marque φ'
		etats.forEach(e -> setEvaluation(e, f_ex, false)); // pour tous les états : éval φ = false par défaut
		for (String e : etats) {
			for (String successeur : getSuccesseurs(e)) {
				if (getEvaluation(successeur, droite)) { // si successeur.φ' == true
					setEvaluation(e, f_ex, true);
					break; // sortir de la boucle successeurs
				}
			}
		}
	}

	private void checkEF(EF f_ef) {
		Formule droite = f_ef.getDroite();
		marquage(droite); // On marque φ'
		marquerAvecEquivalence(f_ef, new EU(new TRUE(), droite)); // EFφ' = E(trueUφ')
	}

	private void checkEG(EG f_eg) {
		Formule droite = f_eg.getDroite();
		marquage(droite); // On marque φ'
		marquerAvecEquivalence(f_eg, new NOT(new AF(new NOT(droite)))); // EGφ' = -(AF-(φ'))
	}

	private void checkAX(AX f_ax) {
		Formule droite = f_ax.getDroite();
		marquage(droite); // On marque φ'
		etats.forEach(e -> setEvaluation(e, f_ax, false)); // pour tous les états : éval φ = false par défaut
		for (String e : etats) {
			boolean resultat = true;
			for (String successeur : getSuccesseurs(e)) {
				if (!getEvaluation(successeur, droite)) { // si successeur.φ' == false
					resultat = false;
					break; // on sort de la boucle des successeurs
				}
			}
			if (resultat) setEvaluation(e, f_ax, true);
		}
	}

	private void checkAF(AF f_af) {
		Formule droite = f_af.getDroite();
		marquage(droite); // On marque φ'
		marquerAvecEquivalence(f_af, new AU(new TRUE(), droite)); // AFφ' = A(trueUφ')
	}

	private void checkAG(AG f_ag) {
		Formule droite = f_ag.getDroite();
		marquage(droite); // On marque φ'
		marquerAvecEquivalence(f_ag, new NOT(new EF(new NOT(droite)))); // AGφ' = -(EF-(φ'))
	}

	private void checkAND(AND f_and) {
		Formule gauche = f_and.getGauche(), droite = f_and.getDroite();
		marquerGD(gauche, droite); // on marque φ' et φ''
		etats.forEach(e -> setEvaluation(e, f_and, getEvaluation(e, gauche) && getEvaluation(e, droite))); // pour tous les états : éval φ = (φ' & φ'')
	}

	private void checkOR(OR f_or) {
		Formule gauche = f_or.getGauche(), droite = f_or.getDroite();
		marquerGD(gauche, droite); // on marque φ' et φ''
		etats.forEach(e -> setEvaluation(e, f_or, getEvaluation(e, gauche) || getEvaluation(e, droite))); // pour tous les états : éval φ = (φ' | φ'')
	}

	private void checkEU(EU f_eu) {
		Formule gauche = f_eu.getGauche(), droite = f_eu.getDroite();
		marquerGD(gauche, droite); // on marque φ' et φ''
		Map<String, Boolean> seenBefore = new HashMap<>(); // chaque état sera associé à un booléen (si il a déjà été visité)
		for (String e : etats) {
			setEvaluation(e, f_eu, false); // pour tous les états : éval φ = false par défaut
			seenBefore.put(e, false);
		}
		ArrayList<String> L = new ArrayList<>();
		for (String e : etats) {
			if (getEvaluation(e, droite)) { // si s.φ'' == true
				L.add(e);
			}
		}
		while (!L.isEmpty()) {
			String s = L.get(0);
			L.remove(s);
			setEvaluation(s, f_eu, true);
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

	private void checkAU(AU f_au) {
		Formule gauche = f_au.getGauche(), droite = f_au.getDroite();
		marquerGD(gauche, droite); // on marque φ' et φ''
		ArrayList<String> L = new ArrayList<>();
		Map<String, Integer> nb = new HashMap<>();
		for (String e : etats) {
			nb.put(e, getSuccesseurs(e).size()); // chaque état associé à son nombre de successeurs
			setEvaluation(e, f_au, false); // pour tous les états : éval φ = false par défaut
			if (getEvaluation(e, droite)) { // si s.φ'' == true
				L.add(e);
			}
		}
		while (!L.isEmpty()) {
			String s = L.remove(0);
			setEvaluation(s, f_au, true);
			for (String predecesseur : getPredecesseurs(s)) { // pour tous les prédécesseurs de cet état
				int valeurNb = nb.get(predecesseur) - 1;
				nb.put(predecesseur, valeurNb);
				if (valeurNb == 0 && getEvaluation(predecesseur, gauche) && !getEvaluation(predecesseur, f_au)) {
					L.add(predecesseur);
				}
			}
		}
	}

	private void checkIMPLIES(IMPLIES f_implies) {
		Formule gauche = f_implies.getGauche(), droite =  f_implies.getDroite();
		marquerGD(gauche, droite); // on marque φ' et φ''
		marquerAvecEquivalence(f_implies, new OR(new NOT(gauche), droite)); // (φ' => φ'') = (-φ' | φ'')
	}

	private void checkEQUIV(EQUIV f_equiv) {
		Formule gauche = f_equiv.getGauche(), droite = f_equiv.getDroite();
		marquerGD(gauche, droite); // on marque φ' et φ''
		marquerAvecEquivalence(f_equiv, new AND(new IMPLIES(gauche, droite), new IMPLIES(droite, gauche))); // (φ' <=> φ'') = (φ' => φ'') & (φ'' => φ')
	}
}
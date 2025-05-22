/*
 * @author Alain Barbier alias "Metroidzeta"
 *
 * Pour compiler avec Windows, GNU/Linux et MacOS :
 *     > javac formules/unaires/*.java formules/binaires/*.java formules/*.java *.java
 *
 * Pour exécuter :
 *     > java CTLMain
 */

import formules.Formule;

import java.io.File;
import java.util.Scanner;

public class CTLMain {

	private static final String DOSSIER_AUTOMATES = "automates/";
	private static final String EXTENSION = ".txt";

	private static boolean fichierPeutEtreLu(String cheminFichier) {
		File f = new File(cheminFichier);
		return f.exists() && f.isFile() && f.canRead();
	}

	// VERSION MANUELLE DE LA STRUCTURE DE KRIPKE GRAPH1.TXT (A UTILISER EN CAS DE PROBLEME) :
	private static void graph1(KripkeStructure ks) {
		for (int i = 1; i < 9; i++) {
			ks.ajouterEtat(String.format("%d", i));
		}

		ks.ajouterTransition("1", "1");
		ks.ajouterTransition("1", "2");
		ks.ajouterTransition("2", "3");
		ks.ajouterTransition("2", "5");
		ks.ajouterTransition("2", "6");
		ks.ajouterTransition("3", "6");
		ks.ajouterTransition("4", "3");
		ks.ajouterTransition("4", "4");
		ks.ajouterTransition("5", "1");
		ks.ajouterTransition("5", "5");
		ks.ajouterTransition("6", "5");
		ks.ajouterTransition("6", "7");
		ks.ajouterTransition("7", "8");
		ks.ajouterTransition("8", "4");

		ks.ajouterLabel("1", "q");
		ks.ajouterLabel("2", "p");
		ks.ajouterLabel("2", "q");
		ks.ajouterLabel("3", "q");
		ks.ajouterLabel("4", "r");
		ks.ajouterLabel("5", "p");
		ks.ajouterLabel("5", "r");
		ks.ajouterLabel("6", "p");
		ks.ajouterLabel("6", "r");
		ks.ajouterLabel("7", "p");
		ks.ajouterLabel("7", "q");
	}

	private static String demanderFichierValide(Scanner scanner) {
		while (true) {
			System.out.printf("Veuillez entrer le nom du fichier à utiliser dans le dossier \"%s\" (mettre l'extension %s) :%n", DOSSIER_AUTOMATES, EXTENSION);
			String nomFichier = scanner.nextLine();
			String cheminFichier = DOSSIER_AUTOMATES + nomFichier;

			if (fichierPeutEtreLu(cheminFichier)) return cheminFichier;

			System.out.printf("Impossible de lire le fichier \"%s\". Veuillez réessayer.%n", cheminFichier);
		}
	}

	public static void main(String[] args) {
		KripkeStructure ks = new KripkeStructure();

		try (Scanner scanner = new Scanner(System.in)) { // try-with-resources
			String cheminFichier = demanderFichierValide(scanner);
			ks.ajouterDonneesDepuisFichier(cheminFichier); //ou mettre manuellement : graph1(ks);

			System.out.println("Ok! Voici les données reconnues de la structure de Kripke :");
			ks.afficherInformations();

			String saisie;
			Formule f = null;
			CTLParser parser = new CTLParser(ks);
			boolean quitter = false;
			while (!quitter) {
				System.out.print("Veuillez taper votre formule (ou \"fin\" pour quitter) : ");
				saisie = scanner.nextLine(); // On récupère la formule de l'utilisateur
				switch (saisie) {
					case "fin","end" -> quitter = true;
					case "voirDetails" -> {
						if (f != null) ks.afficherEvaluations();
						else System.out.println("Aucune formule n'a été donnée précédemment.");
					}
					default -> {
						f = parser.parse(saisie);
						if (f != null) {
							System.out.println("Vérification de la formule " + f + " en cours...");
							ks.marquage(f);
							System.out.println("Résultats : ");
							ks.afficherEvaluation(f);
							System.out.println("Pour voir les résultats plus en détail, tapez : \"voirDetails\"");
						} else {
							System.out.println("Mauvaise syntaxe ou proposition inconnue");
						}
					}
				}
			}
		}
		System.out.println("Fin du programme");
	}
}
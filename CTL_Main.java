// Projet réalisé par Metroidzeta

import java.io.File;
import java.util.Scanner;

public class CTL_Main {

	public static boolean fichierPeutEtreLu(String cheminFichier) {
		File f = new File(cheminFichier);
		return f.exists() && f.isFile() && f.canRead();
	}

	// VERSION MANUELLE DE L'AUTOMATE GRAPH1.TXT (A UTILISER EN CAS DE PROBLEME) :
	public static void graph1(Automate automate) {
		for(int i = 1; i < 9; i++) {
			automate.ajouterEtat(String.format("%d",i));
		}

		automate.ajouterTransition("1","1");
		automate.ajouterTransition("1","2");
		automate.ajouterTransition("2","3");
		automate.ajouterTransition("2","5");
		automate.ajouterTransition("2","6");
		automate.ajouterTransition("3","6");
		automate.ajouterTransition("4","3");
		automate.ajouterTransition("4","4");
		automate.ajouterTransition("5","1");
		automate.ajouterTransition("5","5");
		automate.ajouterTransition("6","5");
		automate.ajouterTransition("6","7");
		automate.ajouterTransition("7","8");
		automate.ajouterTransition("8","4");

		automate.ajouterLabel("1","q");
		automate.ajouterLabel("2","p");
		automate.ajouterLabel("2","q");
		automate.ajouterLabel("3","q");
		automate.ajouterLabel("4","r");
		automate.ajouterLabel("5","p");
		automate.ajouterLabel("5","r");
		automate.ajouterLabel("6","p");
		automate.ajouterLabel("6","r");
		automate.ajouterLabel("7","p");
		automate.ajouterLabel("7","q");
	}

	public static void main(String[] args) {
		Automate automate = new Automate();
		Scanner scanner = new Scanner(System.in);

		System.out.println("Veuillez entrer le nom du fichier a utiliser dans le dossier \"automates\" (mettre l'extention .txt) : ");
		String nomFichier = scanner.nextLine();
		String cheminFichier = "automates/" + nomFichier;

		while(!fichierPeutEtreLu(cheminFichier)) {
			System.out.println("Impossible de lire le fichier \"" + cheminFichier + "\"");
			System.out.println("Veuillez entrer un nom de fichier valide dans le dossier \"automates\" (mettre l'extention .txt) : ");
			nomFichier = scanner.nextLine();
			cheminFichier = "automates/" + nomFichier;
		}

		automate.ajouterDonneesDepuisFichier(cheminFichier); //graph1(automate);

		System.out.println("Ok! Voici les données reconnues de l'automate :");
		automate.afficherInformations();
		String strFormule;
		Formule f = null;
		boolean quitter = false;
		while(!quitter) {
			System.out.print("Veuillez taper votre formule (ou \"fin\" pour quitter) : ");
			strFormule = scanner.nextLine(); // On récupère la formule de l'utilisateur
			switch(strFormule) {
				case "fin":
				case "end":
					quitter = true;
					break;
				case "voirDetails":
					if(f != null) { automate.afficherToutesEvaluations(); }
					else { System.out.println("Impossible de voir en détails les résultats précédents car aucune formule n'a ete donnée précédemment"); }
					break;
				default:
					f = automate.parse(strFormule);
					if(f != null) {
						System.out.println("Verification de la formule " + f.toString() + " en cours..");
						automate.marquage(f);
						System.out.println("Résultats : ");
						automate.afficherEvaluation(f);
						System.out.println("Si vous souhaitez voir le(s) résultat(s) prédécent(s) plus en détails, tapez : \"voirDetails\"");
					} else {
						System.out.println("Mauvaise Syntaxe, ou proposition inconnue");
					}
					break;
			}
		}
		System.out.println("Fin du programme");
	}
}
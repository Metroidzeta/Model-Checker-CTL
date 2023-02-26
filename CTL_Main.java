// Projet réalisé par Metroidzeta

import java.util.Scanner;

public class CTL_Main {
	public static void main(String[] args) {
		Automate a = new Automate();
		Scanner scanner = new Scanner(System.in);

		System.out.println("Veuillez entrer le nom du fichier a utiliser dans le dossier \"automates\" (mettre l'extention .txt) : ");
		String nomFichier = scanner.nextLine();
		String cheminFichier = "automates/" + nomFichier;

		while(!a.FichierPeutEtreLu(cheminFichier)) {
			System.out.println("Impossible de lire le fichier \"" + cheminFichier + "\"");
			System.out.println("Veuillez entrer un nom de fichier valide dans le dossier \"automates\" (mettre l'extention .txt) : ");
			nomFichier = scanner.nextLine();
			cheminFichier = "automates/" + nomFichier;
		}

		a.getDonneesDepuisFichier(cheminFichier);

		/* VERSION MANUELLE DE L'AUTOMATE GRAPH1.TXT (A UTILISER EN CAS DE PROBLEME) :

		for(int i = 1; i < 9; i++) {
			a.ajouterEtat(String.format("%d",i));
		}

		a.ajouterTransition("1","1");
		a.ajouterTransition("1","2");
		a.ajouterTransition("2","3");
		a.ajouterTransition("2","5");
		a.ajouterTransition("2","6");
		a.ajouterTransition("3","6");
		a.ajouterTransition("4","3");
		a.ajouterTransition("4","4");
		a.ajouterTransition("5","1");
		a.ajouterTransition("5","5");
		a.ajouterTransition("6","5");
		a.ajouterTransition("6","7");
		a.ajouterTransition("7","8");
		a.ajouterTransition("8","4");

		a.ajouterLabel("1","q");
		a.ajouterLabel("2","p");
		a.ajouterLabel("2","q");
		a.ajouterLabel("3","q");
		a.ajouterLabel("4","r");
		a.ajouterLabel("5","p");
		a.ajouterLabel("5","r");
		a.ajouterLabel("6","p");
		a.ajouterLabel("6","r");
		a.ajouterLabel("7","p");
		a.ajouterLabel("7","q"); */

		System.out.println("Ok! Voici les données reconnues de l'automate :");
		a.afficherInformations();
		String strFormule;
		boolean premiereFormuleValide = false;
		while(true) {
			System.out.print("Veuillez taper votre formule (\"fin\" pour quitter) : ");
			strFormule = scanner.nextLine(); // On récupère la formule de l'utilisateur
			if(strFormule.equals("fin") || strFormule.equals("end")) { break; } // Fin du programme
			else if(strFormule.equals("voirDetails")) { // Voir les détails 
				if(premiereFormuleValide) { a.afficherToutesEvaluations(); }
				else { System.out.println("Impossible de voir en détails les résultats précédents car aucune formule n'a ete donnée précédemment"); }
			}
			else {
				Formule f = a.parse(strFormule);
				if(f != null) {
					System.out.println("Verification de la formule " + f.toString() + " en cours..");
					a.marquage(f);
					System.out.println("Résultat : ");
					a.afficherEvaluation(f);
					/*if(a.verifierFormule(f)) {
						System.out.println(f.toString() + " -> vrai, car contient ou moins un etat où cela est verifié");
					} else {
						System.out.println(f.toString() + " -> faux, car ne contient pas d'etat ou cela est verifié");
					} */
					System.out.println("Si vous souhaitez voir le(s) résultat(s) prédécent(s) plus en détails, tapez : \"voirDetails\"");
					premiereFormuleValide = true;
				}
				else { System.out.println("Mauvaise Syntaxe, ou proposition inconnue"); }
			}
		}
		System.out.println("Fin du programme");
	}
}
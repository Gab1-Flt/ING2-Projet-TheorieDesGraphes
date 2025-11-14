import java.util.Scanner;

public class AffichageMenu {
    public void affichage(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Qui etes-vous ? \n(C) : Collectivité\n(E) : Entreprise");
        String choix1 = sc.nextLine();
        String choix2;
        String choix3;
        String choix4;
        int erreur = 0;

        do{
            erreur = 0;
            if(choix1.equals("C")){
                System.out.println("Que voulez-vous faire :");
                System.out.println("1. Soumettre le plan de la commune.");
                System.out.println("2. Signaler une modification de circulation.");
                System.out.println("3. Indiquer des contraintes horaire.");
                System.out.println("4. Consulter la quantité de déchets récoltés.");
                System.out.println("5. Gérer les demandes d'encombrants.");
                choix2 = sc.nextLine();

                if(choix2.equals("1")){
                    //Rentrer un nouveau graphe dans le programme
                }
                else if(choix2.equals("2")){
                    //Modifier un graphe ? Permettre l'ajout ou la modification des rues (sens unique, travaux temporaires, etc.) ?
                }
                else if(choix2.equals("3")){
                    //Spécifier des contraintes de temps à respecter pour certaines zones (En fonction du poids ?)
                }
                else if(choix2.equals("4")){
                    //Afficher les données d'après les collectes précédentes s'il y en a eu une
                }
                else if(choix2.equals("5")){

                }
                else {
                    System.out.println("Veuillez choisir une choix valide.");
                    erreur = 1;
                    break;
                }
            }
            else if(choix1.equals("E")){
                System.out.println("Que voulez-vous faire :");
                System.out.println("1. Calculer un itinéraire.");
                System.out.println("2. Planifier les jours de collectes.");
                choix2 = sc.nextLine();

                if (choix2.equals("1")) {
                    System.out.println("Quel itinéraire voulez-vous calculer :");
                    System.out.println("1. Calculer l'itinéraire pour les encombrants.");
                    System.out.println("2. Calculer l'Itinéraire de collecte des poubelles.");
                    System.out.println("3. Calculer l'Itinéraire pour les points de collecte");
                    choix3 = sc.nextLine();

                    if (choix3.equals("1")) {
                        System.out.println("Pour combien de particuliers voulez-vous calculer la tournée:");
                        System.out.println("1. Itinéraire le plus court pour un seul particulier.");
                        System.out.println("2. Itinéraire le plus court pour une tournée de plusieurs particuliers.");
                        choix4 = sc.nextLine();

                        if (choix4.equals("1")) {

                        }
                        else if (choix4.equals("2")) {

                        }
                        else {
                            System.out.println("Veuillez choisir une choix valide.");
                            erreur = 1;
                            break;
                        }
                    }
                    else if (choix3.equals("2")) {

                    }
                    else if (choix3.equals("3")) {
                        System.out.println("Quelle approche voulez-vous utiliser pour calculer la tournée :");
                        System.out.println("1. Plus Proche Voisin.");
                        System.out.println("2. MST.");

                    }
                    else {
                        System.out.println("Veuillez choisir une choix valide.");
                        erreur = 1;
                        break;
                    }
                }
                else if (choix2.equals("2")) {
                    System.out.println("Quel type de collecte :");
                    System.out.println("1. Sans contrainte de capacité.");
                    System.out.println("2. Avec contrainte de capacité et nombre de camions.");
                    choix3 = sc.nextLine();

                    if (choix3.equals("1")) {

                    }
                    else if (choix3.equals("2")) {

                    }
                    else {
                        System.out.println("Veuillez choisir une choix valide.");
                        erreur = 1;
                        break;
                    }
                }
                else {
                    System.out.println("Veuillez choisir une choix valide.");
                    erreur = 1;
                    break;
                }
            }
            else if(choix1.equals("T")){
                //affichage Teste
            }
        }while(erreur==1);


    }
}

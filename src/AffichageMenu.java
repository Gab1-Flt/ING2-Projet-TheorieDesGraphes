import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AffichageMenu {
    public void affichage(){
        Scanner sc = new Scanner(System.in);
        int backMenu = 0;
        Reseau PlanVille = null;

        do{
            System.out.println("Qui etes-vous ? \n(C) : Collectivité\n(E) : Entreprise\n(Q) : Quitter");
            String choix1 = sc.nextLine();
            String choix2;
            String choix3;
            String choix4;


            backMenu = 0;
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

                    //Affichage du plan (Test pour le moment)
                    System.out.println("\n-------------------------------");
                    PlanVille = Plan.load("test.txt");
                    System.out.println("Le plan a été enrergistré.");
                    System.out.println("-------------------------------\n");
                    backMenu = 1;
                }
                else if(choix2.equals("2")){
                    //Modifier un graphe ? Permettre l'ajout ou la modification des rues (sens unique, travaux temporaires, etc.) ?
                    System.out.println("Que voulez-vous signaler :");
                    System.out.println("1. Route barrée.");
                    System.out.println("2. Ajout d'une nouvelle rue.");
                    choix3 = sc.nextLine();

                    if(choix3.equals("1")){
                        //supprimer la rue
                        System.out.println("Indiquer le nom de la rue barrée :");
                        choix4 = sc.nextLine().trim();
                        PlanVille.bloquerRue(choix4); //Bloquage de la rue
                        backMenu = 1;
                    }
                    else if(choix3.equals("2")){
                        //Ajouter la rue (Dans un sens ou deux)
                        System.out.println("Indiquer le nom de la Rue :");
                        String nomRue = sc.nextLine().trim();
                        System.out.println("Indiquer le nom de l'intersection 1 :");
                        String nomIntersection1 = sc.nextLine().trim();
                        System.out.println("Indiquer le nom de l'intersection 2 :");
                        String nomIntersection2 = sc.nextLine().trim();
                        System.out.println("Indiquer la longueur de la rue : ");
                        double distanceRue = Double.parseDouble(sc.nextLine());

                        List<String> numeros = new ArrayList<>();
                        System.out.println("Numéros d'adresse (tapez ENTER pour finir) : ");
                        while (true) {
                            System.out.print("→ ");
                            String num = sc.nextLine().trim();
                            if (num.isEmpty()) break;
                            numeros.add(num);
                        }

                        PlanVille.ajouterRue(nomRue,nomIntersection1,nomIntersection2,distanceRue,numeros);
                        System.out.println("Rue \"" + nomRue + "\" ajoutée entre " + nomIntersection1 + " et " + nomIntersection2);
                        backMenu = 1;
                    }
                    else {
                        System.out.println("Veuillez choisir une choix valide.");
                        backMenu = 1;
                        break;
                    }
                }
                else if(choix2.equals("3")){
                    //Spécifier des contraintes de temps à respecter pour certaines zones (En fonction du poids ?)
                }
                else if(choix2.equals("4")){
                    //Afficher les données d'après les collectes précédentes s'il y en a eu une
                }
                else if(choix2.equals("5")){
                    //Gérer les demandes d'encombrants -> Permettre l'enregistrement des demandes individuelles
                }
                else {
                    System.out.println("Veuillez choisir une choix valide.");
                    backMenu = 1;
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
                            if(PlanVille != null){
                                //SUPPOSER QUE L'INTERSECTION DE DEPART EST TOUJOURS INT_01 !!!
                                Intersection intersectionDepart = PlanVille.getReseauIntersections().get("Int_01");

                                System.out.println("Saisir le nom de la rue du particulier : ");
                                String nomRue = sc.nextLine().trim();
                                System.out.println("Saisir le numero d'adresse du particulier :");
                                String numAdresse = sc.nextLine().trim();

                                Itineraire itineraire = PlanVille.itineraireVersAdresse(intersectionDepart,nomRue,numAdresse);
                                System.out.println("Voici l'itinéraire le plus court pour se rendre " + numAdresse + " " + nomRue + " :");
                                System.out.println(itineraire);
                            }
                            else {
                                System.out.println("Veuillez enregister le plan d'une ville d'abord.");
                            }
                            backMenu = 1;
                        }
                        else if (choix4.equals("2")) {
                            //Faire liste d'adresse
                            //Faire djikstra depuis le départ pour chaque adresse
                            //Trouver le plus proche du point de départ
                            //Recommencer djikstra pour chaque adresse restante en prenant la dernière adresse comme départ
                            // et trouver le plus court
                            //Recommencer jusqu'a reconstituer la liste
                            if(PlanVille != null){
                                Intersection intersectionDepart = null;

                                List<String> numeros = new ArrayList<>();
                                List<String> nomRue = new ArrayList<>();
                                System.out.println("Entrez le numéros d'adresse et le nom de Rue (tapez ENTER pour finir) : ");
                                while (true || (numeros.size() == 10)) {
                                    System.out.print("Numéro → ");
                                    String num = sc.nextLine().trim();
                                    System.out.println("Nom -> ");
                                    String nom = sc.nextLine().trim();
                                    if (num.isEmpty() || nom.isEmpty()) break;
                                    numeros.add(num);
                                    nomRue.add(nom);
                                }

                                List<Itineraire> itineraires = new ArrayList<>();
                                double distance = 0.0;
                                for(int j=0;j < numeros.size();j++) {
                                    for (int i = 0; i < numeros.size(); i++) {
                                        if (j==0){
                                            //SUPPOSER QUE L'INTERSECTION DE DEPART EST TOUJOURS INT_01 !!!
                                            intersectionDepart = PlanVille.getReseauIntersections().get("Int_01");
                                        }
                                        else {
                                            intersectionDepart = itineraires.get(j-1).getArrivee();
                                        }
                                        Itineraire itineraire = PlanVille.itineraireVersAdresse(intersectionDepart, numeros.get(i), nomRue.get(i));
                                        if (itineraire.getDistanceTotale() < distance || distance == 0.0) {
                                            distance = itineraire.getDistanceTotale();
                                            itineraires.set(j,itineraire);
                                        }
                                    }
                                }

                                System.out.println("Voici l'itinéraire le plus court pour la tournée des particuliers : ");
                                System.out.println(itineraires);
                            }
                            else {
                                System.out.println("Veuillez enregister le plan d'une ville d'abord.");
                            }
                            backMenu = 1;
                        }
                        else {
                            System.out.println("Veuillez choisir une choix valide.");
                            backMenu = 1;
                        }
                    }
                    else if (choix3.equals("2")) {
                        //calcul de l'itinéraire de collecte pour les poubelles aux pieds des habitations
                        //Doit passer par toutes les habitations de toutes les rues dans chaque sens
                        //Rendre le graphe Eulerien ??
                    }
                    else if (choix3.equals("3")) {
                        System.out.println("Quelle approche voulez-vous utiliser pour calculer la tournée :");
                        System.out.println("1. Plus Proche Voisin.");
                        System.out.println("2. MST.");
                        choix4 = sc.nextLine();

                        if(choix4.equals("1")) {

                        }
                        else if(choix4.equals("2")) {

                        }
                        else {
                            System.out.println("Veuillez choisir une choix valide.");
                            backMenu = 1;
                            break;
                        }
                    }
                    else {
                        System.out.println("Veuillez choisir une choix valide.");
                        backMenu = 1;
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
                        backMenu = 1;
                        break;
                    }
                }
                else {
                    System.out.println("Veuillez choisir une choix valide.");
                    backMenu = 1;
                    break;
                }
            } else if (choix1.equals("Q")) {
                break;
            }
            else if(choix1.equals("T")){
                //affichage Teste
                PlanVille = Plan.load("test.txt");

                System.out.println("\n-------------------------------");
                //Affichage du plan (Test pour le moment)
                System.out.println("Nombre d'intersections: " + PlanVille.getReseauIntersections().size());
                for (Intersection i : PlanVille.getReseauIntersections().values()) {
                    System.out.println("-> Intersection : " + i.getNom() + "| -> Nom de rue : " + i.getArcsSortants());
                }

                System.out.println("\nLe plan a été enrergistré.");
                System.out.println("-------------------------------\n");
            }
            else if (choix1.equals("T2")){
                PlanVille = Plan.load("test.txt");

                Intersection intersectionDepart = PlanVille.getReseauIntersections().get("Int_01");
                Intersection intersectionArrivee = PlanVille.getReseauIntersections().get("Int_13");

                Itineraire itineraire = PlanVille.djikstra(intersectionDepart, intersectionArrivee);
                System.out.println(itineraire);

                System.out.println("Indiquer le nom de la rue barrée :");
                String choix = "Rue de la Fontaine";
                PlanVille.bloquerRue(choix); //Bloquage de la rue

                itineraire = PlanVille.djikstra(intersectionDepart, intersectionArrivee);
                System.out.println(itineraire);
            }
        }while(backMenu==1);

    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AffichageMenu {

    private Reseau PlanVille = null;
    private final Scanner sc = new Scanner(System.in);

    public void affichage() {
        int continuer;
        do {
            continuer = menuPrincipal();
        } while (continuer == 1);
    }

    private int menuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("(C) Collectivité");
        System.out.println("(E) Entreprise");
        System.out.println("(Q) Quitter");
        String choix = sc.nextLine();

        switch (choix) {
            case "C" -> { menuCollectivite(); return 1; }
            case "E" -> { menuEntreprise();  return 1; }
            case "Q" -> { return 0; }
            case "T" -> { test1(); return 1; }
            case "T2" ->{test2(); return 1; }
            default   -> {
                System.out.println("Choix invalide.");
                return 1;
            }
        }
    }



    //Menu collecivite :
    private void menuCollectivite() {
        System.out.println("\n--- COLLECTIVITÉ ---");
        System.out.println("1. Soumettre le plan de la commune.");
        System.out.println("2. Signaler une modification de circulation.");
        System.out.println("3. Indiquer des contraintes horaire.");
        System.out.println("4. Consulter la quantité de déchets récoltés.");
        System.out.println("5. Gérer les demandes d'encombrants.");
        String choix = sc.nextLine();

        switch (choix) {
            case "1" -> soumettrePlan();
            case "2" -> modifierCirculation();
            case "3" -> contraintesHoraires();
            case "4" -> consulterDechets();
            case "5" -> gererEncombrants();
            default -> System.out.println("Choix invalide.");
        }
    }

    private void soumettrePlan() {
        //Rentrer un nouveau graphe dans le programme

        //Affichage du plan (Test pour le moment)
        System.out.println("\n-------------------------------");
        PlanVille = Plan.load("test.txt");
        System.out.println("Le plan a été enrergistré.");
        System.out.println("-------------------------------\n");

    }

    private void modifierCirculation() {
        //Modifier un graphe ? Permettre l'ajout ou la modification des rues (sens unique, travaux temporaires, etc.) ?
        System.out.println("Que voulez-vous signaler :");
        System.out.println("1. Route barrée.");
        System.out.println("2. Ajout d'une nouvelle rue.");
        String choix = sc.nextLine();

        switch (choix) {
            case "1" -> {
                //supprimer la rue
                System.out.println("Indiquer le nom de la rue barrée :");
                String choix2 = sc.nextLine().trim();
                PlanVille.bloquerRue(choix2); //Bloquage de la rue
            }
            case "2" -> {
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
            }
            default -> System.out.println("Choix invalide.");
        }

    }

    private void contraintesHoraires() {
        System.out.println("(non implémenté)");
    }

    private void consulterDechets() {
        System.out.println("(non implémenté)");
    }

    private void gererEncombrants() {
        System.out.println("(non implémenté)");
    }



    //Menu Entreprise :
    private void menuEntreprise() {
        System.out.println("\n--- ENTREPRISE ---");
        System.out.println("1. Calculer un itinéraire");
        System.out.println("2. Planifier les jours de collectes");
        String choix = sc.nextLine();

        switch (choix) {
            case "1" -> calculItineraires();
            case "2" -> planningCollectes();
            default -> System.out.println("Choix invalide.");
        }
    }

    private void calculItineraires() {
        System.out.println("Quel itinéraire voulez-vous calculer :");
        System.out.println("1. Calculer l'itinéraire pour les encombrants.");
        System.out.println("2. Calculer l'Itinéraire de collecte des poubelles.");
        System.out.println("3. Calculer l'Itinéraire pour les points de collecte");
        String choix = sc.nextLine();

        switch (choix) {
            case "1" -> itineraireEncombrant();
            case "2" -> itinerairePoubelles();
            case "3" -> itinerairePointDeCollecte();
            default -> System.out.println("Choix invalide.");
        }
    }

    private void itineraireEncombrant() {
        System.out.println("Pour combien de particuliers voulez-vous calculer la tournée:");
        System.out.println("1. Itinéraire le plus court pour un seul particulier.");
        System.out.println("2. Itinéraire le plus court pour une tournée de plusieurs particuliers.");
        String choix = sc.nextLine();

        switch (choix) {
            case "1" ->{
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
            }
            case "2" ->{
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
            }
            default -> System.out.println("Choix invalide.");
        }
    }

    private void itinerairePoubelles() {
        //calcul de l'itinéraire de collecte pour les poubelles aux pieds des habitations
        //Doit passer par toutes les habitations de toutes les rues dans chaque sens
        //Rendre le graphe Eulerien ??
        System.out.println("(non implémenté)");
    }

    private void itinerairePointDeCollecte(){
        System.out.println("Quelle approche voulez-vous utiliser pour calculer la tournée :");
        System.out.println("1. Plus Proche Voisin.");
        System.out.println("2. MST.");
        String choix = sc.nextLine();

        switch (choix) {
            case "1" ->{

            }
            case "2" ->{
            }
            default -> System.out.println("Choix invalide.");
        }

    }

    private void planningCollectes(){
        System.out.println("Quel type de collecte :");
        System.out.println("1. Sans contrainte de capacité.");
        System.out.println("2. Avec contrainte de capacité et nombre de camions.");
        String choix = sc.nextLine();

        switch (choix) {
            case "1" ->{

            }
            case "2" ->{
            }
            default -> System.out.println("Choix invalide.");
        }
    }

    private void test1(){
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

    private void test2(){
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

    public void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

}

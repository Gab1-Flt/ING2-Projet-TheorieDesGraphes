import java.util.ArrayList;
import java.util.Collections;
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

                    Itineraire itineraire = PlanVille.itineraireVersAdresse(intersectionDepart,numAdresse,nomRue);
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
                if (PlanVille == null) {
                    System.out.println("Veuillez enregistrer le plan d'une ville d'abord.");
                    return;
                }

                Intersection depot = PlanVille.getReseauIntersections().get("Int_01");

                List<String> numeros = new ArrayList<>();
                List<String> rues = new ArrayList<>();
                System.out.println("Entrez les adresses (max 10 — ENTER pour finir) :");

                while (numeros.size() < 10) {
                    System.out.print("Numéro → ");
                    String num = sc.nextLine().trim();
                    if (num.isEmpty()) break;
                    System.out.print("Rue → ");
                    String rue = sc.nextLine().trim();
                    if (rue.isEmpty()) break;
                    numeros.add(num);
                    rues.add(rue);
                }

                if (numeros.isEmpty()) {
                    System.out.println("Aucune adresse saisie.");
                    return;
                }

                // 1️⃣ Créer la liste des sommets (dépôt + adresses)
                List<Intersection> sommets = new ArrayList<>();
                sommets.add(depot);

                for (int i = 0; i < numeros.size(); i++) {
                    Adresse ad = PlanVille.getAdresse(numeros.get(i), rues.get(i));
                    if (ad == null) {
                        System.out.println("Adresse introuvable : " + numeros.get(i) + " " + rues.get(i));
                        return;
                    }
                    // On choisit l'intersection la plus proche pour chaque adresse
                    Arc arc = ad.getAr();
                    sommets.add(arc.getDepart()); // tu peux choisir arc.getArrivee() si besoin
                }

                int n = sommets.size();
                double[][] distances = new double[n][n];

                // 2️⃣ Calculer toutes les distances entre sommets
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (i == j) distances[i][j] = 0;
                        else {
                            Itineraire it = PlanVille.djikstra(sommets.get(i), sommets.get(j));
                            if (it == null) distances[i][j] = Double.POSITIVE_INFINITY;
                            else distances[i][j] = it.getDistanceTotale();
                        }
                    }
                }

                // 3️⃣ Appliquer l'heuristique Plus Proche Voisin
                List<Integer> ordreVisite = new ArrayList<>();
                boolean[] visite = new boolean[n];
                int actuel = 0; // départ
                ordreVisite.add(actuel);
                visite[actuel] = true;

                for (int k = 1; k < n; k++) {
                    double minDist = Double.POSITIVE_INFINITY;
                    int prochain = -1;
                    for (int i = 1; i < n; i++) { // on ne considère pas le dépôt
                        if (!visite[i] && distances[actuel][i] < minDist) {
                            minDist = distances[actuel][i];
                            prochain = i;
                        }
                    }
                    if (prochain == -1) {
                        System.out.println("Impossible de calculer une tournée complète, certaines adresses sont inaccessibles.");
                        return;
                    }
                    ordreVisite.add(prochain);
                    visite[prochain] = true;
                    actuel = prochain;
                }

                // 4️⃣ Reconstituer les itinéraires réels
                List<Itineraire> itineraireTournée = new ArrayList<>();
                for (int i = 0; i < ordreVisite.size() - 1; i++) {
                    Itineraire it = PlanVille.djikstra(
                            sommets.get(ordreVisite.get(i)),
                            sommets.get(ordreVisite.get(i + 1))
                    );
                    itineraireTournée.add(it);
                }

                // 5️⃣ Affichage
                System.out.println("\n--- Tournée optimisée (Plus Proche Voisin) ---");
                for (Itineraire it : itineraireTournée) {
                    System.out.println(it);
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

        //System.out.println("Indiquer le nom de la rue barrée :");
        String choix = "Rue de la Fontaine";
        //PlanVille.bloquerRue(choix); //Bloquage de la rue

        itineraire = PlanVille.itineraireVersAdresse(intersectionDepart, "6", choix);
        System.out.println(itineraire);
    }

    public void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

}

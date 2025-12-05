import java.io.*;
import java.util.*;

public class AffichageMenu {

    private Reseau PlanVille = null;
    private final Scanner sc = new Scanner(System.in);
    private GrapheSecteurs grapheSecteurs = null;
    protected List<Adresse> adressesPtsCollecte = new ArrayList<>();

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
        System.out.println("3. Consulter la quantité de déchets récoltés.");
        System.out.println("4. Situer les points de collecte.");
        String choix = sc.nextLine();

        switch (choix) {
            case "1" -> soumettrePlan();
            case "2" -> modifierCirculation();
            case "3" -> consulterDechets();
            case "4" -> SituerPtsDeCollecte();
            default -> System.out.println("Choix invalide.");
        }
    }

    private void soumettrePlan() {
        //Rentrer un nouveau graphe dans le programme

        //Affichage du plan (Test pour le moment)
        System.out.println("\n-------------------------------");
        PlanVille = Plan.load("Plan.txt");
        System.out.println("Le plan a été enregistré.");
        System.out.println("-------------------------------\n");

    }

    private void modifierCirculation() {
        //Modifier un graphe ? Permettre l'ajout ou la modification des rues (sens unique, travaux temporaires, etc.) ?
        System.out.println("\nQue voulez-vous signaler :");
        System.out.println("1. Route barrée.");
        System.out.println("2. Ajout d'une nouvelle rue.");
        String choix = sc.nextLine();

        switch (choix) {
            case "1" -> {
                //supprimer la rue
                System.out.println("\nIndiquer le nom de la rue barrée :");
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

    private void consulterDechets() {
        if(grapheSecteurs == null){
            System.out.println("Effectuez une tournée avant de consulter la quantité de déchets récoltée");
            return;
        }
        grapheSecteurs.afficherQuantiteCollecteeDetaillee(capaciteCamion, nbCamions);
    }

    private void SituerPtsDeCollecte() {
        //Definir la liste d'adresse des pts de collecte

        List<String> numeros = new ArrayList<>();
        List<String> rues = new ArrayList<>();
        System.out.println("Entrez les adresses:");

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

        for (int i = 0; i < numeros.size(); i++) {
            Adresse ad = PlanVille.getAdresse(numeros.get(i), rues.get(i));
            if (ad != null) adressesPtsCollecte.add(ad);
        }

        System.out.println("Adresses des points de collecte enregistrées !");
    }



    //Menu Entreprise :
    private void menuEntreprise() {
        System.out.println("\n--- ENTREPRISE ---");
        System.out.println("1. Calculer un itinéraire");
        System.out.println("2. Calculer les jours de collectes");
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

                    System.out.println("Saisir le numero d'adresse du particulier :");
                    String numAdresse = sc.nextLine().trim();
                    System.out.println("Saisir le nom de la rue du particulier : ");
                    String nomRue = sc.nextLine().trim();

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
                if (PlanVille != null) {
                    Intersection depot = PlanVille.getReseauIntersections().get("Int_01");
                    if (depot == null) {
                        System.out.println("Impossible de trouver l'intersection de départ.");
                        return;
                    }

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

                    List<Adresse> adresses = new ArrayList<>();
                    for (int i = 0; i < numeros.size(); i++) {
                        Adresse ad = PlanVille.getAdresse(numeros.get(i), rues.get(i));
                        if (ad != null) adresses.add(ad);
                    }

                    List<Itineraire> tournee = PlanVille.itineraireVersListAdresseTSP(depot, adresses);

                    if (tournee != null) {
                        System.out.println("Itinéraire de la tournée :");
                        for (Itineraire it : tournee) {
                            System.out.println(it);
                        }
                    }

                } else {
                    System.out.println("Veuillez enregistrer le plan d'une ville d'abord.");
                }
            }
            default -> System.out.println("Choix invalide.");
        }
    }

    private void itinerairePoubelles() {
        //calcul de l'itinéraire de collecte pour les poubelles aux pieds des habitations
        //Doit passer par toutes les habitations de toutes les rues dans chaque sens
        //Rendre le graphe Eulerien ??
        Intersection depot = PlanVille.getReseauIntersections().get("Int_01");
        List<Arc> chemin = PlanVille.euler(depot);
        Itineraire it = new Itineraire (depot,depot,chemin);
        System.out.println(it);
    }

    private void itinerairePointDeCollecte(){
        System.out.println("Quelle approche voulez-vous utiliser pour calculer la tournée :");
        System.out.println("1. Plus Proche Voisin.");
        System.out.println("2. MST.");
        String choix = sc.nextLine();

        switch (choix) {
            case "1" ->{
                if(PlanVille != null) {
                    if(adressesPtsCollecte == null) {
                        System.out.println("Veuillez enregistrer des points de collectes");
                    }
                    else {
                        //Plus proche voisin (TSP)
                        Intersection depot = PlanVille.getReseauIntersections().get("Int_01");
                        List<Itineraire> tournee = PlanVille.itineraireVersListAdresseTSP(depot, adressesPtsCollecte);
                        if (tournee != null) {
                            System.out.println("Itinéraire de la tournée :");
                            for (Itineraire it : tournee) {
                                System.out.println(it);
                            }
                        }
                    }
                }
            }
            case "2" ->{
                    //MST
                    Adresse depot = PlanVille.getAdresse("10", "Rue de la République");
                    if (depot == null) {
                        System.out.println("Adresse de dépôt introuvable (10 Rue de la République).");
                        return;
                    }
                    List<Itineraire> tournee=PlanVille.tourneeMST(depot,adressesPtsCollecte);
                    if (tournee != null) {
                        System.out.println("Itinéraire de la tournée :");
                        for (Itineraire it : tournee) {
                            System.out.println(it);
                        }
                    }
            }
            default -> System.out.println("Choix invalide.");
        }
    }

    private void planningCollectes(){
        System.out.println("\nQuel type de collecte :");
        System.out.println("1. Sans contrainte de capacité.");
        System.out.println("2. Avec contrainte de capacité et nombre de camions.");
        String choix = sc.nextLine();

        if (grapheSecteurs == null) {
            grapheSecteurs = creerGrapheSecteursDepuisFichier("test.txt");
        }

        switch (choix) {
            case "1" ->planningSansCapacites();
            case "2" ->planningAvecCapacites();
            default -> System.out.println("\nChoix invalide.");
        }
    }

    private void planningSansCapacites(){
        System.out.println("\n--- Hypothèse 1 : sans contraintes de capacité ---");

        int nbJours = grapheSecteurs.colorierWelshPowell();
        System.out.println("\nNombre de jours utilisés : " + nbJours);

        grapheSecteurs.afficherPlanningSimple();
    }

    int capaciteCamion = 0;
    int nbCamions = 0;

    private void planningAvecCapacites(){
        System.out.println("\n--- Hypothèse 2 : avec capacités ---");

        try {
            System.out.print("\nCapacité d'un camion (C) : ");
            capaciteCamion = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Nombre de camions disponibles (N) : ");
            nbCamions = Integer.parseInt(sc.nextLine().trim());

            int nbJours = grapheSecteurs.planifierAvecCapacitesEtDivision(capaciteCamion, nbCamions);
            System.out.println("\nNombre de jours utilisés : " + nbJours);

            grapheSecteurs.afficherPlanningAvecCapacitesEtDivision(capaciteCamion, nbCamions);
        } catch (NumberFormatException e) {
            System.out.println("\nValeur incorrecte, veuillez entrer des nombres entiers.");
        }
    }

    //CONSTRUCTION DU GRAPHE DE SECTEURS (THÈME 3)

    private String determinerSecteurPourIntersection(String nomIntersection) {
        try {
            int underscore = nomIntersection.indexOf('_');
            String numStr = nomIntersection.substring(underscore + 1);
            int num = Integer.parseInt(numStr);

            if (num >= 1 && num <= 4) {
                return "S1";
            } else if (num <= 8) {
                return "S2";
            } else if (num <= 12) {
                return "S3";
            } else {
                return "S4";
            }
        } catch (Exception e) {
            return "S1";
        }
    }

     // - On estime la quantité de déchets par secteur via le nombre d'adresses.
     // - On crée une arête entre deux secteurs si une rue relie des intersections de secteurs différents.

    private GrapheSecteurs creerGrapheSecteursDepuisFichier(String nomFichier) {
        GrapheSecteurs g = new GrapheSecteurs();

        Map<String, Integer> quantiteParSecteur = new HashMap<>();
        Map<String, Set<String>> voisinages = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                // NomRue;Int1;Int2;Distance;Ad1;Ad2;...
                if (parts.length < 5) continue;

                String inter1 = parts[1].trim();
                String inter2 = parts[2].trim();

                String s1 = determinerSecteurPourIntersection(inter1);
                String s2 = determinerSecteurPourIntersection(inter2);

                int nbAdresses = parts.length - 4;

                quantiteParSecteur.merge(s1, nbAdresses, Integer::sum);
                quantiteParSecteur.merge(s2, nbAdresses, Integer::sum);

                if (!s1.equals(s2)) {
                    voisinages.computeIfAbsent(s1, k -> new HashSet<>()).add(s2);
                    voisinages.computeIfAbsent(s2, k -> new HashSet<>()).add(s1);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur en lisant le fichier : " + e.getMessage());
        }

        // Création des 4 secteurs S1..S4
        for (String id : new String[]{"S1", "S2", "S3", "S4"}) {
            int q = quantiteParSecteur.getOrDefault(id, 0);
            g.ajouterSecteur(id, "Secteur " + id, q);
        }

        // Ajout des voisinages entre secteurs
        for (String s1 : voisinages.keySet()) {
            for (String s2 : voisinages.get(s1)) {
                if (s1.compareTo(s2) < 0) {
                    g.ajouterVoisinage(s1, s2);
                }
            }
        }

        return g;
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

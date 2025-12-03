import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*; // List, ArrayList, Map, HashMap, Set, HashSet, Scanner...

public class AffichageMenu {

    private Reseau PlanVille = null;
    private GrapheSecteurs grapheSecteurs = null;  // Pour le thème 3
    private final Scanner sc = new Scanner(System.in);

    public void affichage() {
        int continuer;
        do {
            continuer = menuPrincipal();
        } while (continuer == 1);
    }

    // ===================== MENU PRINCIPAL =====================

    private int menuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("(C) Collectivité");
        System.out.println("(E) Entreprise");
        System.out.println("(Q) Quitter");
        String choix = sc.nextLine().trim().toUpperCase();

        switch (choix) {
            case "C" -> { menuCollectivite(); return 1; }
            case "E" -> { menuEntreprise();  return 1; }
            case "Q" -> { return 0; }
            case "T" -> { test1(); return 1; }
            case "T2" ->{ test2(); return 1; }
            default   -> {
                System.out.println("Choix invalide.");
                return 1;
            }
        }
    }

    // ===================== MENU COLLECTIVITÉ =====================

    private void menuCollectivite() {
        System.out.println("\n--- COLLECTIVITÉ ---");
        System.out.println("1. Soumettre le plan de la commune.");
        System.out.println("2. Signaler une modification de circulation.");
        System.out.println("3. Indiquer des contraintes horaires.");
        System.out.println("4. Consulter la quantité de déchets récoltés.");
        System.out.println("5. Gérer les demandes d'encombrants.");
        String choix = sc.nextLine().trim();

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
        // Charger un nouveau graphe (plan de la ville)
        System.out.println("\n-------------------------------");
        PlanVille = Plan.load("test.txt");
        System.out.println("Le plan a été enregistré.");
        System.out.println("-------------------------------\n");
    }

    private void modifierCirculation() {
        System.out.println("Que voulez-vous signaler :");
        System.out.println("1. Route barrée.");
        System.out.println("2. Ajout d'une nouvelle rue.");
        String choix = sc.nextLine().trim();

        switch (choix) {
            case "1" -> {
                System.out.println("Indiquer le nom de la rue barrée :");
                String choix2 = sc.nextLine().trim();
                if (PlanVille != null) {
                    PlanVille.bloquerRue(choix2);
                } else {
                    System.out.println("Veuillez d'abord enregistrer le plan de la ville.");
                }
            }
            case "2" -> {
                if (PlanVille == null) {
                    System.out.println("Veuillez d'abord enregistrer le plan de la ville.");
                    return;
                }
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

                PlanVille.ajouterRue(nomRue, nomIntersection1, nomIntersection2, distanceRue, numeros);
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

    // ===================== MENU ENTREPRISE =====================

    private void menuEntreprise() {
        System.out.println("\n--- ENTREPRISE ---");
        System.out.println("1. Calculer un itinéraire");
        System.out.println("2. Planifier les jours de collectes");
        String choix = sc.nextLine().trim();

        switch (choix) {
            case "1" -> calculItineraires();
            case "2" -> planningCollectes();
            default -> System.out.println("Choix invalide.");
        }
    }

    private void calculItineraires() {
        System.out.println("Quel itinéraire voulez-vous calculer :");
        System.out.println("1. Calculer l'itinéraire pour les encombrants.");
        System.out.println("2. Calculer l'itinéraire de collecte des poubelles.");
        System.out.println("3. Calculer l'itinéraire pour les points de collecte");
        String choix = sc.nextLine().trim();

        switch (choix) {
            case "1" -> itineraireEncombrant();
            case "2" -> itinerairePoubelles();
            case "3" -> itinerairePointDeCollecte();
            default -> System.out.println("Choix invalide.");
        }
    }

    // ===================== ENCOMBRANTS =====================

    private void itineraireEncombrant() {
        System.out.println("Pour combien de particuliers voulez-vous calculer la tournée:");
        System.out.println("1. Itinéraire le plus court pour un seul particulier.");
        System.out.println("2. Itinéraire le plus court pour une tournée de plusieurs particuliers.");
        String choix = sc.nextLine().trim();

        switch (choix) {
            case "1" -> {
                if (PlanVille != null) {
                    // SUPPOSER QUE L'INTERSECTION DE DEPART EST TOUJOURS INT_01 !!!
                    Intersection intersectionDepart = PlanVille.getReseauIntersections().get("Int_01");

                    System.out.println("Saisir le nom de la rue du particulier : ");
                    String nomRue = sc.nextLine().trim();
                    System.out.println("Saisir le numero d'adresse du particulier :");
                    String numAdresse = sc.nextLine().trim();

                    Itineraire itineraire = PlanVille.itineraireVersAdresse(intersectionDepart, numAdresse, nomRue);

                    System.out.println("Voici l'itinéraire le plus court pour se rendre " + numAdresse + " " + nomRue + " :");
                    System.out.println(itineraire);
                } else {
                    System.out.println("Veuillez enregistrer le plan d'une ville d'abord.");
                }
            }
            case "2" -> {
                if (PlanVille != null) {
                    Intersection intersectionDepart;

                    List<String> numeros = new ArrayList<>();
                    List<String> nomsRue = new ArrayList<>();

                    System.out.println("Entrez les numéros d'adresse et les noms de rue (tapez ENTER sur un numéro pour finir) : ");

                    while (numeros.size() < 10) { // limite à 10 adresses max
                        System.out.print("Numéro → ");
                        String num = sc.nextLine().trim();
                        if (num.isEmpty()) break;  // fin de saisie

                        System.out.print("Nom de la rue → ");
                        String nom = sc.nextLine().trim();
                        if (nom.isEmpty()) {
                            System.out.println("Rue vide → fin de la saisie.");
                            break;
                        }

                        numeros.add(num);
                        nomsRue.add(nom);
                    }

                    if (numeros.isEmpty()) {
                        System.out.println("Aucune adresse saisie.");
                        return;
                    }

                    // Liste des itinéraires choisis
                    List<Itineraire> itineraires = new ArrayList<>();

                    // on part toujours de Int_01
                    intersectionDepart = PlanVille.getReseauIntersections().get("Int_01");

                    // on va gérer une "liste" d'indices restants (adresses non encore visitées)
                    List<Integer> indicesRestants = new ArrayList<>();
                    for (int i = 0; i < numeros.size(); i++) {
                        indicesRestants.add(i);
                    }

                    while (!indicesRestants.isEmpty()) {
                        Itineraire meilleur = null;
                        double meilleureDistance = Double.POSITIVE_INFINITY;
                        int meilleurIndex = -1;

                        // on cherche le plus proche parmi les adresses restantes
                        for (int idx : indicesRestants) {
                            String num = numeros.get(idx);
                            String nomRue = nomsRue.get(idx);

                            Itineraire it = PlanVille.itineraireVersAdresse(intersectionDepart, num, nomRue);

                            if (it == null) {
                                System.out.println("Adresse introuvable : " + num + " " + nomRue);
                                continue;
                            }

                            double d = it.getDistanceTotale();
                            if (d < meilleureDistance) {
                                meilleureDistance = d;
                                meilleur = it;
                                meilleurIndex = idx;
                            }
                        }

                        if (meilleur == null) {
                            System.out.println("Plus aucune adresse atteignable, fin de la tournée.");
                            break;
                        }

                        itineraires.add(meilleur);
                        intersectionDepart = meilleur.getArrivee();
                        indicesRestants.remove(Integer.valueOf(meilleurIndex));
                    }

                    // affichage de la tournée complète
                    System.out.println("Voici l'itinéraire le plus court pour la tournée des particuliers : ");
                    double distanceTotale = 0.0;
                    for (Itineraire it : itineraires) {
                        System.out.println(it);
                        distanceTotale += it.getDistanceTotale();
                    }
                    System.out.println("Distance totale de la tournée : " + distanceTotale + " m");

                } else {
                    System.out.println("Veuillez enregistrer le plan d'une ville d'abord.");
                }
            }
        }
    }

            private void itinerairePoubelles() {
        System.out.println("(non implémenté)");
    }

    private void itinerairePointDeCollecte() {
        if (PlanVille == null) {
            System.out.println("Veuillez d'abord enregistrer le plan de la ville.");
            return;
        }

        // On crée l'outil TSP pour les points de collecte
        TSPPointsCollecte tsp = new TSPPointsCollecte(PlanVille);

        // Pour l'instant on considère que tous les sommets sont des points de collecte
        List<Intersection> points = tsp.getPointsDeCollecte();
        if (points == null || points.isEmpty()) {
            System.out.println("Aucun point de collecte défini.");
            return;
        }

        // On suppose le dépôt = Int_01 (comme ailleurs dans ton code)
        Intersection depot = PlanVille.getReseauIntersections().get("Int_01");
        if (depot == null) {
            System.out.println("Impossible de trouver l'intersection de dépôt Int_01.");
            return;
        }

        System.out.println("Quelle approche voulez-vous utiliser pour calculer la tournée :");
        System.out.println("1. Plus Proche Voisin.");
        System.out.println("2. MST.");
        String choix = sc.nextLine().trim();

        List<Intersection> ordre;

        switch (choix) {
            case "1" -> {
                ordre = tsp.tourneePlusProcheVoisin(depot, points);
                System.out.println("\n--- Tournée (Plus Proche Voisin) ---");
            }
            case "2" -> {
                ordre = tsp.tourneeMST(depot, points);
                System.out.println("\n--- Tournée (MST) ---");
            }
            default -> {
                System.out.println("Choix invalide.");
                return;
            }
        }

        // Affichage détaillé des tronçons avec djikstra + distance totale
        double distanceTotale = 0.0;
        for (int i = 0; i < ordre.size() - 1; i++) {
            Intersection a = ordre.get(i);
            Intersection b = ordre.get(i + 1);

            Itineraire it = PlanVille.djikstra(a, b);
            System.out.println(it);
            distanceTotale += it.getDistanceTotale();
        }

        System.out.println("Distance totale estimée de la tournée : " + distanceTotale + " m");
    }

    // ===================== THÈME 3 : PLANIFICATION DES COLLECTES =====================

    private void planningCollectes() {
        if (grapheSecteurs == null) {
            System.out.println("Création du graphe de secteurs à partir du fichier des rues (test.txt)...");
            grapheSecteurs = creerGrapheSecteursDepuisFichier("test.txt");
            System.out.println("Graphe de secteurs créé.");
        }

        System.out.println("\n--- PLANIFIER LES COLLECTES (THÈME 3) ---");
        System.out.println("1. Sans contrainte de capacité (Hypothèse 1).");
        System.out.println("2. Avec contrainte de capacité et nombre de camions (Hypothèse 2).");
        String choix = sc.nextLine().trim();

        switch (choix) {
            case "1" -> planningSansCapacites();
            case "2" -> planningAvecCapacites();
            default  -> System.out.println("Choix invalide.");
        }
    }

    private void planningSansCapacites() {
        System.out.println("\n--- Hypothèse 1 : sans contraintes de capacité ---");

        int nbJours = grapheSecteurs.colorierWelshPowell();
        System.out.println("Nombre de jours utilisés : " + nbJours);

        grapheSecteurs.afficherPlanningSimple();
    }

    private void planningAvecCapacites() {
        System.out.println("\n--- Hypothèse 2 : avec capacités ---");

        try {
            System.out.print("Capacité d'un camion (C) : ");
            int capaciteCamion = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Nombre de camions disponibles (N) : ");
            int nbCamions = Integer.parseInt(sc.nextLine().trim());

            int nbJours = grapheSecteurs.planifierAvecCapacites(capaciteCamion, nbCamions);
            System.out.println("Nombre de jours utilisés : " + nbJours);

            grapheSecteurs.afficherPlanningAvecCapacites(capaciteCamion, nbCamions);
        } catch (NumberFormatException e) {
            System.out.println("Valeur incorrecte, veuillez entrer des nombres entiers.");
        }
    }

    // ===================== CONSTRUCTION DU GRAPHE DE SECTEURS (THÈME 3) =====================

    /**
     * Associe une intersection (Int_01, Int_02, ...) à un secteur S1..S4.
     * Découpage simple :
     *  Int_01 à Int_04 → S1
     *  Int_05 à Int_08 → S2
     *  Int_09 à Int_12 → S3
     *  Int_13 à Int_16 → S4
     */
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

    /**
     * Construit un graphe de secteurs S1..S4 à partir du fichier des rues.
     * - On estime la quantité de déchets par secteur via le nombre d'adresses.
     * - On crée une arête entre deux secteurs si une rue relie des intersections de secteurs différents.
     */
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

    // ===================== TESTS =====================

    private void test1() {
        PlanVille = Plan.load("test.txt");

        System.out.println("\n-------------------------------");
        System.out.println("Nombre d'intersections: " + PlanVille.getReseauIntersections().size());
        for (Intersection i : PlanVille.getReseauIntersections().values()) {
            System.out.println("-> Intersection : " + i.getNom() + " | -> Nom de rue : " + i.getArcsSortants());
        }

        System.out.println("\nLe plan a été enregistré.");
        System.out.println("-------------------------------\n");
    }

    private void test2() {
        PlanVille = Plan.load("test.txt");

        Intersection intersectionDepart = PlanVille.getReseauIntersections().get("Int_01");
        Intersection intersectionArrivee = PlanVille.getReseauIntersections().get("Int_13");

        Itineraire itineraire = PlanVille.djikstra(intersectionDepart, intersectionArrivee);
        System.out.println(itineraire);

        String choix = "Rue de la Fontaine";
        PlanVille.bloquerRue(choix);

        itineraire = PlanVille.djikstra(intersectionDepart, intersectionArrivee);
        System.out.println(itineraire);
    }

    public void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

}

import java.util.*;

public class GrapheSecteurs {

    private final Map<String, Secteur> secteurs = new HashMap<>();
    private Map<Integer, List<Part>> dernierPlanningAvecParts = new HashMap<>();

    // classe interne simple pour représenter une part (ajoutez-la dans GrapheSecteurs)
    public static class Part {
        public final String secteurId;
        public final int quantite;
        public final boolean partielle; // true si c'est une part du secteur

        public Part(String secteurId, int quantite, boolean partielle) {
            this.secteurId = secteurId;
            this.quantite = quantite;
            this.partielle = partielle;
        }

        @Override
        public String toString() {
            return secteurId;
        }
    }

    public Secteur ajouterSecteur(String id, String nom, int quantite) {
        Secteur s = new Secteur(id, nom, quantite);
        secteurs.put(id, s);
        return s;
    }

    public Secteur getSecteur(String id) {
        return secteurs.get(id);
    }

    public void ajouterVoisinage(String id1, String id2) {
        Secteur s1 = secteurs.get(id1);
        Secteur s2 = secteurs.get(id2);
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Secteur inconnu: " + id1 + " ou " + id2);
        }
        s1.ajouterVoisin(s2);
        s2.ajouterVoisin(s1);
    }

    public Collection<Secteur> getSecteurs() {
        return secteurs.values();
    }

    //HYPOTHÈSE 1 : Welsh-Powell (coloration)

    public int colorierWelshPowell() {
        // reset
        for (Secteur s : secteurs.values()) {
            s.setJour(-1);
        }

        // trier par degré décroissant
        List<Secteur> liste = new ArrayList<>(secteurs.values());
        liste.sort((a, b) -> Integer.compare(b.getVoisins().size(), a.getVoisins().size()));

        int jourCourant = 0;

        for (Secteur s : liste) {
            if (s.getJour() != -1) continue;

            // nouveau jour
            s.setJour(jourCourant);

            // essayer de mettre d'autres sommets sur ce jour
            for (Secteur autre : liste) {
                if (autre.getJour() == -1 && peutAffecterJour(autre, jourCourant)) {
                    autre.setJour(jourCourant);
                }
            }

            jourCourant++;
        }

        return jourCourant;
    }

    public void afficherQuantiteCollecteeDetaillee(int capaciteCamion, int nbCamions) {
        if (dernierPlanningAvecParts.isEmpty() || capaciteCamion == 0 || nbCamions == 0) {
            System.out.println("Aucune tournée effectuée");
            return;
        }

        int capaciteMaxParJour = capaciteCamion * nbCamions;
        System.out.println("\n--- Quantités collectées par jour ---");

        List<Integer> jours = new ArrayList<>(dernierPlanningAvecParts.keySet());
        Collections.sort(jours);

        int total = 0;
        for (int j : jours) {
            int somme = 0;
            System.out.print("Jour " + (j + 1));
            for (Part p : dernierPlanningAvecParts.get(j)) {
                somme += p.quantite;
            }
            total += somme;
            System.out.println(" | Collecté = " + somme + " kg" );
        }

        System.out.println("\n>>> Quantité totale collectée (tous jours) : " + total + " kg");
    }


    private boolean peutAffecterJour(Secteur s, int jour) {
        for (Secteur v : s.getVoisins()) {
            if (v.getJour() == jour) return false;
        }
        return true;
    }

    public void afficherPlanningSimple() {
        System.out.println("\n---Planning (sans capacités)---");
        Map<Integer, List<Secteur>> parJour = new HashMap<>();
        for (Secteur s : secteurs.values()) {
            parJour.computeIfAbsent(s.getJour(), k -> new ArrayList<>()).add(s);
        }
        List<Integer> jours = new ArrayList<>(parJour.keySet());
        Collections.sort(jours);
        for (int j : jours) {
            System.out.print("Jour " + (j + 1) + " : ");
            for (Secteur s : parJour.get(j)) {
                System.out.print(s.getId() + " ");
            }
            System.out.println();
        }
    }

    //HYPOTHÈSE 2 : avec capacités

    public int planifierAvecCapacitesEtDivision(int capaciteCamion, int nbCamions) {
        if (capaciteCamion <= 0 || nbCamions <= 0) {
            throw new IllegalArgumentException("Capacités invalides");
        }
        int capaciteMaxParJour = capaciteCamion * nbCamions;

        // quantité restante par secteur
        Map<Secteur, Integer> restant = new HashMap<>();
        for (Secteur s : secteurs.values()) {
            s.setJour(-1);
            restant.put(s, s.getQuantite());
        }

        dernierPlanningAvecParts.clear();
        Map<Integer, Integer> chargeParJour = new HashMap<>();
        Map<Integer, Set<Secteur>> secteursParJour = new HashMap<>();

        int nbJours = 0;
        boolean reste = true;

        while (reste) {
            reste = false;
            // tri décroissant par quantité restante
            List<Secteur> liste = new ArrayList<>(restant.keySet());
            liste.sort((a, b) -> Integer.compare(restant.get(b), restant.get(a)));

            for (Secteur s : liste) {
                int r = restant.get(s);
                if (r <= 0) continue;
                reste = true;

                // tester jours existants
                for (int jour = 0; jour < nbJours && r > 0; jour++) {
                    int chargeActuelle = chargeParJour.getOrDefault(jour, 0);
                    int disponible = capaciteMaxParJour - chargeActuelle;
                    if (disponible <= 0) continue;

                    // vérifier voisins
                    Set<Secteur> assignes = secteursParJour.getOrDefault(jour, Collections.emptySet());
                    boolean voisinSurJour = false;
                    for (Secteur v : s.getVoisins()) {
                        if (assignes.contains(v)) { voisinSurJour = true; break; }
                    }
                    if (voisinSurJour) continue;

                    int part = Math.min(r, disponible);
                    boolean estPartielle = part < s.getQuantite();

                    dernierPlanningAvecParts.computeIfAbsent(jour, k -> new ArrayList<>())
                            .add(new Part(s.getId(), part, estPartielle));

                    chargeParJour.put(jour, chargeActuelle + part);
                    secteursParJour.computeIfAbsent(jour, k -> new HashSet<>()).add(s);
                    if (s.getJour() == -1) s.setJour(jour);

                    r -= part;
                }

                // si il reste encore de la quantité, créer de nouveaux jours jusqu'à tout placer
                while (r > 0) {
                    int part = Math.min(r, capaciteMaxParJour);
                    boolean estPartielle = part < s.getQuantite();

                    dernierPlanningAvecParts.computeIfAbsent(nbJours, k -> new ArrayList<>())
                            .add(new Part(s.getId(), part, estPartielle));

                    chargeParJour.put(nbJours, chargeParJour.getOrDefault(nbJours, 0) + part);
                    Set<Secteur> set = secteursParJour.computeIfAbsent(nbJours, k -> new HashSet<>());
                    set.add(s);
                    if (s.getJour() == -1) s.setJour(nbJours);

                    r -= part;
                    nbJours++;
                }

                restant.put(s, r);
            }
        }

        return nbJours;
    }

    public void afficherPlanningAvecCapacitesEtDivision(int capaciteCamion, int nbCamions) {
        int capaciteMaxParJour = capaciteCamion * nbCamions;
        System.out.println("\n---Planning (avec capacités, secteurs fractionnés si nécessaire)---");
        List<Integer> jours = new ArrayList<>(dernierPlanningAvecParts.keySet());
        Collections.sort(jours);

        for (int j : jours) {
            List<Part> liste = dernierPlanningAvecParts.get(j);
            int charge = 0;
            System.out.print("Jour " + (j + 1) + " : ");
            for (Part p : liste) {
                System.out.print(p.toString() + " ");
                charge += p.quantite; // pas de parsing de string, on utilise la structure
            }
            System.out.println(" | Charge totale = " + charge + " / " + capaciteMaxParJour);
        }
    }

}
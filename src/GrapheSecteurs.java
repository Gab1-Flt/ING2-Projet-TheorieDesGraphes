import java.util.*;

public class GrapheSecteurs {

    private final Map<String, Secteur> secteurs = new HashMap<>();

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

    private boolean peutAffecterJour(Secteur s, int jour) {
        for (Secteur v : s.getVoisins()) {
            if (v.getJour() == jour) return false;
        }
        return true;
    }

    public void afficherPlanningSimple() {
        System.out.println("---Planning (sans capacités)---");
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

    public int planifierAvecCapacites(int capaciteCamion, int nbCamions) {
        if (capaciteCamion <= 0 || nbCamions <= 0) {
            throw new IllegalArgumentException("Capacités invalides");
        }

        int capaciteMaxParJour = capaciteCamion * nbCamions;

        // reset jours
        for (Secteur s : secteurs.values()) {
            s.setJour(-1);
        }

        // trier par quantité décroissante
        List<Secteur> liste = new ArrayList<>(secteurs.values());
        liste.sort((a, b) -> Integer.compare(b.getQuantite(), a.getQuantite()));

        Map<Integer, Integer> chargeParJour = new HashMap<>();
        int nbJours = 0;

        for (Secteur s : liste) {
            boolean affecte = false;

            // essayer sur les jours existants
            for (int jour = 0; jour < nbJours; jour++) {
                int chargeActuelle = chargeParJour.getOrDefault(jour, 0);
                if (chargeActuelle + s.getQuantite() <= capaciteMaxParJour
                        && peutAffecterJourAvecVoisins(s, jour)) {
                    s.setJour(jour);
                    chargeParJour.put(jour, chargeActuelle + s.getQuantite());
                    affecte = true;
                    break;
                }
            }

            // sinon, créer un nouveau jour
            if (!affecte) {
                int nouveauJour = nbJours;
                s.setJour(nouveauJour);
                chargeParJour.put(nouveauJour, s.getQuantite());
                nbJours++;
            }
        }

        return nbJours;
    }

    private boolean peutAffecterJourAvecVoisins(Secteur s, int jour) {
        for (Secteur v : s.getVoisins()) {
            if (v.getJour() == jour) return false;
        }
        return true;
    }

    public void afficherPlanningAvecCapacites(int capaciteCamion, int nbCamions) {
        int capaciteMaxParJour = capaciteCamion * nbCamions;

        System.out.println("---Planning (avec capacités)---");
        Map<Integer, List<Secteur>> parJour = new HashMap<>();
        for (Secteur s : secteurs.values()) {
            parJour.computeIfAbsent(s.getJour(), k -> new ArrayList<>()).add(s);
        }
        List<Integer> jours = new ArrayList<>(parJour.keySet());
        Collections.sort(jours);

        for (int j : jours) {
            List<Secteur> liste = parJour.get(j);
            int charge = 0;
            System.out.print("Jour " + (j + 1) + " : ");
            for (Secteur s : liste) {
                System.out.print(s.getId() + "(q=" + s.getQuantite() + ") ");
                charge += s.getQuantite();
            }
            System.out.println(" | Charge totale = " + charge + " / " + capaciteMaxParJour);
        }
    }
}

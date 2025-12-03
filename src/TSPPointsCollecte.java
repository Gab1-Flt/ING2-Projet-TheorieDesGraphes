import java.util.*;
public class TSPPointsCollecte {

    private final Reseau reseau;

    public TSPPointsCollecte(Reseau reseau) {
        this.reseau = reseau;
    }

    public List<Intersection> getPointsDeCollecte() {
        return new ArrayList<>(reseau.getReseauIntersections().values());
    }

    // Approche 1 : + proche voisin

    public List<Intersection> tourneePlusProcheVoisin(Intersection depot, List<Intersection> points) {
        List<Intersection> nonVisites = new ArrayList<>(points);
        nonVisites.remove(depot);

        List<Intersection> ordre = new ArrayList<>();
        Intersection courant = depot;
        ordre.add(courant);

        while (!nonVisites.isEmpty()) {
            Intersection meilleur = null;
            double meilleureDistance = Double.POSITIVE_INFINITY;

            for (Intersection cible : nonVisites) {
                Itineraire it = reseau.djikstra(courant, cible);
                double d = it.getDistanceTotale();
                if (d < meilleureDistance) {
                    meilleureDistance = d;
                    meilleur = cible;
                }
            }
            ordre.add(meilleur);
            courant = meilleur;
            nonVisites.remove(meilleur);
        }
        ordre.add(depot);
        return ordre;
    }

    // Approche 2 : MST + DFS

    public List<Intersection> tourneeMST(Intersection depot, List<Intersection> points) {
        if (!points.contains(depot)) {
            points = new ArrayList<>(points);
            points.add(0, depot);
        }
        // matrice des distances entre points (via djikstra)
        Map<Intersection, Map<Intersection, Double>> dist = matriceDistances(points);

        // MST avec Prim
        Map<Intersection, List<Intersection>> mstAdj = new HashMap<>();
        for (Intersection i : points) {
            mstAdj.put(i, new ArrayList<>());
        }

        Set<Intersection> inMST = new HashSet<>();
        Map<Intersection, Double> key = new HashMap<>();
        Map<Intersection, Intersection> parent = new HashMap<>();

        for (Intersection i : points) {
            key.put(i, Double.POSITIVE_INFINITY);
        }
        key.put(depot, 0.0);

        while (inMST.size() < points.size()) {
            Intersection u = null;
            double best = Double.POSITIVE_INFINITY;
            for (Intersection v : points) {
                if (!inMST.contains(v) && key.get(v) < best) {
                    best = key.get(v);
                    u = v;
                }
            }
            inMST.add(u);

            if (parent.get(u) != null) {
                Intersection p = parent.get(u);
                mstAdj.get(p).add(u);
                mstAdj.get(u).add(p);
            }

            for (Intersection v : points) {
                if (!inMST.contains(v)) {
                    double dv = dist.get(u).get(v);
                    if (dv < key.get(v)) {
                        key.put(v, dv);
                        parent.put(v, u);
                    }
                }
            }
        }
        // Parcours DFS sur le MST pour obtenir un ordre de visite
        List<Intersection> ordre = new ArrayList<>();
        Set<Intersection> visite = new HashSet<>();
        dfsMST(depot, mstAdj, visite, ordre);
        ordre.add(depot);
        return ordre;
    }

    private Map<Intersection, Map<Intersection, Double>> matriceDistances(List<Intersection> points) {
        Map<Intersection, Map<Intersection, Double>> dist = new HashMap<>();

        for (Intersection i : points) {
            dist.put(i, new HashMap<>());
            dist.get(i).put(i, 0.0);
        }

        for (int a = 0; a < points.size(); a++) {
            Intersection i = points.get(a);
            for (int b = a + 1; b < points.size(); b++) {
                Intersection j = points.get(b);
                Itineraire it = reseau.djikstra(i, j);
                double d = it.getDistanceTotale();
                dist.get(i).put(j, d);
                dist.get(j).put(i, d);
            }
        }
        return dist;
    }

    private void dfsMST(Intersection courant,
                        Map<Intersection, List<Intersection>> adj,
                        Set<Intersection> visite,
                        List<Intersection> ordre) {
        visite.add(courant);
        ordre.add(courant);
        for (Intersection v : adj.get(courant)) {
            if (!visite.contains(v)) {
                dfsMST(v, adj, visite, ordre);
            }
        }
    }
}

import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class Reseau {
    private Map<String, Intersection> ReseauRues = new HashMap<>();
    private Map<String, Adresse> adressesParCle = new HashMap<>();
    private Map<String, Arc> IntersectionsDecouvertes = new HashMap<>();

    private String cleAdresse(String numero, String rue) {
        return numero.trim() + "|" + rue.trim().toLowerCase();
    }

    public void indexerAdresse(Adresse a) {
        String numero = a.getNumero();
        String rue = a.getAr().getRue();
        adressesParCle.put(cleAdresse(numero, rue), a);
    }

    public Adresse getAdresse(String numero, String rue) {
        return adressesParCle.get(cleAdresse(numero, rue));
    }

    Intersection intersection(String nom){
        return ReseauRues.computeIfAbsent(nom, Intersection::new);
    }

    public List<Arc> getArcsParRue(String nomRue) {
        List<Arc> resultat = new ArrayList<>();
        String cible = nomRue.trim().toLowerCase();

        for (Intersection inter : ReseauRues.values()) {
            for (Arc arc : inter.getArcsSortants()) {
                if (arc.getRue().trim().toLowerCase().equals(cible)) {
                    resultat.add(arc);
                }
            }
        }
        return resultat;
    }

    Arc addArcOriente(String depart, String arrivee, String NomRue, double distance, List<Adresse> adresses){
        Intersection d = intersection(depart);
        Intersection a = intersection(arrivee);
        Arc e = new Arc(d, a, NomRue, distance, adresses);
        d.getArcsSortants().add(e);
        return e;
    }

    public Map<String, Intersection> getReseauRues(){
        return ReseauRues;
    }

    public Intersection getIntersection(String nom) {
        return ReseauRues.get(nom);
    }

    public Itineraire djikstra(Intersection depart, Intersection destination) {

        if (depart == null || destination == null) {
            return null;
        }

        if (depart.equals(destination)) {
            return new Itineraire(depart, destination, new ArrayList<Arc>());
        }

        Map<String, Double> distance = new HashMap<>();
        for (String nom : ReseauRues.keySet()) {
            distance.put(nom, Double.POSITIVE_INFINITY);
        }
        distance.put(depart.getNom(), 0.0);
        IntersectionsDecouvertes.clear();
        IntersectionsDecouvertes.put(depart.getNom(), null);

        final Map<String, Double> distFinal = distance;

        PriorityQueue<Intersection> file = new PriorityQueue<>(new Comparator<Intersection>() {
            @Override
            public int compare(Intersection i1, Intersection i2) {
                return Double.compare(
                        distFinal.get(i1.getNom()),
                        distFinal.get(i2.getNom())
                );
            }
        });

        file.add(depart);

        while (!file.isEmpty()) {
            Intersection courant = file.poll();

            if (courant.equals(destination)) {
                break;
            }

            double distCourant = distance.get(courant.getNom());
            if (distCourant == Double.POSITIVE_INFINITY) {
                break;
            }

            for (Arc arc : courant.getArcsSortants()) {
                Intersection voisin = arc.getArrivee();
                double nouvelleDist = distCourant + arc.getDistance();


                if (nouvelleDist < distance.get(voisin.getNom())) {
                    distance.put(voisin.getNom(), nouvelleDist);


                    IntersectionsDecouvertes.put(voisin.getNom(), arc);

                    file.remove(voisin);
                    file.add(voisin);
                }
            }
        }


        if (!IntersectionsDecouvertes.containsKey(destination.getNom())) {
            return null; // aucun chemin
        }

        List<Arc> chemin = new ArrayList<>();
        Intersection actuel = destination;

        while (!actuel.equals(depart)) {
            Arc arc = IntersectionsDecouvertes.get(actuel.getNom());
            if (arc == null) {
                return null;
            }
            chemin.add(arc);
            actuel = arc.getDepart();
        }

        Collections.reverse(chemin);

        return new Itineraire(depart, destination, chemin);
    }


    public Itineraire itineraireVersAdresse(Intersection depart, String numero, String rue) {
        Adresse adresse = getAdresse(numero, rue);  // <<< on utilise this.getAdresse
        if (adresse == null) {
            System.err.println("Adresse introuvable : " + numero + " " + rue);
            return null;
        }

        Arc arcAdresse = adresse.getAr();
        Intersection cible1 = arcAdresse.getDepart();
        Intersection cible2 = arcAdresse.getArrivee();

        Itineraire it1 = djikstra(depart, cible1);
        Itineraire it2 = djikstra(depart, cible2);

        double dist1 = (it1 == null) ? Double.POSITIVE_INFINITY : it1.getDistanceTotale();
        double dist2 = (it2 == null) ? Double.POSITIVE_INFINITY : it2.getDistanceTotale();

        Itineraire meilleur = (dist1 <= dist2) ? it1 : it2;

        if (meilleur == null) {
            System.err.println("Impossible d'atteindre l'adresse : " + numero + " " + rue);
            return null;
        }

        List<Arc> chemin = new ArrayList<>(meilleur.getArcs()); // ou getArcs()

        if (chemin.isEmpty() || chemin.get(chemin.size() - 1) != arcAdresse) {
            chemin.add(arcAdresse);
        }

        return new Itineraire(depart, adresse.getAr().getArrivee(), chemin);
    }
}

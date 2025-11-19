import java.nio.file.*;
import java.util.*;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Itineraire {
    private Intersection depart;
    private Intersection destination;
    private List<Arc> arcs;
    private Map<String, Arc> IntersectionsDecouvertes = new HashMap<>();
    PriorityQueue<Intersection> file = new PriorityQueue<>(new Comparator<Intersection>() {
        @Override
        public int compare(Intersection i1, Intersection i2) {
            double d1 = distance.get(i1.getNom());
            double d2 = distance.get(i2.getNom());
            return Double.compare(d1, d2);
        }
    });


    public Itineraire(Intersection depart, Intersection destination, List<Arc> arcs) {
        this.depart = depart;
        this.destination = destination;
        this.arcs=List.copyOf(arcs);
    }
    public double getDistanceTotale() {
        double total = 0.0;
        if (arcs == null) {
            return 0.0;
        }
        for (Arc arc : arcs) {
            if (arc != null) {
                total += arc.getDistance();
            }
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Itinéraire : ").append(depart.getNom()).append(" → ").append(destination.getNom()).append("\n");

        for (Arc a : arcs) {
            sb.append("  ").append(a.toString()).append("\n");
        }

        return sb.toString();
    }

    public Itineraire itineraireVersAdresse(Intersection depart, String numero, String rue, Reseau reseau) {
        Adresse adresse = reseau.getAdresse(numero,rue);
        if (adresse == null) {
            System.err.println("Adresse introuvable : " + numero + " " + rue);
            return null;
        }

        Arc arcAdresse = adresse.getAr();
        Intersection cible1 = arcAdresse.getDepart();
        Intersection cible2 = arcAdresse.getArrivee();
        Itineraire it1 = djikstra (depart,cible1);
        Itineraire it2 = djikstra (depart, cible2);
        double dist1 = (it1 == null) ? Double.POSITIVE_INFINITY : it1.getDistanceTotale();
        double dist2 = (it2 == null) ? Double.POSITIVE_INFINITY : it2.getDistanceTotale();


        Itineraire meilleur;

        if (dist1 <= dist2) {
            meilleur = it1;
        } else {
            meilleur = it2;
        }

        if (meilleur == null) {
            System.err.println("Impossible d'atteindre l'adresse : " + numero + " " + rue);
            return null;
        }

        // 3. Ajouter l'arc contenant l'adresse à la fin de l’itinéraire, si ce n'est pas déjà le cas
        List<Arc> chemin = new ArrayList<>(meilleur.getArcs()); // ou getChemin()
        if (chemin.isEmpty() || chemin.get(chemin.size()-1) != arcAdresse) {
            chemin.add(arcAdresse);
        }

        return new Itineraire(depart, adresse.getAr().getArrivee(), chemin);
    }

    public Itineraire djikstra(Intersection depart, Intersection destination) {
        if (depart.equals(destination)) {
            return new Itineraire(depart, destination, new ArrayList<>());
        }
        IntersectionsDecouvertes.clear();
        file.clear();
        Map<String, Double> distance = new HashMap<>();
        this.IntersectionsDecouvertes.put(depart.getNom(), null);
        file.addAll(depart.getArcsSortants());
        Arc arcActuel= null;
        while(!file.isEmpty()) {
            arcActuel = file.poll();
            if (!IntersectionsDecouvertes.containsKey(arcActuel.getArrivee().getNom())) {
                // On mémorise par quel arc on est arrivé
                IntersectionsDecouvertes.put(arcActuel.getArrivee().getNom(), arcActuel);

                // Si c’est la station recherchée, on peut s’arrêter
                if (arcActuel.getArrivee().equals(destination)) {
                    break;
                }
                file.addAll(arcActuel.getArrivee().getArcsSortants());

            }
        }

        List<Arc> chemin = new ArrayList<>();
        chemin.add(arcActuel);
        Intersection statActuelle = arcActuel.getArrivee();
        while (arcActuel.getDepart()!=depart){
            arcActuel =IntersectionsDecouvertes.get(statActuelle.getNom());
            chemin.add(arcActuel);
            statActuelle= arcActuel.getDepart();
        }

        Collections.reverse(chemin);

        Itineraire I = new Itineraire(depart, destination, chemin);
        return I;
    }
    public Intersection getDepart() { return depart; }
    public Intersection getArrivee() { return destination; }
    public List<Arc> getArcs() { return arcs; }
}
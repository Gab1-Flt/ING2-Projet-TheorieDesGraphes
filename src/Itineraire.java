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


    public Intersection getDepart() { return depart; }
    public Intersection getArrivee() { return destination; }
    public List<Arc> getArcs() { return arcs; }
}
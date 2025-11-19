import java.util.ArrayList;
import java.util.*;

public class Arc {
    private Intersection depart;
    private Intersection arrivee;
    private String rue;
    private final double distance;
    private List<Adresse> adresses = new ArrayList<>();
    public Arc (Intersection depart, Intersection arrivee, String rue, double distance, List<Adresse> adresses) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.rue= rue;
        this.distance = distance;
        this.adresses = adresses;
    }

    @Override
    public String toString() {
        return depart.getNom() + " --(" + rue + ")--> " + arrivee.getNom();
    }

    //Getter
    public Intersection getDepart(){
        return depart;
    }
    public String getRue(){
        return rue;
    }
    public double getDistance(){
        return distance;
    }
    public Intersection getArrivee(){
        return arrivee;
    }
}

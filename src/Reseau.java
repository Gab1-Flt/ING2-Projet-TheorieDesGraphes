import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.io.*;
public class Reseau {
    private Map<String, Intersection> ReseauRues = new HashMap<>();
    Intersection intersection(String nom){
        return ReseauRues.computeIfAbsent(nom, Intersection::new);
    }

    Arc addArcOriente(String depart, String arrivee, String NomRue, List<Adresse> adresses){
        Intersection d = intersection(depart);
        Intersection a = intersection(arrivee);
        Arc e = new Arc(d, a, NomRue, adresses);
        d.getArcsSortants().add(e);
        return e;
    }

    //Getter
    public Map<String, Intersection> getReseauStation(){
        return ReseauRues;
    }
}

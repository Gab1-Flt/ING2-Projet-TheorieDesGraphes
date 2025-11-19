import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.io.*;
public class Reseau {
    private Map<String, Intersection> ReseauRues = new HashMap<>();
    private Map<String, Adresse> adressesParCle = new HashMap<>();
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

    Arc addArcOriente(String depart, String arrivee, String NomRue, double distance, List<Adresse> adresses){
        Intersection d = intersection(depart);
        Intersection a = intersection(arrivee);
        Arc e = new Arc(d, a, NomRue, distance,adresses);
        d.getArcsSortants().add(e);
        return e;
    }



    //Getter
    public Map<String, Intersection> getReseauRues(){
        return ReseauRues;
    }
}

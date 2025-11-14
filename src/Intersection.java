import java.util.ArrayList;
import java.util.List;
import java.util.*;
public class Intersection {
    private String nom;
    private List<Arc> arcsSortants = new ArrayList<>();

    public Intersection(String nom) {
        this.nom = nom;
    }

    //Getter
    public String getNom(){
        return nom;
    }
    public List<Arc> getArcsSortants(){
        return arcsSortants;
    }
}

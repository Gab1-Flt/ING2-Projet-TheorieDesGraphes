import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Secteur {
    private final String id;           // ex : "S1"
    private final String nom;          // ex : "Secteur 1"
    private final int quantite;        // qs : quantité estimée de déchets
    private final Set<Secteur> voisins = new HashSet<>();

    // jour / couleur, -1 = pas encore affecté
    private int jour = -1;

    public Secteur(String id, String nom, int quantite) {
        this.id = id;
        this.nom = nom;
        this.quantite = quantite;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getQuantite() {
        return quantite;
    }

    public Set<Secteur> getVoisins() {
        return voisins;
    }

    public int getJour() {
        return jour;
    }

    public void setJour(int jour) {
        this.jour = jour;
    }

    public void ajouterVoisin(Secteur s) {
        if (s != null && s != this) {
            voisins.add(s);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Secteur)) return false;
        Secteur secteur = (Secteur) o;
        return Objects.equals(id, secteur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + " (" + nom + ", q=" + quantite + ", jour=" + jour + ")";
    }
}
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class Reseau {
    private Map<String, Intersection> ReseauIntersections = new HashMap<>();
    private Map<String, Adresse> adressesParCle = new HashMap<>();
    private Map<String, Arc> IntersectionsDecouvertes = new HashMap<>();
    private Map<String, Arc> Rues = new HashMap<>();

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
        return ReseauIntersections.computeIfAbsent(nom, Intersection::new);
    }

    public List<Arc> getArcsParRue(String nomRue) {
        List<Arc> resultat = new ArrayList<>();
        String cible = nomRue.trim().toLowerCase();

        for (Intersection inter : ReseauIntersections.values()) {
            for (Arc arc : inter.getArcsSortants()) {
                if (arc.getRue().trim().toLowerCase().equals(cible)) {
                    resultat.add(arc);
                }
            }
        }
        return resultat;
    }

    public boolean rueExiste(String nomRue) {
        if (nomRue == null || nomRue.trim().isEmpty()) {
            return false;
        }
        String rueRecherche = nomRue.trim();
        for (Intersection i : ReseauIntersections.values()) {
            for (Arc arc : i.getArcsSortants()) {
                if (arc.getRue().equalsIgnoreCase(rueRecherche)) {
                }
            }
        }
        return false;
    }

    public void bloquerRue(String nomRue) {
        if (nomRue == null || nomRue.trim().isEmpty()) {
            System.out.println("Nom de rue invalide.");
            return;
        }

        String rueRecherche = nomRue.trim();
        boolean trouve = false;

        for (Intersection i : ReseauIntersections.values()) {
            for (Arc arc : i.getArcsSortants()) {
                if (arc.getRue().equalsIgnoreCase(rueRecherche)) {
                    arc.setBloque(true);
                    trouve = true;
                }
            }
        }

        if (trouve) {
            System.out.println("La rue \"" + nomRue + "\" a été bloquée.");
        } else {
            System.out.println("La rue \"" + nomRue + "\" n'existe pas dans le réseau.");
        }
    }

    public void ajouterRue(String rue, String departNom, String arriveeNom, double distance, List<String> numeros) {

        List<Adresse> adresses = new ArrayList<>();

        Intersection d = intersection(departNom);
        Intersection a = intersection(arriveeNom);

        Arc arc = new Arc(d, a, rue, distance, adresses);

        for (String numero : numeros) {
            Adresse ad = new Adresse(numero, arc);
            adresses.add(ad);
            indexerAdresse(ad);
        }

        d.getArcsSortants().add(arc);

    }



    Arc addArcOriente(String depart, String arrivee, String NomRue, double distance, List<Adresse> adresses){
        Intersection d = intersection(depart);
        Intersection a = intersection(arrivee);
        Arc e = new Arc(d, a, NomRue, distance, adresses);
        d.getArcsSortants().add(e);
        return e;
    }
    Arc addArcNonOriente(String depart, String arrivee, String NomRue, double distance, List<Adresse> adresses){
        Intersection d = intersection(depart);
        Intersection a = intersection(arrivee);
        Arc e = new Arc(d, a, NomRue, distance, adresses);

        Rues.putIfAbsent(NomRue, e);
        return e;
    }
    public int getNbRues(){
        return Rues.size();
    }

    public Map<String, Intersection> getReseauIntersections(){
        return ReseauIntersections;
    }

    public Intersection getIntersection(String nom) {
        return ReseauIntersections.get(nom);
    }

    public Itineraire djikstra(Intersection depart, Intersection destination) {

        if (depart == null || destination == null) {
            return null;
        }

        if (depart.equals(destination)) {
            return new Itineraire(depart, destination, new ArrayList<Arc>());
        }

        Map<String, Double> distance = new HashMap<>();
        for (String nom : ReseauIntersections.keySet()) {
            distance.put(nom, Double.POSITIVE_INFINITY);
        }
        distance.put(depart.getNom(), 0.0);
        IntersectionsDecouvertes.clear();
        IntersectionsDecouvertes.put(depart.getNom(), null);

        final Map<String, Double> distFinal = distance;

        PriorityQueue<Intersection> file = new PriorityQueue<>(new Comparator<Intersection>() {
            @Override
            public int compare(Intersection i1, Intersection i2) {
                return Double.compare(distFinal.get(i1.getNom()), distFinal.get(i2.getNom()));
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
                if (arc.isBloque()) {
                    continue;
                }

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

    public List<Arc> euler(Intersection depart) {
        int stop=0;
        Map<Intersection, List<Arc>> adj = new HashMap<>();
        for (Intersection inter : ReseauIntersections.values()) {
            adj.put(inter, new ArrayList<>());
        }

        for (Arc rueArc : Rues.values()){
            Intersection a = rueArc.getDepart();
            Intersection b = rueArc.getArrivee();
            adj.computeIfAbsent(a, key->new ArrayList<>()).add(rueArc);
            adj.computeIfAbsent(b, key->new ArrayList<>()).add(rueArc);
        }
        Deque<Intersection> pileIntersections = new ArrayDeque<>();
        Deque<Arc> pileRues = new ArrayDeque<>();
        List<Arc> circuitFinal = new ArrayList<>();
        pileIntersections.push(depart);
        while (!pileIntersections.isEmpty()) {
            Intersection courant = pileIntersections.peek();
            List<Arc> ruesDisponibles = adj.get(courant);
            Arc rueChoisie=null;
            while (ruesDisponibles !=null && !ruesDisponibles.isEmpty()) {
                rueChoisie = ruesDisponibles.remove(ruesDisponibles.size() - 1);
                if (rueChoisie !=null){break;}
            }
            if (rueChoisie != null) {
                Intersection voisin;
                if (rueChoisie.getDepart().equals(courant)) {
                    voisin = rueChoisie.getArrivee();
                } else {
                    voisin = rueChoisie.getDepart();
                }
                adj.get(voisin).remove(rueChoisie);
                pileIntersections.push(voisin);
                pileRues.push(rueChoisie);
            }else {

                // Cul-de-sac : on remonte
                pileIntersections.pop();

                if (!pileRues.isEmpty()) {
                    circuitFinal.add(pileRues.pop());
                }
            }
        }
        Collections.reverse(circuitFinal);
        return circuitFinal;
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

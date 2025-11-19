import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Chargement {

    public static Reseau load(String fichier) {
        Reseau reseau = new Reseau();

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;

            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.isEmpty()) {
                    continue;
                }

                String[] elements = ligne.split(";");
                if (elements.length < 3) {
                    System.err.println("Ligne invalide (moins de 3 éléments) : " + ligne);
                    continue;
                }

                String rue = elements[0].trim();
                String intersectionDepart = elements[1].trim();
                String intersectionArrivee = elements[2].trim();
                Double distance = Double.parseDouble(elements[3].trim());

                List<Adresse> adresses = new ArrayList<>();

                // Un seul Arc pour toutes les adresses de cette ligne
                Arc arc = new Arc(
                        new Intersection(intersectionDepart),
                        new Intersection(intersectionArrivee),
                        rue,distance
                        , adresses
                );

                // Adresses à partir de l’indice 3
                for (int u = 4; u < elements.length; u++) {
                    String numero = elements[u].trim();
                    if (numero.isEmpty()) {
                        continue;
                    }
                    Adresse a = new Adresse(numero, arc);
                    adresses.add(a);
                    reseau.indexerAdresse(a);
                }

                reseau.addArcOriente(intersectionDepart, intersectionArrivee, rue, distance,adresses);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return reseau;
    }
}


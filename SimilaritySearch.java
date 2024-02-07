// Author: Mor Fall Sylla
// Student number: 300218857

// CSI 2520 Winter 2024

import java.io.File;
import java.util.*;

public class SimilaritySearch {
    public static void main(String[] arguments) {
        // Vérification du nombre correct d'arguments
        if (arguments.length != 2) {
            System.out.println("Usage: java SimilaritySearch <queryFileName> <imageDirectory>");
            return;
        }

        String queryFileName = arguments[0];
        String imageDirectory = arguments[1];

        // Remplacement de l'extension .jpg par .ppm si nécessaire
        if (queryFileName.endsWith("jpg")) {
            queryFileName = queryFileName.replaceFirst("\\.jpg$", ".ppm");
        }

        // Chargement de l'image de requête
        ColorImage ImageRequete = new ColorImage(queryFileName);

        // Réduction de la profondeur de couleur de l'image de requête
        ImageRequete.reduceColor(3);

        // Calcul de l'histogramme de couleur de l'image de requête
        ColorHistogram histogrammeRequete = new ColorHistogram(3);
        histogrammeRequete.setImage(ImageRequete);
        histogrammeRequete.calculateHistogram();

        // Chargement de la liste des fichiers d'images dans le répertoire spécifié
        File imageFolder = new File(imageDirectory);
        File[] listOfImageFiles = imageFolder.listFiles();

        // Création d'une map pour stocker les noms de fichiers d'images et leurs scores de similarité
        Map<String, Double> scoresSimilarité = new HashMap<>();

        // Parcours de la liste des fichiers d'images pour calculer les scores de similarité
        if (listOfImageFiles != null) {
            for (File imageFile : listOfImageFiles) {
                if (imageFile.isFile() && imageFile.getName().endsWith(".txt")) {
                    String imageFileName = imageFile.getName();
                    ColorHistogram datasetHistogram = new ColorHistogram(imageDirectory + File.separator + imageFileName);
                    datasetHistogram.Hist_Normaliser();  // Normalisation de l'histogramme
                    double score = histogrammeRequete.compare(datasetHistogram);
                    scoresSimilarité.put(imageFileName, score);
                }
            }
        } else {
            System.out.println("Le répertoire spécifié n'existe pas ou n'est pas un répertoire.");
            return;
        }

        // Tri de la liste des scores de similarité
        List<Map.Entry<String, Double>> sortedList = new ArrayList<>(scoresSimilarité.entrySet());
        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        //  Affichage des noms des 5 images les plus similaires
        System.out.println("Author: Mor Fall Sylla\n" + "Student number: 300218857");
        System.out.println("Les 5 images les plus similaires à " + queryFileName + " sont :");
        for (Map.Entry<String, Double> imageName : sortedList.subList(0, Math.min(5, sortedList.size()))) {
            System.out.println(imageName);
        }
    }
}

// Author: Mor Fall Sylla
// Student number: 300218857

// CSI 2520 Winter 2024

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ColorHistogram {
    private int d;  // Nombre de bits par canal de couleur
    private double[] histogramArray; // Tableau pour stocker l'histogramme
    private ColorImage colorImage; // Image associée à l'histogramme
    private int numberOfPixels; // Nombre de pixels dans l'image associée

    // Constructeur pour créer un histogramme à partir de d bits
    public ColorHistogram(int d) {
        this.d = d;
    }

    // Constructeur pour créer un histogramme à partir d'un fichier texte
    public ColorHistogram(String filename) {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filename));
            fileReader.readLine(); // Ignorer la première ligne
            List<Double> valuesList = new ArrayList<>();

            String fileLine;
            while ((fileLine = fileReader.readLine()) != null) {
                String[] lineValues = fileLine.trim().split("\\s+");
                for (String strValue : lineValues) {
                    if (!strValue.isEmpty()) {
                        double doubleValue = Double.parseDouble(strValue);
                        valuesList.add(doubleValue);
                    }
                }
            }

            // Convertir la liste en tableau
            histogramArray = new double[valuesList.size()];
            for (int i = 0; i < valuesList.size(); i++) {
                histogramArray[i] = valuesList.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour normaliser l'histogramme
    public void Hist_Normaliser() {
        double sum = 0;
        for (double value : histogramArray) {
            sum += value;
        }
        if (sum == 0) {
            throw new RuntimeException("La somme des valeurs de l'histogramme est nulle. Impossible de normaliser.");
        }
        for (int j = 0; j < histogramArray.length; j++) {
            histogramArray[j] = (histogramArray[j] / sum);
        }
    }

    // Méthode pour associer une image à l'histogramme
    public void setImage(ColorImage image) {
        this.colorImage = image;
        this.numberOfPixels = image.getHeight() * image.getWidth();
    }

    // Méthode pour obtenir l'histogramme normalisé de l'image associée
    public double[] getHistogram() {
        if (this.histogramArray == null && this.colorImage != null) {
            calculateHistogram();
        }
        return this.histogramArray;
    }

    // Méthode pour calculer l'histogramme normalisé d'une image
    public void calculateHistogram() {
        int numberOfEntries = (int) Math.pow(2, d * 3);
        this.histogramArray = new double[numberOfEntries];
        for (int i = 0; i < this.colorImage.getHeight(); i++) {
            for (int j = 0; j < this.colorImage.getWidth(); j++) {
                int[] reducedPixel = colorImage.getReducedPixel(i, j);
                int indexOfEntry = (reducedPixel[0] << (2 * d)) + (reducedPixel[1] << d) + reducedPixel[2];
                histogramArray[indexOfEntry]++;
            }
        }
        for (int i = 0; i < numberOfEntries; i++) {
            histogramArray[i] /= numberOfPixels;
        }
    }

    // Méthode pour comparer deux histogrammes et retourner leur intersection
    public double compare(ColorHistogram hist) {
        double intersectionValue = 0.0;
        for (int i = 0; i < this.histogramArray.length; i++) {
            intersectionValue += Math.min(this.histogramArray[i], hist.histogramArray[i]);
        }
        return intersectionValue;
    }

    // Méthode pour sauvegarder l'histogramme dans un fichier
    public void save(String nomFichier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier))) {
            writer.write(histogramArray.length + "\n"); // Écrire le nombre d'entrées
            for (int i = 0; i < histogramArray.length; i++) {
                writer.write(histogramArray[i] + (i < histogramArray.length - 1 ? " " : ""));
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

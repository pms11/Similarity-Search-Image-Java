// Author: Mor Fall Sylla
// Student number: 300218857

// CSI 2520 Winter 2024

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ColorImage {
    // Largeur et longueur de l'image
    private int width;
    private int height;
    private int depth; // Profondeur de couleur
    private int[][][] pixels; // Matrice de pixels en RGB
    private int[][][] reducedPixels;
    private String filename; // Nom du fichier

    // Constructeur de la classe ColorImage
    public ColorImage(String filename) {
        try {
            // Ouverture du fichier et lecture de ses métadonnées
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            // Verifier que le ficher text est un ppm
            if (!line.equals("P3")) {
                throw new IOException("Format de fichier PPM invalide");
            }
            reader.readLine(); // Lire la ligne de commentaire
            String[] dimensions = reader.readLine().trim().split("\\s+"); // Les dimensions de l'image sont deja precisén dans le fichier
            this.width = Integer.parseInt(dimensions[0]);
            this.height = Integer.parseInt(dimensions[1]);
            this.depth = 8;

            // Initialisation de la matrice de pixels
            this.pixels = new int[height][width][3];
            this.filename = filename;
            reader.close();

            // Chargement de l'image dans la matrice de pixels
            loadImageMatrixFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    private void loadImageMatrixFromFile() {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filename)); // Lecteur de fichier pour lire le fichier
            for (int i = 0; i < 4; i++) {
                fileReader.readLine(); // Ignorer les lignes de métadonnées
            }
    
            int currentRow = 0; // Suivre la ligne actuelle en cours de traitement
            List<Integer> rgbValues = new ArrayList<>(); // Liste pour stocker temporairement les valeurs RVB
            String line;
            // Lire chaque ligne du fichier jusqu'à la fin ou jusqu'à ce que toutes les lignes soient traitées
            while ((line = fileReader.readLine()) != null && currentRow < height) {
                String[] currentLineValues = line.trim().split("\\s+"); // Diviser la ligne par des espaces blancs
                for (String value : currentLineValues) {
                    rgbValues.add(Integer.parseInt(value)); // Analyser chaque valeur en Integer et ajouter à la liste RVB
                    if (rgbValues.size() >= width * 3) { // Vérifier si suffisamment de valeurs RVB sont accumulées pour une ligne
                        for (int col = 0; col < width; col++) {
                            int r = rgbValues.remove(0); // Retirer la première valeur comme rouge
                            int g = rgbValues.remove(0); // Retirer la deuxième valeur comme vert
                            int b = rgbValues.remove(0); // Retirer la troisième valeur comme bleu
                            pixels[currentRow][col] = new int[]{r, g, b}; // Stocker les valeurs RVB dans le tableau de pixels
                        }
                        currentRow++; // Passer à la ligne suivante
                    }
                }
            }
            fileReader.close(); // Fermer le lecteur de fichier
        } catch (IOException e) {
            e.printStackTrace(); // Imprimer la trace de la pile en cas d'exception d'entrée/sortie
        }
    }
    


    // Méthode pour obtenir la profondeur de couleur
    public int getDepth() {
        return this.depth;
    }

    // Méthode pour obtenir la largeur de l'image
    public int getWidth() {
        return this.width;
    }

    // Méthode pour obtenir la hauteur de l'image
    public int getHeight() {
        return this.height;
    }

    // Méthode pour obtenir les valeurs des canaux RVB d'un pixel donné
    public int[] getPixel(int i, int j) {
        // Vérifier que les indices de pixel sont dans les limites autorisées
        if (i < 0 || i >= this.height || j < 0 || j >= this.width) {
            throw new IllegalArgumentException("Indice de pixel hors limites");
        } else {
            return pixels[i][j];
        }
    }

    // Méthode pour réduire la profondeur de couleur à d bits
    public void reduceColor(int d) {
        reducedPixels = new int[height][width][3]; // Initialiser la matrice de pixels réduits
        for (int i = 0; i < this.height; i++) { // Parcourir la matrice de pixels
            for (int j = 0; j < this.width; j++) {
                for (int k = 0; k < 3; k++) {
                    reducedPixels[i][j][k] = pixels[i][j][k] >> (8 - d); // Réduire la profondeur de couleur
                }
            }
        }
    }

    // Méthode pour obtenir les valeurs des canaux RVB réduits d'un pixel donné
    public int[] getReducedPixel(int i, int j) {
        // Vérifier que la matrice de pixels réduits n'est pas nulle
        if (reducedPixels == null) {
            throw new IllegalArgumentException("Matrice de pixels réduits est nulle");
        }
        // Vérifier que les indices de pixel sont dans les limites autorisées
        if (i >= 0 && i <= height && j >= 0 && j <= width) {
            return reducedPixels[i][j];
        } else {
            throw new IllegalArgumentException("Indice de pixel hors limites");
        }
    }
}

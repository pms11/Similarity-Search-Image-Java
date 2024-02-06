import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ColorHistogram {

    private int d;
    private double[] histogram;
    private ColorImage colorImage;
    private int NumberOfPixel;
    double[] histogramArray;
    //constructeur pour créer un histogramme à partir de d bits
    public ColorHistogram(int d) {
        this.d = d;
    }
    // constructeur pour créer un histogramme à partir d'un fichier texte
    public ColorHistogram(String filename) {
        try {
            BufferedReader fileReader = new BufferedReader (new FileReader(filename));
            // Passer les trois premiers lignes
            fileReader.readLine();
            fileReader.readLine();
            fileReader.readLine();

        // Initialise une liste pour stocker les valeurs de l'histogramme
        List<Double> valuesList = new ArrayList<>();

        // Lit chaque ligne du fichier
        String fileLine;
        while ((fileLine = fileReader.readLine()) != null) {
            // Divise la ligne en valeurs individuelles en supprimant les espaces
            String[] lineValues = fileLine.trim().split("\\s+");
            for (String strValue : lineValues) {
                // Convertit chaque valeur en double et l'ajoute à la liste
                if (!strValue.isEmpty()) {
                    double doubleValue = Double.parseDouble(strValue);
                    valuesList.add(doubleValue);
                }
            }
        }
        // Convertit la liste en tableau
        histogramArray = new double[valuesList.size()];
        for (int i = 0; i < valuesList.size(); i++) {
            histogramArray[i] = valuesList.get(i);
        }
        }
        catch(FileNotFoundException e){
            throw new RuntimeException("Le fichier n'existe pas");
        }
        catch( IOException e){
            e.printStackTrace();
        }
        
        
    }
    public void Hist_Normaliser(){
        double somme=0;
        int i=0;
        while (i<histogramArray.length){
            somme+=histogramArray[i];
            i++;
        }
        if (somme==0){
            throw new RuntimeException("La somme des valeurs de l'histogramme est nulle. Impossible de normaliser.");
        }
        for(int j=0; j<histogramArray.length; j++){
            histogramArray[j]=(histogramArray[j]/somme);
        }

    } 
    public void setImage(ColorImage image) {
        // Associer une image avec un histogramme
        this.colorImage=image;
        this.NumberOfPixel=image.getHeight()*image.getWidth();
    }
        // Retourner l'histogramme normalisé de l'image associée
    public double[] getHistogram() {
        if (this.histogram == null && this.colorImage != null) {
            calculateHistogram();
        }
        return this.histogramArray;
    }
     //methode pour calculer l'histogramme normalisé d'une image
    void calculateHistogram() {
        int NumberOfEntree=(int) Math.pow(2, this.d);
        this.histogramArray=new double[NumberOfEntree];
        for(int i=0; i<this.colorImage.getHeight();i++){
            for(int j=0; j<this.colorImage.getWidth(); j++){
                int [] Reducedpixel =colorImage.getReducedPixel(i, j);
                int indexOfEntree=(Reducedpixel[0]<<(2*d))+ (Reducedpixel[1]<<d)+Reducedpixel[2];
                histogramArray[indexOfEntree]++;
            }
        }
        for (int i = 0; i < NumberOfEntree; i++) {
            histogramArray[i] /= NumberOfPixel;
        }
    }
    public double compare(ColorHistogram hist) {
        // Retourner la valeur d'intersection entre deux histogrammes
        double intersectionValue  = 0.0;
        for (int i = 0; i < this.histogramArray.length; i++) {
            intersectionValue+= Math.min( this.histogramArray[i], hist.histogramArray[i] );
        }
        return intersectionValue;
    }

    public void save(String nomFichier) {
        // Utilisation d'un bloc try-with-resources pour assurer la fermeture automatique du flux de fichier
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier))) {
            // Écriture du nombre d'entrées au début de l'histogramme dans le fichier
            writer.write(histogramArray.length + "\n"); // Puis saut de ligne comme dans le format
    
            // Écriture des données de l'histogramme
            for (int i = 0; i < histogramArray.length; i++) {
                // Écriture de chaque valeur de l'histogramme dans le fichier
                writer.write(histogramArray[i] + (i < histogramArray.length - 1 ? " " : ""));
            }
            // Ajout d'un saut de ligne à la fin pour indiquer la fin des données de l'histogramme
            writer.newLine();
        } catch (IOException e) {
            // Gestion des exceptions en cas de problème lors de l'écriture dans le fichier
            e.printStackTrace();
        }
    }
    
}

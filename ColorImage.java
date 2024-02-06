//import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;

//import javax.imageio.ImageIO;

public class ColorImage {

    private int width;
    private int height;
    private int depth;
    private int[][][] pixels;
    private int [][][] reducedpixels;

    public ColorImage(String filename) {
    try {
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line = reader.readLine();
        if (!line.equals("P3")) {
            throw new Exception("Invalid PPM file format");
        }

        line = reader.readLine();
        String[] dimensions = line.split(" ");
        this.width = Integer.parseInt(dimensions[0]);
        this.height = Integer.parseInt(dimensions[1]);
        this.depth =8;

        this.pixels = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = reader.read();
                int g = reader.read();
                int b = reader.read();
                this.pixels[y][x][0] = r;
                this.pixels[y][x][1] = g;
                this.pixels[y][x][2] = b;
            }
        }

        reader.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    // methode d'acces pour depth
    public int getDepth(){
        return this.depth;


    }
    // Methode d'acces pour  width
    public int getWidth(){
        return this.width;
    }
    // Methode d'acces pour height
    public int getHeight() {
        return this.height;
    }

    

    public int[] getPixel(int i, int j) {
        // Retourner les valeurs des canaux du pixel à la colonne i et rangée j
        if (i < 0 || i >= this.height || j < 0 || j >= this.width) {
            // Lancer une exception quand les indices ne sont pas valides
            throw new IllegalArgumentException("Error setting pixel: index out of bounds");
        } else {
            // Sinon retourner les valeurs des canaux
        return pixels[i][j]; 
    }
    }
    // Méthode reduceColor permettant de réduire l’espace couleur à une représentation sur d bits.
   public void reduceColor(int d){
        reducedpixels= new int[height][width][3];
         for (int i = 0; i < this.height; i++) {
              for (int j = 0; j < this.width; j++) {
                for (int k = 0; k < 3; k++) {
                    reducedpixels[i][j][k] = pixels[i][j][k] >> (8 - d);
                }
              }
         }
   }
   public int [] getReducedPixel(int i, int j){
        // On doit s'assurer que les pixels de depart ne sont pas nuls et verifier que les indices sont bien valides
        if (reducedpixels == null) {
            throw new IllegalArgumentException(" The pixel that you're trying to access is null");
        }
        if (i>=0 && i<= height && j>=0 && j<= width){
            return reducedpixels[i][j];
        }
        else {
            throw new IllegalArgumentException("Error setting pixel: index out of bounds");

        }

   }

}


import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SimilaritySearch {

    public static void main(String[] arguments) {
        System.setProperty("user.dir", "");
        if (arguments.length != 2) {
            System.out.println("Usage: java SimilaritySearch <queryFileName> <imageDirectory>");
            return;
        }

        String queryFileName = arguments[0];
        String imageDirectory = arguments[1];

        if (queryFileName.endsWith("jpg")) {
            // Replace the ".jpg" extension with ".ppm"
            queryFileName = queryFileName.replaceFirst("\\.jpg$", ".ppm");
        }

        // Load the query file
        ColorImage queryImage = new ColorImage(queryFileName);

        // Load the image database and calculate similarities
        queryImage.reduceColor(3);
        ColorHistogram queryHistogram = new ColorHistogram(3);
        queryHistogram.setImage(queryImage);
        queryHistogram.calculateHistogram();

        File imageFolder = new File(imageDirectory);
        File[] listOfImageFiles = imageFolder.listFiles();

        // Map to store image filenames and their similarity scores
        Map<String, Double> similarityScores = new HashMap<>();

        if (listOfImageFiles != null) {
            for (File imageFile : listOfImageFiles) {
                if (imageFile.isFile() && imageFile.getName().endsWith(".txt")) {
                    String imageFileName = imageFile.getName();
                    ColorHistogram datasetHistogram = new ColorHistogram(imageDirectory + File.separator + imageFileName);
                    datasetHistogram.Hist_Normaliser(); // Normalizing

                    double score = queryHistogram.compare(datasetHistogram);
                    similarityScores.put(imageFileName, score);
                }
            }
        } else {
            System.out.println("The specified directory does not exist or is not a directory.");
            return;
        }
        // Sort the list
        List<Map.Entry<String, Double>> sortedList = new ArrayList<>(similarityScores.entrySet());
        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        // Display the names of the top 5 most similar images
        System.out.println("The top 5 images most similar to " + queryFileName + " are:");
        for (Entry<String, Double> imageName : sortedList.subList(0, Math.min(5, sortedList.size()))) {
            System.out.println(imageName);
        }
    }
}

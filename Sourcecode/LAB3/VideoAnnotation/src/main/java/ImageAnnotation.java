import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.typography.hershey.HersheyFont;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.*;

/**
 * Created by Vijaya Yeruva on 02-08-2017.
 */
public class ImageAnnotation {
    public static void main(String[] args) throws IOException {
        final ClarifaiClient client = new ClarifaiBuilder("eRnnzUP3qYx-re60UrrmecVOZlS3aqUkaTOhHYLp", "IDavwZ8X6DQAaa7b4CjGqoQ6pMFwdalvJZXNps9_")
                .client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
                .buildSync(); // or use .build() to get a Future<ClarifaiClient>
        client.getToken();

        File file = new File("output/mainframes");
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            ClarifaiResponse response = client.getDefaultModels().generalModel().predict()
                    .withInputs(
                            ClarifaiInput.forImage(ClarifaiImage.of(files[i]))
                    )
                    .executeSync();
            List<ClarifaiOutput<Concept>> predictions = (List<ClarifaiOutput<Concept>>) response.get();
            MBFImage image = ImageUtilities.readMBF(files[i]);
            int x = image.getWidth();
            int y = image.getHeight();

            MBFImage image1 = ImageUtilities.readMBF(files[i]);
            int x1 = image1.getWidth();
            int y1 = image1.getHeight();

            System.out.println("*************" + files[i] + "***********");
            List<Concept> data = predictions.get(0).data();


            String output = "Summary of pic:";
            try {
                PrintWriter writer = new PrintWriter("Annotations.txt", "UTF-8");
                PrintWriter writer1 = new PrintWriter("Summary.txt", "UTF-8");
                for (int j = 0; j < data.size(); j++) {
                    System.out.println(data.get(j).name() + " - " + data.get(j).value());
                    output = output + " " + data.get(j).name();
                    image.drawText(data.get(j).name(), (int)Math.floor(Math.random()*x), (int) Math.floor(Math.random()*y), HersheyFont.ASTROLOGY, 20, RGBColour.RED);
                    writer.println(data.get(j).name());
                }

                writer.close();
                writer1.println(output);
                writer1.close();
            } catch (IOException e) {
                // do something
            }
            DisplayUtilities.displayName(image, "image" + i);
            System.out.println(output);
            image1.drawText(output.substring(0, 64), 20, 400, HersheyFont.FUTURA_MEDIUM, 20, RGBColour.WHITE);
            DisplayUtilities.displayName(image1, "image1" + i);
        }
    }
}

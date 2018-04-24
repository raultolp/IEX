package app;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;

public abstract class IEXdata {

    //-----------------------------------------------------------------------
    //Source for reading from url and conversion to JSON:
    //https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
    // user: user2654569 (Feb 23 '14 at 4:02)
    //-----------------------------------------------------------------------

    public static JsonElement downloadData(String urlAsString) throws IOException {
        JsonElement root; // = null;
/*        HttpURLConnection request = null;
        InputStream is = null;*/

        try (InputStream is = new URL(urlAsString).openStream()) {  //try-with-resources
            JsonParser jp = new JsonParser(); //from gson
            root = jp.parse(new InputStreamReader(is)); //contains JSON Array or JSON Object
        }

/*        try {
            URL url = new URL(urlAsString);
            request = (HttpURLConnection) url.openConnection();
            request.connect();
            is = (InputStream) request.getContent();

            //TAGASISIDE: HttpURLConnection ja request.getContent() asemel saaks vist teha
            // lihtsalt new URL(..).openStream(). soovitan selle tõsta enne try,
            // siis finally plokki ei pea kõiki neid null checke lisama

            JsonParser jp = new JsonParser(); //from gson
            root = jp.parse(new InputStreamReader(is)); //contains JSON Array or JSON Object
        } finally {
            if (request != null) {
                request.disconnect();
            }
            if (is != null) {
            }
            try {
                is.close();
            } catch (IOException e) {
                System.out.println("Saving company info to file failed.");
                //e.printStackTrace();  // should never be reached - but still happened (once)....

                //TAGASISIDE: miks seda üldse catchida vaja on?

            }
            catch (NullPointerException e) {
                System.out.println("Saving company info to file failed.");
            }
            //TAGASISIDE: NullPointerException ei peaks kunagi catchima. kui see exception tekib,
            // siis programm üritab teha võimatut - kasutada objekti, mida pole olemas. selle asemel
            // peaks kasutama if (something != null), mis on palju selgem
        }*/

        return root;

    }

}

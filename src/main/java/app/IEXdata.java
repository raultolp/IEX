package app;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class IEXdata {

    //-----------------------------------------------------------------------
    //Source for reading from url and conversion to JSON:
    //https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
    // user: user2654569 (Feb 23 '14 at 4:02)
    //-----------------------------------------------------------------------

    public static JsonElement downloadData(String urlAsString) throws IOException {  //throws Exception
        JsonElement root=null;
        HttpURLConnection request=null;
        InputStream is=null;

        try {
            URL url = new URL(urlAsString);
            request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser(); //from gson
            is = (InputStream) request.getContent();
            root = jp.parse(new InputStreamReader(is)); //contains JSON Array or JSON Object
        }

        finally {
            if (request!=null){
                request.disconnect();
            }
            if (is!=null) {
            }
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();  // should never be reached
                }
        }
         return root;

    }
    //TODO: (PRIORITY 1- DONE.) VEEBIST LUGEMISE ERIND (RuntimeException) VAJA PÜÜDA.

}

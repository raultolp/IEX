package app.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

abstract class IEXdata {

    //-----------------------------------------------------------------------
    //Source for reading from url and conversion to JSON:
    //https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
    // user: user2654569 (Feb 23 '14 at 4:02)
    //-----------------------------------------------------------------------

    public static JsonElement downloadData(String urlAsString) throws IOException {
        try (InputStream is = new URL(urlAsString).openStream()) {  //try-with-resources
            JsonParser jp = new JsonParser(); //from gson
            return jp.parse(new InputStreamReader(is, "UTF-8")); //JsonElement; contains JSON Array or JSON Object
        }
    }
}

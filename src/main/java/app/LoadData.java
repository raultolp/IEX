package app;

import com.oracle.tools.packager.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

//API: https://docs.oracle.com/javase/8/docs/api/java/net/URL.html
//     https://docs.oracle.com/javase/8/docs/api/java/net/URLConnection.html


public class LoadData {

    //SOURCE: https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
    JSONObject json = new JSONObject(IOUtils.toString(new URL("https://graph.facebook.com/me"),
            Charset.forName("UTF-8")));



    InputStream sisse = new URL("http://ut.ee/").openStream();

}

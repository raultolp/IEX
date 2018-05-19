package app.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Company extends IEXdata {

    private final String symbol; //aktsia sümbol
    private String companyName;
    private String sector;
    private String industry; // täpsustab sektorit
    private String description; //ettevõtte tegevusalade kirjeldus
    private String CEO;
    private String website;
    //ETF-ide puhul: sector "", industry "", CEO "", description vb olla mitmel real
    //FAZ, SPY, DIA,  IWM, XLB, XLE XLF XLK XLP XLU XLV, TLT, UVXY, OIL, FAS


    //-----------------------------------------
    //Company information is loaded from IEX only if it is not yet stored in file:
    public Company(String symbol, Iu handler, IO io) throws Exception {
        this.symbol = symbol.toUpperCase();
        if (!loadFromFile(handler)) {
            loadDataFromWeb(handler, io);
        }
    }

    //-----------------------------------------
    //Loading Company info from file:
    private boolean loadFromFile(Iu handler) throws IOException {
        File fail = new File("CompanyInfo.txt");

        try ( Scanner sc = new Scanner(fail, "UTF-8") ) {  //try-with-resources
            while (sc.hasNextLine()) {
                String row = sc.nextLine();
                String[] pieces = row.split("////");
                if (pieces[0].equals(symbol)) {
                    companyName = pieces[1];
                    sector = pieces[2];
                    industry = pieces[3];
                    description = pieces[4];
                    CEO = pieces[5];
                    website = pieces[6];

                    if (handler.isAdmin()) {
                        System.out.println("Company info of " + symbol + " loaded from file.");
                    }

                    return true;
                }
            }
        }
        return false;
    }

    //-----------------------------------------
    //Loading Company info from web:
    private void loadDataFromWeb(Iu handler, IO io) throws IOException {  //äkki peaks tegema booleaniks - kui õnnestub, on true
        String URL = "https://api.iextrading.com/1.0/stock/" + symbol + "/company";
        boolean isAdmin = handler.isAdmin();

        try {
            JsonElement root = IEXdata.downloadData(URL);  // array or object
            JsonObject rootobj = root.getAsJsonObject();

            companyName = rootobj.get("companyName").getAsString(); //Apple Inc.
            sector = rootobj.get("sector").getAsString(); //"Technology"
            industry = rootobj.get("industry").getAsString(); // "Computer Hardware"
            CEO = rootobj.get("CEO").getAsString();  //Timothy D. Cook
            website = rootobj.get("website").getAsString();

            //For ETFs, description can be on several lines; for saving to file, the info
            // should be only on one line. Therefore, the line breaks should be deleted:
            String description_temp = rootobj.get("description").getAsString();
            description = description_temp.replaceAll("\n", "");

            if (isAdmin) {
                System.out.println("Company info of " + symbol + " downloaded from IEX.");
            }

            //-----------------------------------------
            //Saving downloaded data to file:
            try ( OutputStream out = new FileOutputStream("CompanyInfo.txt", true); //true- lisamine faili lõppu
                  OutputStreamWriter texToWrite = new OutputStreamWriter(out, "UTF-8")) {
                texToWrite.write(symbol + "////" + companyName + "////" + sector + "////" + industry + "////"
                        + description + "////" + CEO + "////" + website + "\n");
            } catch (IOException e) {
                if (isAdmin) {
                    System.out.println("Writing Company info of " + symbol + " to file failed.");
                }

            }

        } catch (IOException e) {
            io.println("Connection to IEX failed. Please try again.");
        }

    }


    //Getting latest news on the company:
    public ArrayList<String> getCompanyNews(Iu handler, IO io) throws IOException {

        ArrayList<String> newsItems = new ArrayList<>();
        String URL = "https://api.iextrading.com/1.0/stock/" + symbol + "/news";

        try {
            JsonElement root = IEXdata.downloadData(URL);  // array or object
            JsonArray rootArray = root.getAsJsonArray();

            for (JsonElement jsonElement : rootArray) {
                JsonObject jsonNewsItem = jsonElement.getAsJsonObject();
                String timeTemp = jsonNewsItem.get("datetime").getAsString();
                String time = timeTemp.substring(0, 17);
                String headline = jsonNewsItem.get("headline").getAsString();
                String source = jsonNewsItem.get("source").getAsString();
                String website = jsonNewsItem.get("url").getAsString();
                String summary = jsonNewsItem.get("summary").getAsString();
                String related = jsonNewsItem.get("related").getAsString();

                NewsItem newsItem = new NewsItem(symbol, time, headline, source, website, summary, related);
                newsItems.add(newsItem.toString());
            }

        } catch (IOException e) {
            io.println("Connection to IEX failed. Please try again.");
        }
        return newsItems;
    }

    @Override
    public String toString() {
        return "\nCOMPANY INFORMATION FOR " + symbol + ": \n" +
                "Company name: " + companyName + "(" + website + ")\n" +
                "CEO: " + CEO + "\n" +
                "Sector: " + sector + " (" + industry + "),\n\n" +
                "Description:\n" + description + "\n";
    }

    //TODO: (PRIORITY 3) IT WOULD BE POSSIBLE TO ALSO DOWNLOAD COMPANY LOGO:
    // https://iextrading.com/developer/docs/#logo
}




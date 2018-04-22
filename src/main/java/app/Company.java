package app;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Company extends IEXdata {

    private final String symbol; //aktsia sümbol

    private String companyName;
    private String sector;
    private String industry; // täpsustab sektorit
    private String description; //ettevõtte tegevusalade kirjeldus
    private String CEO;
    private String website;


    //-----------------------------------------
    //Company information is loaded from IEX only if it is not yet stored in file:
    public Company(String symbol) throws Exception {
        this.symbol = symbol.toUpperCase();
        //loadFromFile();  // pole vajalik
        if (loadFromFile() == false) {
            loadDataFromWeb();
        }
    }

    //-----------------------------------------
    //Loading Company info from file:
    public boolean loadFromFile() throws IOException {
        File fail = new File("CompanyInfo.txt");

        try (Scanner sc = new Scanner(fail, "UTF-8")) {  //try-with-resources
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

                    System.out.println("Company info of " + symbol + " loaded from file.");
                    return true;
                }
            }
        }
        return false;
    }

    //-----------------------------------------
    //Loading Company info from web:
    public void loadDataFromWeb() {
        String URL = "https://api.iextrading.com/1.0/stock/" + symbol + "/company";

        try {
            JsonElement root = IEXdata.downloadData(URL);  // array or object
            JsonObject rootobj = root.getAsJsonObject();

            companyName = rootobj.get("companyName").getAsString(); //Apple Inc.
            sector = rootobj.get("sector").getAsString(); //"Technology"
            industry = rootobj.get("industry").getAsString(); // "Computer Hardware"
            description = rootobj.get("description").getAsString();
            CEO = rootobj.get("CEO").getAsString();  //Timothy D. Cook
            website = rootobj.get("website").getAsString();

            System.out.println("Company info of " + symbol + " downloaded from IEX.");


            //-----------------------------------------
            //Saving downloaded data to file:
            try (OutputStream out = new FileOutputStream("CompanyInfo.txt", true); //true- lisamine faili lõppu
                 OutputStreamWriter texToWrite = new OutputStreamWriter(out, "UTF-8");) {
                texToWrite.write(symbol + "////" + companyName + "////" + sector + "////" + industry + "////"
                        + description + "////" + CEO + "////" + website + "\n");
            } catch (IOException e) {
                System.out.println("Writing Company info of " + symbol + " to file failed.");
            }

        } catch (IOException e) {
            System.out.println("Connection to IEX failed. Please try again.");
        }

    }

    //Getting latest news on the company:
    public ArrayList<String> getCompanyNews(){

        ArrayList<String> newsItems=new ArrayList<>();
        String URL = "https://api.iextrading.com/1.0/stock/"+symbol+"/news";

        try {
            JsonElement root = IEXdata.downloadData(URL);  // array or object
            JsonArray rootArray = root.getAsJsonArray();

            for (JsonElement jsonElement : rootArray) {
                JsonObject jsonNewsItem=jsonElement.getAsJsonObject();
                String timeTemp=jsonNewsItem.get("datetime").getAsString();
                String time=timeTemp.substring(0,17);
                String headline=jsonNewsItem.get("headline").getAsString();
                String source=jsonNewsItem.get("source").getAsString();
                String website=jsonNewsItem.get("url").getAsString();
                String summary=jsonNewsItem.get("summary").getAsString();
                String related=jsonNewsItem.get("related").getAsString();

                NewsItem newsItem=new NewsItem(symbol, time, headline, source, website, summary,  related);
                newsItems.add(newsItem.toString());
            }

        } catch (IOException e) {
            System.out.println("Connection to IEX failed. Please try again.");
        }
        return newsItems;
    }

    @Override
    public String toString() {
        return "COMPANY INFORMATION FOR " + symbol + ": \n" +
                "Company name: " + companyName + "(" + website + ")\n" +
                "CEO: " + CEO + "\n" +
                "Sector: " + sector + "(" + industry + "),\n" +
                "Descriprion: " + description + "\n";
    }

    //TODO: (PRIORITY 1) CHECK IF VIEWING INFO FOR ONE COMPANY (USING COMPANY'S TOSTRING) IS AVAILABLE IN MAIN MENU.
    //TODO: (PRIORITY 3) IT WOULD BE POSSIBLE TO ALSO DOWNLOAD COMPANY LOGO:
    // https://iextrading.com/developer/docs/#logo
}




package app;

public class NewsItem extends IEXdata{
    String symbol;
    String time;
    String headline;
    String source;
    String website;
    String summary;
    String related;

    public NewsItem(String symbol, String time, String headline, String source,
                    String website, String summary, String related) {
        this.symbol = symbol;
        this.time = time;
        this.headline = headline;
        this.source = source;
        this.website = website;
        this.summary = summary;
        this.related = related;
    }

    @Override
    public String toString() {
        return time+": "+headline+"("+source+")\n"+summary+"\n("+website+")\nRelated: "+related+"\n";
    }
}

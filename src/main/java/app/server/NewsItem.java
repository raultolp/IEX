package app.server;

class NewsItem extends IEXdata {
    private final String time;
    private final String headline;
    private final String source;
    private final String website;
    private final String summary;
    private String related;

    public NewsItem(String symbol, String time, String headline, String source,
                    String website, String summary, String related) {
        this.time = time;
        this.headline = headline;
        this.source = source;
        this.website = website;
        this.summary = summary;
        this.related = related;
    }

    @Override
    public String toString() {
        return time + ": " + headline + "(" + source + ")\n" + summary + "\n(" + website + ")\nRelated: " + related + "\n";
    }
}

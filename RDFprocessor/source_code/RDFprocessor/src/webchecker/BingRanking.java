package webchecker;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * @author fet
 */
public class BingRanking {

    TagNode rootNode;

    public BingRanking() {
        System.setProperty("http.agent", "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)");
    }

    public Long getResultsNumber(String query) throws IOException {
        Long ranking = 0L;
        String url = "http://www.bing.com/search?q=\"" + URLEncoder.encode(query, "UTF-8") + "\"";
        HtmlCleaner cleaner = new HtmlCleaner();
        URL httpUrl = new URL(url);
        rootNode = cleaner.clean(httpUrl);
        String attribute = "id";
        String value = "count";
        TagNode results[] = rootNode.getElementsByAttValue(attribute, value, true, false);
        for (int i = 0; results != null && i < results.length; i++) {
            try {
                ranking = new Long(results[i].getText().toString().split("\\s+")[0].replace(".", ""));
            } catch (ArrayIndexOutOfBoundsException ex) {
            }
        }
        return ranking;
    }
}
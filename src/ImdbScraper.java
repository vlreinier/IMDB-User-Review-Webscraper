import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * This public class consists exclusively of non-static and public methods. Constructor is used to set an IMDB movie id.
 * <p>
 * If valid IMDB movie id is set, method reviewsToMap can extract x number of movie reviews from IMDB.
 * X number of movie reviews can be set with variable limit.
 * <p>
 * reviewsToMap extracts all user reviews from certain movies/series using paginationKeys (instead of the load more button).
 * Only approx. 25 reviews at a time are loaded onto the web page.
 * Each loaded review page contains such a paginationKey in HTML code. This code is used to call a separate HTML file using an AJAX link.
 * This called page contains the paginationKey for the next x number of reviews.
 * Say for 1000 reviews, the algorithm iterates around approx. 40 times, while checking if limit is reached.
 * Method reviewsToMap will print results to terminal, to inform user of the process and its possible flaws.
 * <p>
 * At last map with ratings and reviews can be written to CSV file using method CsvWriter.
 * CsvWriter will write to CSV using library 'java.io.BufferedWriter', separating columns by character '~'
 * <p>
 * Mainly used libraries: BufferedWriter and Jsoup
 */


public class ImdbScraper {
    private String movieid;

    /**
     * @param movieid set valid imdb movie id for 'this' variable in String format
     *                <p>
     *                constructor for class ImdbScraper
     */


    public ImdbScraper(String movieid) {
        this.movieid = movieid;
    }


    /**
     * @param limit integer limit can be set for getting x number of user reviews
     * @return Map(String, Double) with user reviews and corresponding user star rating
     * <p>
     * method retrieves x number of user reviews from IMDB if given movie ID is a valid IMDB movie ID
     * library JSOUP is used to retrieve targeted elements from HTML page
     */


    public Map<String, Double> reviewsToMap(int limit) {
        Map<String, Double> reviews = new HashMap<>();

        // if no valid movieid is found, return empty map
        if (movieid == null || movieid.length() == 0) {

            System.out.println("No valid movie ID was provided");
            return reviews;

        } else {
            try {
                // starting url for getting first ~25 reviews
                String url = "https://www.imdb.com/title/" + movieid + "/reviews/_ajax";

                while (reviews.size() < limit) {

                    Document data = Jsoup.connect(url).get();
                    Elements elements = data.select("div.lister-item-content");

                    for (Element content : elements) {

                        String title = content.select("a").text();
                        String rating = content.select("div.ipl-ratings-bar > span > span:nth-child(2)").text();
                        String review = content.select("div.content > div.text.show-more__control").text();

                        if (rating.length() > 0) {
                            if (reviews.size() == limit) break;
                            reviews.put(title + " " + review, Double.parseDouble(rating));
                        }
                    }
                    String datakey = null;
                    try {
                        datakey = data.select("body > div > div.load-more-data").first().attr("data-key");
                    } catch (Exception e) {
                        System.out.println("No more pagination keys can be found, reviews will be returned, " + reviews.size() + " user reviews are found");
                        return reviews;
                    }

                    if (datakey == null || datakey.length() == 0) {
                        return reviews;
                    } else {
                        url = "https://www.imdb.com/title/" + movieid + "/reviews/_ajax?paginationKey=" + datakey;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Limit is reached, " + reviews.size() + " user reviews are found");
            return reviews;
        }
    }


    /**
     * @param map       Map(String, Double) with user reviews and corresponding user ratings
     * @param filepath  path (incl. filename) for file destination in format String
     * @param separator integer separator for columns in targeted CSV file
     *                  <p>
     *                  Method writes user reviews with its corresponding rating to a CSV file separating columns by given separator
     *                  library BufferedWriter is used to write to file.
     */


    public void csvWriter(Map<String, Double> map, String filepath, String separator) {
        // remove spaces from filepath
        filepath = filepath.replace(" ", "_");

        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(filepath));

            for (Map.Entry<String, Double> string : map.entrySet()) {
                writer.write(string.getValue() + separator + string.getKey() + "\n");
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

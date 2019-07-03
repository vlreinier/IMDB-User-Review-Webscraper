package Testing;

import ImdbReviewScraper.ImdbScraper;
import ImdbReviewScraper.Omdb;

import java.util.Map;

/**
 * This main class is created for testing purposes. It shows a test case of how you might use this library.
 * All used directories should be changed before reusing it.
 * <p>
 * Some steps are explained using java comments
 */

public class ImdbScraperTest {

    public static void main(String[] args) {
        // define the movie you want to download reviews from
        String movie = "rambo";
        int limit = 1000;
        // define path for CSV file you want reviews to be written
        String path = "your filepath goes here"


        // define api key for using OMDB to find corresponding IMDB movie id
        // you can also choose to manually get the movie id from the IMDB url, to skip the OMDB part
        String omdb_apikey = "your api key goes here";
        // we have to obtain the IMDB movie ID for a url search on IMDB
        // this part can be skipped if movie id is manually found
        Omdb omdb = new Omdb(movie, omdb_apikey);
        // loadJson will use JSONObject and JSOUP to save information from OMDB, from html to JSON file
        omdb.loadJson();
        // movie id is retrieved from JSON file using JSONObject
        String movieid = omdb.getMovieId();


        // we create an imdb object with given movieid
        ImdbScraper imdbscraper = new ImdbScraper(movieid);
        // load reviews into map with argument limit for max to get reviews
        Map<String, Double> reviews = imdbscraper.reviewsToMap(limit);
        // if movie id was incorrect or nothing was found, an empty map will return, so we check for the map size
        // less then 200 reviews is not worthy and trustworthy enough
        if (reviews.size() > 200) {
            imdbscraper.csvWriter(reviews, path, "~");
        }

    }
}

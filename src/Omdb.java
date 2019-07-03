import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This public class consists exclusively of non-static and public methods.
 * Constructor is used to set an movie name and api key
 * <p>
 * This class is created to retrieve movie information for targeted movie.
 * For retrieving this information a free API from OMDB is used.
 * <p>
 * Jsoup will retrieve the HTML file and JSONObject will convert this into a usable JSON datastructure.
 * Mainly used libraries: JSONObject and Jsoup
 */


public class Omdb {
    private String movie, apikey;
    private JSONObject json;

    /**
     *
     * @param movie  target movie information should be searched for
     * @param apikey api key for accessing OMDB database / environment
     *               <p>
     *               constructor for class Omdb to set targeted movie name and api key for OMDB
     */

    public Omdb(String movie, String apikey) {
        this.movie = movie.replaceAll("[^a-zA-Z0-9'\\-]+", "+").replace(" ", "+");
        this.apikey = apikey;
    }

    /**
     *
     * method loads html page (omdb) for given movie id and saves information in JSON format
     */

    public void loadJson() {
        String url = "http://www.omdbapi.com/?t=" + movie + "&apikey=" + apikey + "&plot=full&r=json";

        try {
            String data = Jsoup.connect(url).ignoreContentType(true).execute().body();
            this.json = new JSONObject(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return method loads imdbID from this.json
     */

    public String getMovieId() {
        return json.getString("imdbID");
    }

    /**
     *
     * @return method loads Runtime from this.json
     */

    public String getRunTime() {
        return json.getString("Runtime");
    }

    /**
     *
     * @return method loads Genre from this.json
     */

    public String getGenre() {
        return json.getString("Genre");
    }

    /**
     *
     * @return method loads Released from this.json
     */

    public String getReleaseDate() {
        return json.getString("Released");
    }

    /**
     *
     * @return method loads imdbRating from this.json
     */

    public String getImdbRating() {
        return json.getString("imdbRating");
    }

    /**
     *
     * @return method loads Poster from this.json
     */

    public String getPosterLink() {
        return json.getString("Poster");
    }

    /**
     *
     * @return method loads Plot from this.json
     */

    public String getPlot() {
        return json.getString("Plot");
    }

    /**
     *
     * @return method loads Writer from this.json
     */

    public String getWriter() {
        return json.getString("Writer");
    }

    /**
     *
     * @return method loads Actors from this.json
     */

    public String getActors() {
        return json.getString("Actors");
    }

    /**
     *
     * @return method loads Awards from this.json
     */

    public String getAwards() {
        return json.getString("Awards");
    }

    /**
     *
     * @return method loads Language from this.json
     */

    public String getLanguage() {
        return json.getString("Language");
    }

    /**
     *
     * @return method loads Country from this.json
     */

    public String getCountry() {
        return json.getString("Country");
    }

    /**
     *
     * @return method loads Production from this.json
     */

    public String getProduction() {
        return json.getString("Production");
    }

    /**
     *
     * @return method loads Director from this.json
     */

    public String getDirector() {
        return json.getString("Director");
    }

    /**
     *
     * @return method loads ratings from this.json
     */

    public List<String> getRatings() {
        List<String> ratings = new ArrayList<>();
        JSONArray jsonArray = json.getJSONArray("Ratings");

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                ratings.add(jsonArray.get(i).toString());
            }
        }
        return ratings;
    }
}

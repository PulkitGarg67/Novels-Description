package cic.du.ac.in.recylerviewandcardview.Utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scorpion on 10/26/2018.
 */

public final class QueryUtils {

    static String  test = "https://www.googleapis.com/books/v1/volumes?";
    private QueryUtils(){
    }

    public static ArrayList<Book> extractBooks(String requestURL){
        ArrayList<Book> list = new ArrayList<>();
        Uri builtURI = Uri.parse(requestURL).buildUpon()
                .appendQueryParameter("q", "harry potter")
                .appendQueryParameter("maxResults", "40")
                .appendQueryParameter("printType", "books")
                .build();

        URL url = null;
        try {
            url = new URL(builtURI.toString());
            Log.i("URL",url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ;
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i("jsresp",jsonResponse);
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            if(!jsonResponse.isEmpty())
                list = extractFeatureFromJson(jsonResponse);
        } catch (IOException e) {
            Log.e("1",e.getMessage());
        }
        return list;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("3 : ", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("4 : ", "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Book> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        ArrayList<Book> books = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");
            Log.i("items", String.valueOf(itemsArray.length()));
            // If there are results in the features array
            for (int i = 0; i < itemsArray.length(); i++) {
                // Extract out the first feature (which is an earthquake)
                JSONObject firstBook = itemsArray.getJSONObject(i);
                JSONObject info = firstBook.getJSONObject("volumeInfo");

                // Extract out the title, number of people, and perceived strength values
                if(info.isNull( "title")){
                    continue;
                }
                String title = info.getString("title");
                String description;
                if(info.isNull( "description")){
                    description = null;
                }else {
                    description = info.getString("description");
                }
                String imageURL = "";
                if(info.has("imageLinks")){
                JSONObject imglinks = info.getJSONObject("imageLinks");
                    if(imglinks.isNull( "thumbnail")){
                        imageURL = null;
                    }else {
                        imageURL = imglinks.getString("thumbnail");
                    }
                }

                // Create a new {@link Event} object
                Book book =  new Book(title, description, imageURL);

                books.add(book);
            }
        } catch (JSONException e) {
            Log.e("5 : ", "Problem parsing the earthquake JSON results", e);
        }
        return books;
    }
}

package com.example.maria.booklisting;

import android.graphics.drawable.Drawable;
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

import static com.example.maria.booklisting.BooksSearchActivity.LOG_TAG;

public final class QueryUtils {

    private QueryUtils() {
    }

    public static List<Book> fetchBookData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Book> books = extractFeatureFromJson(jsonResponse);
        return books;
    }

    private static ArrayList<Book> extractFeatureFromJson(String bookJSON) {

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        ArrayList<Book> books = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++) {

                JSONObject currentBook = bookArray.getJSONObject(i);

                JSONObject items = currentBook.getJSONObject("volumeInfo");

                String title = "";
                if(items.has("title")) {
                    title = items.getString("title");
                }

                String publisher="";
                if(items.has("publisher")) {
                    publisher = items.getString("publisher");
                }

                String publishedDate="";
                if(items.has("publishedDate")){
                    publishedDate = items.getString("publishedDate");
                }

                String infoLink = items.getString("infoLink");

                String authors = "";
                if(items.has("authors")) {
                    JSONArray authorsArray = items.getJSONArray("authors");
                    for (int j = 0; j < authorsArray.length(); j++) {
                        authors += authorsArray.getString(j);
                        if (j + 1 < authorsArray.length())
                            authors += ", ";
                    }
                }

                Drawable bookImage;
                if(items.has("imageLinks")) {
                    JSONObject imageLinks = items.getJSONObject("imageLinks");
                    String imageUrl = imageLinks.getString("smallThumbnail");
                    bookImage=LoadImageFromWebOperations(imageUrl);
                }
                else {
                    bookImage= BooksSearchActivity.getAppContext().getResources().getDrawable( R.drawable.no_cover_thumb );
                }

                Book book = new Book(title, publisher, authors, publishedDate, infoLink, bookImage);

                books.add(book);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
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

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
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

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            return null;
        }
    }
}

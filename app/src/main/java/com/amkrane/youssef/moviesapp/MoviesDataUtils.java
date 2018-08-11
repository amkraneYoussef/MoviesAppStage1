package com.amkrane.youssef.moviesapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MoviesDataUtils {
    private static String MovieData = "";
    private static moviesDataAdapter mAdapter;
    public static UpdateUiInterface updateUi=null;

    public static void getMoviesList(Context context, moviesDataAdapter mAdapter1, String sortOrder)
    {
        MoviesDataUtils.mAdapter = mAdapter1;
        try
        {
            URL url = new URL(context.getString(R.string.movies_base_url) + sortOrder +
                                                context.getString(R.string.api_key));
            LoadMoviesTask loadMoviesTask = new LoadMoviesTask();
            loadMoviesTask.execute(url);
        }catch (Exception e){
            Log.e("Error","Error : "+ e.getMessage());
    }
    }

    static class LoadMoviesTask extends AsyncTask<URL,Void, String>
    {
        @Override
        protected String doInBackground(URL... urls) {
            String res = "";
            try
            {
               res =  getMoviesFromWeb(urls[0]);
            }catch (Exception e){
                Log.e("Error","Error : "+ e.getMessage());
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MovieData = result;
            mAdapter.setData(parseMovieJSONToList());
            updateUi.showResults();
        }
    }
    private static String getMoviesFromWeb(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }

    private static List<movie> parseMovieJSONToList()
    {
        if(MovieData == null || MovieData.isEmpty()) return  null;
        List<movie> MoviesList = new ArrayList<>();
        JSONObject jsonData;
        try
        {
            jsonData = new JSONObject(MovieData);
            JSONArray results = jsonData.getJSONArray("results");
            for (int i= 0; i<results.length();i++)
            {
                jsonData = results.getJSONObject(i); // use jsonData to store each single movie json data
                // map values of the movie to a new movie and add it to the list
                movie mMovie = new movie();
                mMovie.setOriginalTitle(jsonData.getString("title"));
                mMovie.setOverview(jsonData.getString(("overview")));
                mMovie.setReleaseDate(jsonData.getString("release_date"));
                mMovie.setPosterImage(jsonData.getString("poster_path"));
                mMovie.setRating(jsonData.getString("vote_average"));
                MoviesList.add(mMovie);
            }
            return MoviesList;
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPosterUrl(Context context,String PosterID,String size)
    {
        return context.getResources().getString(R.string.poster_base_url) +
                size +
                PosterID;
    }

    public interface UpdateUiInterface
    {
        void showResults();
    }
}

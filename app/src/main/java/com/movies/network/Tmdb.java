package com.movies.network;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.movies.models.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tmdb {

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String SEARCH_URL = "https://api.themoviedb.org/3/search/movie";
    private static final String bearerToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYWUxMzBhODE1ZTVkOGY3NTAxYTFjOGZhZWI1NmM2ZSIsInN1YiI6IjY1NWFiNTU1MDgxNmM3MDBlMDFiYzYxYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.mJHhOLiACm1FUfY97TSw_Ytk8E2K6NriMoxePrLcyjs";

    public interface MovieFetchCallback {
        void onSuccess(List<Movie> movies);
        void onError(String errorMessage);
    }

    public static void fetchMovies(Context context, String url, MovieFetchCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Movie> movies = new ArrayList<>();
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject movie = results.getJSONObject(i);
                                String title = movie.getString("title");
                                String overview = movie.getString("overview");
                                String posterPath = movie.getString("poster_path");
                                String backdropPath = movie.getString("backdrop_path");
                                String releaseYear = movie.getString("release_date");
                                String rating = movie.getString("vote_average");

                                movies.add(new Movie(title, overview, posterPath, backdropPath, releaseYear, rating));
                            }
                            callback.onSuccess(movies);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error parsing data");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callback.onError("Failed to fetch data");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", bearerToken);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static void getNowPlaying(Context context, MovieFetchCallback callback) {
        String url = BASE_URL + "/now_playing";
        fetchMovies(context, url, callback);
    }

    public static void getUpcoming(Context context, MovieFetchCallback callback) {
        String url = BASE_URL + "/upcoming";
        fetchMovies(context, url, callback);
    }

    public static void getPopular(Context context, MovieFetchCallback callback) {
        String url = BASE_URL + "/popular";
        fetchMovies(context, url, callback);
    }

    public static void getTopRated(Context context, MovieFetchCallback callback) {
        String url = BASE_URL + "/top_rated";
        fetchMovies(context, url, callback);
    }

    public static void searchMovies(Context context, String query, MovieFetchCallback callback) {
        String url = SEARCH_URL + "?query=" + query;
        fetchMovies(context, url, callback);
    }
}

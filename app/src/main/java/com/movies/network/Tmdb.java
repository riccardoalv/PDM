package com.movies.network;

import android.content.Context;
import com.movies.models.Movie;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class Tmdb {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmYWUxMzBhODE1ZTVkOGY3NTAxYTFjOGZhZWI1NmM2ZSIsInN1YiI6IjY1NWFiNTU1MDgxNmM3MDBlMDFiYzYxYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.mJHhOLiACm1FUfY97TSw_Ytk8E2K6NriMoxePrLcyjs";

    // Retrofit interface for TMDb API
    public interface TmdbApi {
        @GET("movie/now_playing")
        Call<JsonObject> getNowPlaying(@Header("Authorization") String authorization);

        @GET("movie/upcoming")
        Call<JsonObject> getUpcoming(@Header("Authorization") String authorization);

        @GET("movie/popular")
        Call<JsonObject> getPopular(@Header("Authorization") String authorization);

        @GET("movie/top_rated")
        Call<JsonObject> getTopRated(@Header("Authorization") String authorization);

        @GET("search/movie")
        Call<JsonObject> searchMovies(@Header("Authorization") String authorization, @Query("query") String query);
    }

    private static TmdbApi createApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TmdbApi.class);
    }

    public interface MovieFetchCallback {
        void onSuccess(List<Movie> movies);
        void onError(String errorMessage);
    }

    private static List<Movie> parseMovies(JsonObject response) {
        List<Movie> movies = new ArrayList<>();
        try {
            if (response.has("results")) {
                response.getAsJsonArray("results").forEach(item -> {
                    JsonObject movie = item.getAsJsonObject();
                    String title = movie.get("title").getAsString();
                    String overview = movie.get("overview").getAsString();
                    String posterPath = movie.get("poster_path").getAsString();
                    String backdropPath = movie.get("backdrop_path").getAsString();
                    String releaseDate = movie.get("release_date").getAsString();
                    String rating = movie.get("vote_average").getAsString();
                    movies.add(new Movie(title, overview, posterPath, backdropPath, releaseDate, rating));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static void getNowPlaying(Context context, MovieFetchCallback callback) {
        TmdbApi api = createApi();
        api.getNowPlaying(BEARER_TOKEN).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = parseMovies(response.body());
                    callback.onSuccess(movies);
                } else {
                    callback.onError("Failed to fetch now playing movies");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError("Error: " + t.getMessage());
            }
        });
    }

    public static void getUpcoming(Context context, MovieFetchCallback callback) {
        TmdbApi api = createApi();
        api.getUpcoming(BEARER_TOKEN).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = parseMovies(response.body());
                    callback.onSuccess(movies);
                } else {
                    callback.onError("Failed to fetch upcoming movies");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError("Error: " + t.getMessage());
            }
        });
    }

    public static void getPopular(Context context, MovieFetchCallback callback) {
        TmdbApi api = createApi();
        api.getPopular(BEARER_TOKEN).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = parseMovies(response.body());
                    callback.onSuccess(movies);
                } else {
                    callback.onError("Failed to fetch popular movies");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError("Error: " + t.getMessage());
            }
        });
    }

    public static void getTopRated(Context context, MovieFetchCallback callback) {
        TmdbApi api = createApi();
        api.getTopRated(BEARER_TOKEN).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = parseMovies(response.body());
                    callback.onSuccess(movies);
                } else {
                    callback.onError("Failed to fetch top-rated movies");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError("Error: " + t.getMessage());
            }
        });
    }

    public static void searchMovies(Context context, String query, MovieFetchCallback callback) {
        TmdbApi api = createApi();
        api.searchMovies(BEARER_TOKEN, query).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = parseMovies(response.body());
                    callback.onSuccess(movies);
                } else {
                    callback.onError("Failed to search movies");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError("Error: " + t.getMessage());
            }
        });
    }
}

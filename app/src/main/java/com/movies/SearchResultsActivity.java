package com.movies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.movies.adapters.SearchMovieAdapter;
import com.movies.models.Movie;
import com.movies.network.Tmdb;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView searchResultsRecyclerView;
    private SearchMovieAdapter movieAdapter;
    private List<Movie> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Initialize back button
        ImageView backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> onBackPressed());
        }

        // Initialize RecyclerView
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize list and adapter
        searchResults = new ArrayList<>();
        movieAdapter = new SearchMovieAdapter(this, searchResults, movie -> {
            // Handle click events on movies
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("MOVIE_TITLE", movie.getTitle());
            intent.putExtra("MOVIE_DESCRIPTION", movie.getOverview());
            intent.putExtra("POSTER_PATH", "https://image.tmdb.org/t/p/w500" + movie.getPosterPath());
            intent.putExtra("BACKDROP_PATH", "https://image.tmdb.org/t/p/original" + movie.getBackdropPath());
            startActivity(intent);
        });
        searchResultsRecyclerView.setAdapter(movieAdapter);

        // Fetch search query from intent
        String query = getIntent().getStringExtra("QUERY");
        if (query != null && !query.trim().isEmpty()) {
            performSearch(query);
        } else {
            Toast.makeText(this, "No search query provided.", Toast.LENGTH_SHORT).show();
        }
    }

    private void performSearch(String query) {
        Tmdb.searchMovies(this, query, new Tmdb.MovieFetchCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                if (movies != null && !movies.isEmpty()) {
                    searchResults.clear();
                    searchResults.addAll(movies);
                    movieAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SearchResultsActivity.this, "No results found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(SearchResultsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.movies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.tabs.TabLayout;
import com.movies.adapters.MovieAdapter;
import com.movies.models.Movie;
import com.movies.network.Tmdb;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movies;
    private TabLayout tabLayout;
    private TextInputEditText searchInput;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Grid with 3 columns

        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                Intent intent = new Intent(this, SearchResultsActivity.class);
                intent.putExtra("QUERY", query);
                startActivity(intent);
            } else {
                showToast("Please enter a search query.");
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fetchNowPlayingMovies();
                        break;
                    case 1:
                        fetchUpcomingMovies();
                        break;
                    case 2:
                        fetchPopularMovies();
                        break;
                    case 3:
                        fetchTopRatedMovies();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        movies = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movies, this);
        recyclerView.setAdapter(movieAdapter);

        fetchNowPlayingMovies();
    }

    private void fetchNowPlayingMovies() {
        Tmdb.getNowPlaying(this, new Tmdb.MovieFetchCallback() {
            @Override
            public void onSuccess(List<Movie> fetchedMovies) {
                updateRecyclerView(fetchedMovies);
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    private void fetchUpcomingMovies() {
        Tmdb.getUpcoming(this, new Tmdb.MovieFetchCallback() {
            @Override
            public void onSuccess(List<Movie> fetchedMovies) {
                updateRecyclerView(fetchedMovies);
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    private void fetchPopularMovies() {
        Tmdb.getPopular(this, new Tmdb.MovieFetchCallback() {
            @Override
            public void onSuccess(List<Movie> fetchedMovies) {
                updateRecyclerView(fetchedMovies);
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    private void fetchTopRatedMovies() {
        Tmdb.getTopRated(this, new Tmdb.MovieFetchCallback() {
            @Override
            public void onSuccess(List<Movie> fetchedMovies) {
                updateRecyclerView(fetchedMovies);
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    private void updateRecyclerView(List<Movie> fetchedMovies) {
        movies.clear();
        movies.addAll(fetchedMovies);
        movieAdapter.notifyDataSetChanged();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        Movie clickedMovie = movies.get(position);

        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("MOVIE_TITLE", clickedMovie.getTitle());
        intent.putExtra("MOVIE_DESCRIPTION", clickedMovie.getOverview());
        intent.putExtra("POSTER_PATH", "https://image.tmdb.org/t/p/w500" + clickedMovie.getPosterPath());
        intent.putExtra("BACKDROP_PATH", "https://image.tmdb.org/t/p/original" + clickedMovie.getBackdropPath());
        startActivity(intent);
    }
}


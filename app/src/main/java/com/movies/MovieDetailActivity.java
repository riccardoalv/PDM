package com.movies;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Retrieve data from the Intent
        String title = getIntent().getStringExtra("MOVIE_TITLE");
        String description = getIntent().getStringExtra("MOVIE_DESCRIPTION");
        String backdropPath = getIntent().getStringExtra("BACKDROP_PATH");
        String posterPath = getIntent().getStringExtra("POSTER_PATH");

        // Find views in the layout
        TextView movieTitle = findViewById(R.id.movieTitle);
        TextView movieDescription = findViewById(R.id.movieDescription);
        ImageView movieBackdrop = findViewById(R.id.movieBackdrop);
        ImageView moviePoster = findViewById(R.id.moviePoster);
        ImageView backButton = findViewById(R.id.backButton);

        // Set data to views
        movieTitle.setText(title);
        movieDescription.setText(description);
        Glide.with(this).load(backdropPath).into(movieBackdrop);
        Glide.with(this).load(posterPath).into(moviePoster);

        // Set back button functionality
        backButton.setOnClickListener(v -> onBackPressed());
    }
}


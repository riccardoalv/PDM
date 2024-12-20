package com.movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.movies.R;
import com.movies.models.Movie;
import java.util.List;

public class SearchMovieAdapter extends RecyclerView.Adapter<SearchMovieAdapter.SearchMovieViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private OnMovieClickListener onMovieClickListener;

    // Constructor
    public SearchMovieAdapter(Context context, List<Movie> movieList, OnMovieClickListener onMovieClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.onMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public SearchMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the search result layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new SearchMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchMovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // Bind data to the views
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .into(holder.moviePoster);

        holder.movieTitle.setText(movie.getTitle());
        holder.movieYear.setText(String.valueOf(movie.getReleaseYear()));
        holder.movieLanguage.setText(movie.getLanguage());
        holder.movieRating.setText(String.valueOf(movie.getRating()));

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (onMovieClickListener != null) {
                onMovieClickListener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class SearchMovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle;
        TextView movieYear;
        TextView movieLanguage;
        TextView movieRating;

        public SearchMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieYear = itemView.findViewById(R.id.movieYear);
            movieLanguage = itemView.findViewById(R.id.movieLanguage);
            movieRating = itemView.findViewById(R.id.movieRating);
        }
    }

    // Listener interface for handling movie item clicks
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
}

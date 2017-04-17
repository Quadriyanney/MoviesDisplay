package com.quadriyanney.moviesdisplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MoviesDetail extends AppCompatActivity {

    public String thumbnail, overview, title, releaseDate;
    public int voteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);

        Bundle data = getIntent().getExtras();

        thumbnail = data.getString("thumbnail");
        title = data.getString("title");
        overview = data.getString("overview");
        releaseDate = data.getString("date");
        voteAverage = data.getInt("vote");

        ImageView movieThumbnail = (ImageView) findViewById(R.id.detail_thumbnail);

        TextView movieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        movieReleaseDate.setText(releaseDate);

        TextView movieTitle = (TextView) findViewById(R.id.movie_title);
        movieTitle.setText(title);

        TextView movieOverview = (TextView) findViewById(R.id.movie_overview);
        movieOverview.setText(overview);

        TextView movieVoteAverage = (TextView) findViewById(R.id.movie_vote_average);
        movieVoteAverage.setText(String.valueOf(voteAverage));

        Glide.with(getBaseContext()).load("http://image.tmdb.org/t/p/w185/" + thumbnail).into(movieThumbnail);
    }
}

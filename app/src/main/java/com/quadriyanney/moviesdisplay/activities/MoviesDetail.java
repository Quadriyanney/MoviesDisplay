package com.quadriyanney.moviesdisplay.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.quadriyanney.moviesdisplay.Controller;
import com.quadriyanney.moviesdisplay.R;
import com.quadriyanney.moviesdisplay.adapters.ReviewsAdapter;
import com.quadriyanney.moviesdisplay.adapters.TrailersAdapter;
import com.quadriyanney.moviesdisplay.data.MovieContract;
import com.quadriyanney.moviesdisplay.data.ReviewsInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesDetail extends AppCompatActivity implements TrailersAdapter.ListItemClickListener{

    String thumbnail, overview, title, releaseDate, reviewQuery, author, review, trailerQuery, voteAverage, movie_id, key;
    int counter = 0, iterator = 0;
    List<String> trailersList;
    List<ReviewsInfo> reviewsList;
    JSONArray jsonArray1, jsonArray2;
    ReviewsAdapter reviewsAdapter;
    TrailersAdapter trailersAdapter;
    Cursor cursor;
    CheckBox favButton;
    Button trailerButton, reviewButton, shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);

        favButton = (CheckBox) findViewById(R.id.btn_favorite);

        thumbnail = getIntent().getExtras().getString("thumbnail");
        title = getIntent().getExtras().getString("title");
        overview = getIntent().getExtras().getString("overview");
        releaseDate = getIntent().getExtras().getString("date");
        voteAverage = getIntent().getExtras().getString("vote");
        movie_id = getIntent().getExtras().getString("id");

        reviewQuery = new Uri.Builder().scheme("http")
                .authority(getResources().getString(R.string.base_url)).appendPath("3")
                .appendPath("movie").appendPath(movie_id)
                .appendPath("reviews").appendQueryParameter("api_key", getResources().getString(R.string.api_key)).toString();

        trailerQuery = new Uri.Builder().scheme("http")
                .authority(getResources().getString(R.string.base_url)).appendPath("3")
                .appendPath("movie").appendPath(movie_id)
                .appendPath("videos").appendQueryParameter("api_key", getResources().getString(R.string.api_key)).toString();

        ImageView movieThumbnail = (ImageView) findViewById(R.id.detail_thumbnail);

        TextView movieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        movieReleaseDate.setText(releaseDate);

        TextView movieTitle = (TextView) findViewById(R.id.movie_title);
        movieTitle.setText(title);

        TextView movieOverview = (TextView) findViewById(R.id.movie_overview);
        movieOverview.setText(overview);

        TextView movieVoteAverage = (TextView) findViewById(R.id.movie_vote_average);
        movieVoteAverage.setText(String.valueOf(voteAverage));

        Glide.with(getBaseContext())
                .load(getResources().getString(R.string.imageBaseUrl) + thumbnail).into(movieThumbnail);

        trailerButton = (Button) findViewById(R.id.trailerButton);
        reviewButton = (Button) findViewById(R.id.reviewButton);

        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.reviewsRecyclerView);
        reviewsList = new ArrayList<>();
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        reviewsAdapter = new ReviewsAdapter(reviewsList);
        recyclerView1.setAdapter(reviewsAdapter);

        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.trailersRecyclerView);
        trailersList = new ArrayList<>();
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        trailersAdapter = new TrailersAdapter(trailersList, this);
        recyclerView2.setAdapter(trailersAdapter);

        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView2.setNestedScrollingEnabled(false);

        getReviews();
        getTrailers();

        cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movie_id).build(),
                null, null, null, null);
        assert cursor != null;
        if (cursor.getCount() > 0){
            favButton.setChecked(true);
        }
    }

    public void OnClicks(View v){
        shareButton = (Button) findViewById(R.id.btn_share);
        if (v == shareButton){
            String message = "Checkout the trailer for " + title;

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (intent.resolveActivity(getPackageManager()) != null){
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
            }
        }
        else if (v == favButton){
            if (favButton.isChecked()){
                addAsFavorite(thumbnail, title, overview, releaseDate, voteAverage, movie_id);
                Toast.makeText(getBaseContext(), "Added as favourite", Toast.LENGTH_SHORT).show();
            }
            else {
                getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movie_id).build(),
                        null,
                        null);
                Toast.makeText(getBaseContext(), "Removed from favourite", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v == trailerButton){
            Toast.makeText(getBaseContext(), "Getting Trailers", Toast.LENGTH_SHORT).show();
            trailerButton.setVisibility(View.GONE);
            getTrailers();
        }
        else if (v == reviewButton){
            Toast.makeText(getBaseContext(), "Getting Reviews", Toast.LENGTH_SHORT).show();
            reviewButton.setVisibility(View.GONE);
            getReviews();
        }
    }

    private void addAsFavorite(String thumbnail, String title, String overview, String releaseDate, String voteAverage, String movieId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_THUMBNAIL, thumbnail);
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
        contentValues.put(MovieContract.MovieEntry.COLUMN_DATE, releaseDate);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE, voteAverage);
        contentValues.put(MovieContract.MovieEntry.COLUMN_ID, movieId);

        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
    }

    @Override
    public void onListItemClick(int clicked) {
        String url= getResources().getString(R.string.youTubeBaseUrl) + trailersList.get(clicked);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void getReviews(){
        JsonObjectRequest request = new JsonObjectRequest(reviewQuery, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response1) {
                        try {
                            jsonArray1 = response1.getJSONArray("results");

                            while (counter < jsonArray1.length()){
                                author = jsonArray1.getJSONObject(counter).getString("author");
                                review = jsonArray1.getJSONObject(counter).getString("content");

                                reviewsList.add(new ReviewsInfo(author, review));
                                counter++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        reviewsAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        reviewButton.setVisibility(View.VISIBLE);
                        Toast.makeText(getBaseContext(), "Couldn't get reviews", Toast.LENGTH_SHORT).show();
                    }
                });
        Controller.getInstance().addToRequestQueue(request, "tag_review");
    }

    public void getTrailers(){
        JsonObjectRequest request = new JsonObjectRequest(trailerQuery, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response2) {
                        try {
                            jsonArray2 = response2.getJSONArray("results");

                            while (iterator < jsonArray2.length()) {
                                key = jsonArray2.getJSONObject(iterator).getString("key");
                                trailersList.add(key);

                                iterator++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        trailersAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        trailerButton.setVisibility(View.VISIBLE);
                        Toast.makeText(getBaseContext(), "Couldn't get trailers", Toast.LENGTH_SHORT).show();
                    }
                });
        Controller.getInstance().addToRequestQueue(request, "tag_trailer");
    }
}
package com.quadriyanney.moviesdisplay.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.quadriyanney.moviesdisplay.Controller;
import com.quadriyanney.moviesdisplay.R;
import com.quadriyanney.moviesdisplay.adapters.MovieAdapter;
import com.quadriyanney.moviesdisplay.data.MovieContract;
import com.quadriyanney.moviesdisplay.data.MoviesInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviesTop extends AppCompatActivity implements MovieAdapter.ListItemClickListener, NavigationView.OnNavigationItemSelectedListener{

    String top_ratedQuery, overview, thumbnailURL, title, releaseDate, voteAverage, movie_id;
    JSONArray jsonArray;
    int counter = 0;
    Toolbar toolbar;
    private ArrayList<MoviesInfo> movies_list;
    MovieAdapter adapter;
    DrawerLayout drawer;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_top);

        toolbar = (Toolbar) findViewById(R.id.favourite_toolbar);
        toolbar.setTitle(getResources().getString(R.string.top_rated_movies));
        setSupportActionBar(toolbar);

        navDrawer = (NavigationView) findViewById(R.id.drawer);
        drawer = (DrawerLayout) findViewById(R.id.favourite_layout);

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navDrawer.setNavigationItemSelectedListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.topRecycler);

        if (savedInstanceState != null){
            movies_list = savedInstanceState.getParcelableArrayList("movies");
            adapter = new MovieAdapter(this, movies_list, this);
            recyclerView.setAdapter(adapter);
        }
        else {
            top_ratedQuery = new Uri.Builder().scheme("http")
                    .authority(getResources().getString(R.string.base_url)).appendPath("3")
                    .appendPath("movie").appendPath("top_rated")
                    .appendQueryParameter("api_key", getResources().getString(R.string.api_key)).toString();

            movies_list = new ArrayList<>();
            adapter = new MovieAdapter(this, movies_list, this);
            recyclerView.setAdapter(adapter);
            setUp();
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onListItemClick(int clicked) {
        Intent intent = new Intent(MoviesTop.this, MoviesDetail.class);
        intent.putExtra("thumbnail", movies_list.get(clicked).getThumbnailUrl());
        intent.putExtra("overview", movies_list.get(clicked).getOverview());
        intent.putExtra("title", movies_list.get(clicked).getTitle());
        intent.putExtra("vote", movies_list.get(clicked).getVoteAverage());
        intent.putExtra("date", movies_list.get(clicked).getReleaseDate());
        intent.putExtra("id", movies_list.get(clicked).getMovie_id());
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.popular){
            Intent i = new Intent(MoviesTop.this, MoviesPopular.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.favourite){

            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);

            assert cursor != null;
            if (cursor.getCount() > 0){
                cursor.close();
                Intent i = new Intent(MoviesTop.this, MoviesFavourite.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
            else {
                drawer.closeDrawers();
                Toast.makeText(getBaseContext(), "You don't have any favourite movies", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            drawer.closeDrawers();
            Toast.makeText(getBaseContext(), "Already sorted by top_rated", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void setUp(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Recipes...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(top_ratedQuery, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {
                            jsonArray = response.getJSONArray("results");

                            while (counter < jsonArray.length()) {
                                overview = jsonArray.getJSONObject(counter).getString("overview");
                                thumbnailURL = jsonArray.getJSONObject(counter).getString("poster_path");
                                voteAverage = String.valueOf(jsonArray.getJSONObject(counter).getInt("vote_average"));
                                title = jsonArray.getJSONObject(counter).getString("original_title");
                                releaseDate = jsonArray.getJSONObject(counter).getString("release_date");
                                movie_id = String.valueOf(jsonArray.getJSONObject(counter).getInt("id"));

                                movies_list.add(new MoviesInfo(thumbnailURL, voteAverage, overview, title, releaseDate, movie_id));
                                counter++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Snackbar.make(drawer, "Poor/No Connection", Snackbar.LENGTH_INDEFINITE).setAction(
                                "Refresh", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        setUp();
                                    }
                                }
                        ).show();
                    }
                });
        Controller.getInstance().addToRequestQueue(request, "tag");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movies_list != null){
            outState.putParcelableArrayList("movies", movies_list);
        }
    }
}
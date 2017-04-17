package com.quadriyanney.moviesdisplay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MoviesPopular extends AppCompatActivity implements MovieAdapter.ListItemClickListener, NavigationView.OnNavigationItemSelectedListener{

    String popularQuery, popularJSON, json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Toolbar toolbar;
    private List<MoviesInfo> movies_list;
    MovieAdapter adapter;
    DrawerLayout drawer;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        new Background().execute();

        toolbar = (Toolbar) findViewById(R.id.popular_toolbar);
        toolbar.setTitle("Popular Movies");
        setSupportActionBar(toolbar);

        navDrawer = (NavigationView) findViewById(R.id.drawer);
        navDrawer.setNavigationItemSelectedListener(this);

        drawer = (DrawerLayout) findViewById(R.id.activity_movies);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movies_list);
        movies_list = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new MovieAdapter(this, movies_list, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onListItemClick(int clicked) {

        Intent intent = new Intent(MoviesPopular.this, MoviesDetail.class);
        intent.putExtra("thumbnail", movies_list.get(clicked).getThumbnailUrl());
        intent.putExtra("overview", movies_list.get(clicked).getOverview());
        intent.putExtra("title", movies_list.get(clicked).getTitle());
        intent.putExtra("vote", movies_list.get(clicked).getVoteAverage());
        intent.putExtra("date", movies_list.get(clicked).getReleaseDate());
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.top_rated){
            Intent i = new Intent(MoviesPopular.this, MoviesTop.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        return false;
    }


    public class Background extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(MoviesPopular.this);

        @Override
        protected void onPreExecute() {
            popularQuery = "http://api.themoviedb.org/3/movie/popular?api_key=<<>>";
            progressDialog.setMessage("Fetching Data...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(popularQuery);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((json_string = bufferedReader.readLine()) != null){
                    stringBuilder.append(json_string).append("\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            popularJSON = s;

            if (popularJSON == null){
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(drawer, "Poor/No Connection", Snackbar.LENGTH_INDEFINITE).setAction(
                        "Refresh", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new Background().execute();
                            }
                        }
                );
                snackbar.show();
            }
            else {
                try {
                    progressDialog.dismiss();
                    jsonObject = new JSONObject(popularJSON);
                    jsonArray = jsonObject.getJSONArray("results");

                    int counter = 0;

                    String overview, thumbnailURL, title, releaseDate;
                    int voteAverage;

                    while (counter < jsonArray.length()){

                        JSONObject json = jsonArray.getJSONObject(counter);

                        overview = json.getString("overview");
                        thumbnailURL = json.getString("poster_path");
                        voteAverage = json.getInt("vote_average");
                        title = json.getString("original_title");
                        releaseDate = json.getString("release_date");

                        MoviesInfo info = new MoviesInfo(thumbnailURL, voteAverage, overview, title, releaseDate);
                        movies_list.add(info);
                        counter++;
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}

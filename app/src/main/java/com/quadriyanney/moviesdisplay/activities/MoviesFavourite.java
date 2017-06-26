package com.quadriyanney.moviesdisplay.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.quadriyanney.moviesdisplay.R;
import com.quadriyanney.moviesdisplay.adapters.MovieAdapter;
import com.quadriyanney.moviesdisplay.data.DatabaseManager;
import com.quadriyanney.moviesdisplay.data.MovieContract;
import com.quadriyanney.moviesdisplay.data.MoviesInfo;

import java.util.ArrayList;

public class MoviesFavourite extends AppCompatActivity implements MovieAdapter.ListItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navDrawer;
    Toolbar toolbar;
    SQLiteDatabase db;
    ArrayList<MoviesInfo> movies_list;
    MovieAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_favourite);

        toolbar = (Toolbar) findViewById(R.id.favourite_toolbar);
        toolbar.setTitle(getResources().getString(R.string.favourite_movies));
        setSupportActionBar(toolbar);

        navDrawer = (NavigationView) findViewById(R.id.drawer);
        navDrawer.setNavigationItemSelectedListener(this);

        drawer = (DrawerLayout) findViewById(R.id.favourite_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        recyclerView = (RecyclerView) findViewById(R.id.favouriteRecycler);

        DatabaseManager databaseManager = new DatabaseManager(this);
        db = databaseManager.getWritableDatabase();

        if (savedInstanceState != null){
            movies_list = savedInstanceState.getParcelableArrayList("movies");
            adapter = new MovieAdapter(this, movies_list, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else {
            movies_list = new ArrayList<>();

            Cursor cursor = db.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                movies_list.add(new MoviesInfo(
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_THUMBNAIL)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID)))
                );
            }
            cursor.close();

            adapter = new MovieAdapter(this, movies_list, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.top_rated){
            Intent i = new Intent(MoviesFavourite.this, MoviesTop.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.popular){
            Intent i = new Intent(MoviesFavourite.this, MoviesPopular.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
            drawer.closeDrawers();
            Toast.makeText(getBaseContext(), "Already sorted by favourites", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onListItemClick(int clicked) {
        Intent intent = new Intent(MoviesFavourite.this, MoviesDetail.class);
        intent.putExtra("thumbnail", movies_list.get(clicked).getThumbnailUrl());
        intent.putExtra("overview", movies_list.get(clicked).getOverview());
        intent.putExtra("title", movies_list.get(clicked).getTitle());
        intent.putExtra("vote", movies_list.get(clicked).getVoteAverage());
        intent.putExtra("date", movies_list.get(clicked).getReleaseDate());
        intent.putExtra("id", movies_list.get(clicked).getMovie_id());
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movies_list != null){
            outState.putParcelableArrayList("movies", movies_list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        movies_list = new ArrayList<>();

        Cursor cursor = db.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            movies_list.add(new MoviesInfo(
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_THUMBNAIL)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID)))
            );
        }
        cursor.close();

        adapter = new MovieAdapter(this, movies_list, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
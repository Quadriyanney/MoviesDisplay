package com.quadriyanney.moviesdisplay.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by quadriy on 6/3/17.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.quadriyanney.moviesdisplay";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE = "favoriteMovies";


    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String TABLE_NAME = "favoriteMovies";
        public static final String COLUMN_THUMBNAIL = "thumbnailUrl";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE = "vote_average";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_ID = "movie_id";
    }
}

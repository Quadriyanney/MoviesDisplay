package com.quadriyanney.moviesdisplay.data;

import android.provider.BaseColumns;

/**
 * Created by quadriy on 6/3/17.
 */

public class MovieContract {

    public static final class MovieEntry implements BaseColumns{

        public static final String TABLE_NAME = "favoriteMovies";
        public static final String COLUMN_THUMBNAIL = "thumbnailUrl";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE = "vote_average";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_ID = "movie_id";
    }
}

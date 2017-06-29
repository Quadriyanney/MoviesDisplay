package com.quadriyanney.moviesdisplay.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by quadriy on 6/28/17.
 */

public class MovieProvider extends ContentProvider {

    private DatabaseManager mDatabaseManager;
    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITE, FAVORITES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITE + "/*", FAVORITES_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {

        Context context = getContext();
        mDatabaseManager = new DatabaseManager(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        final SQLiteDatabase database = mDatabaseManager.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (match){
            case FAVORITES:
                retCursor = database.query(MovieContract.MovieEntry.TABLE_NAME,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1);
                break;
            case FAVORITES_WITH_ID:

                String id = uri.getLastPathSegment();
                String mSelection = MovieContract.MovieEntry.COLUMN_ID + "=?";
                String[] mSelectionArgs = {id};

                retCursor = database.query(MovieContract.MovieEntry.TABLE_NAME,
                        strings,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        s1);
                break;
            default:
                throw new UnsupportedOperationException("Unknown : " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) throws SQLException {

        final SQLiteDatabase database = mDatabaseManager.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case FAVORITES:
                long id = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                if (id > 0){
                 returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                }
                else {
                    Toast.makeText(getContext(), "Operation Not Successful", Toast.LENGTH_SHORT).show();
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown : " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        final  SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        int match =sUriMatcher.match(uri);
        int mDeleted;

        switch (match){
            case FAVORITES:
                mDeleted=db.delete(MovieContract.MovieEntry.TABLE_NAME,
                        s,
                        strings);
                break;
            case FAVORITES_WITH_ID:

                String id =uri.getLastPathSegment();
                String mSelection= MovieContract.MovieEntry.COLUMN_ID + "=?";
                String[] mSelectionArgs = {id};

                mDeleted=db.delete(MovieContract.MovieEntry.TABLE_NAME,
                        mSelection,
                        mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return mDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

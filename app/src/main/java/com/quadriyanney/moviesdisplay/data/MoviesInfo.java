package com.quadriyanney.moviesdisplay.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by quadriy on 4/14/17.
 */

public class MoviesInfo implements Parcelable{

    private String thumbnailUrl, overview, title, releaseDate, voteAverage, movie_id;

    public MoviesInfo(String thumbnailUrl, String voteAverage, String overview, String title, String releaseDate, String movie_id) {
        this.thumbnailUrl = thumbnailUrl;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.title = title;
        this.releaseDate = releaseDate;
        this.movie_id = movie_id;
    }

    protected MoviesInfo(Parcel in) {
        thumbnailUrl = in.readString();
        overview = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
        movie_id = in.readString();
    }

    public static final Creator<MoviesInfo> CREATOR = new Creator<MoviesInfo>() {
        @Override
        public MoviesInfo createFromParcel(Parcel in) {
            return new MoviesInfo(in);
        }

        @Override
        public MoviesInfo[] newArray(int size) {
            return new MoviesInfo[size];
        }
    };

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVoteAverage() {
        return voteAverage;
    }
    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(thumbnailUrl);
        parcel.writeString(overview);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(voteAverage);
        parcel.writeString(movie_id);
    }
}

package com.quadriyanney.moviesdisplay;

/**
 * Created by quadriy on 4/14/17.
 */

public class MoviesInfo {

    private String thumbnailUrl, overview, title, releaseDate;
    private int voteAverage;




    MoviesInfo(String thumbnailUrl, int voteAverage, String overview, String title, String releaseDate) {
        this.thumbnailUrl = thumbnailUrl;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getVoteAverage() {
        return voteAverage;
    }
    public void setVoteAverage(int voteAverage) {
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

}

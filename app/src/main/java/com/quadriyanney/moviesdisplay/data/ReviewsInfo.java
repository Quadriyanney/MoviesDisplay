package com.quadriyanney.moviesdisplay.data;

/**
 * Created by quadriy on 5/22/17.
 */

public class ReviewsInfo {

    private String author, review;

    public ReviewsInfo(String author, String review) {
        this.author = author;
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

}

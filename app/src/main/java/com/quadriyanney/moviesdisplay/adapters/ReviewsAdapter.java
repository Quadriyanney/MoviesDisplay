package com.quadriyanney.moviesdisplay.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quadriyanney.moviesdisplay.R;
import com.quadriyanney.moviesdisplay.data.ReviewsInfo;

import java.util.List;

/**
 * Created by quadriy on 5/22/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>{

    private List<ReviewsInfo> movies;

    public ReviewsAdapter(List<ReviewsInfo> movies) {
        this.movies = movies;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.author.setText(movies.get(position).getAuthor());
        holder.review.setText(movies.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView author, review;

        MyViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author_name);
            review = (TextView) itemView.findViewById(R.id.review);
        }
    }
}
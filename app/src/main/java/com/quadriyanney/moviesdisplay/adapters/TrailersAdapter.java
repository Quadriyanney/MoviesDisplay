package com.quadriyanney.moviesdisplay.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quadriyanney.moviesdisplay.R;

import java.util.List;

/**
 * Created by quadriy on 5/22/17.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MyViewHolder>{

    private List<String> movies;
    final private TrailersAdapter.ListItemClickListener mListener;



    public interface ListItemClickListener {
        void onListItemClick(int clicked);
    }

    public TrailersAdapter(List<String> movies, ListItemClickListener listener) {
        this.movies = movies;
        this.mListener = listener;
    }

    @Override
    public TrailersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_list_item, parent, false);
        return new TrailersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.MyViewHolder holder, int position) {
        int number = position + 1;
        String string = "Trailer " + String.valueOf(number);
        holder.trailer.setText(string);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView trailer;

        MyViewHolder(View itemView) {
            super(itemView);
            trailer = (TextView) itemView.findViewById(R.id.trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clicked = getAdapterPosition();
            mListener.onListItemClick(clicked);
        }
    }
}
package com.quadriyanney.moviesdisplay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by quadriy on 4/15/17.
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>{

    private Context context;
    private List<MoviesInfo> movies;
    final private ListItemClickListener mListener;



    public interface ListItemClickListener {
        void onListItemClick(int clicked);
    }

    public MovieAdapter(Context context, List<MoviesInfo> movies, ListItemClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.title.setText(movies.get(position).getTitle());
        Glide.with(context).load("http://image.tmdb.org/t/p/w185/" + movies.get(position).getThumbnailUrl()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView thumbnail;
        public TextView title;

        MyViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.movie_thumbnail);
            title = (TextView) itemView.findViewById(R.id.movie_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int clicked = getAdapterPosition();
            mListener.onListItemClick(clicked);
        }
    }
}
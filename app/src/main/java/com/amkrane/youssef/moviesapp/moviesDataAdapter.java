package com.amkrane.youssef.moviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class moviesDataAdapter extends RecyclerView.Adapter<moviesDataAdapter.movieViewHolder> {
    final private  onPosterClickHandler mPosterClickHandler;
    private List<movie> mMovies= new ArrayList<>();

    public void setData(List<movie> lMovies)
    {
        mMovies = lMovies;
        notifyDataSetChanged();
    }

    public moviesDataAdapter(onPosterClickHandler mPosterClickHandler)
    {
        this.mPosterClickHandler = mPosterClickHandler;
    }

    @Override
    public void onBindViewHolder(movieViewHolder holder, int position) {
        movie movieItem = mMovies.get(position);
        Context context = holder.posterImg.getContext();
        String posterUrl = MoviesDataUtils.getPosterUrl(context,movieItem.getPosterImage(),
                                                        context.getString(R.string.poster_size_w342));
        // load the image
        Picasso.with(holder.posterImg.getContext()).load(posterUrl).into(holder.posterImg);
    }

    @Override
    public movieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_movie_item,parent, false);
        return new movieViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

    public  class movieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView posterImg;
        movieViewHolder(View itemView) {
            super(itemView);
            posterImg = itemView.findViewById(R.id.iv_moviePoster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mPosterClickHandler.onPosterClicked(mMovies.get(position));
        }
    }

    public interface onPosterClickHandler{
        void onPosterClicked(movie mMovie);
    }
}


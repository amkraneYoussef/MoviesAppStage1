package com.amkrane.youssef.moviesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static com.amkrane.youssef.moviesapp.MoviesDataUtils.getPosterUrl;

public class DetailActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.EXTRA_MOVIE)))
        {
            movie mMovie = (movie) intent.getSerializableExtra(getString(R.string.EXTRA_MOVIE)) ;
            setTitle(mMovie.getOriginalTitle());
            ((TextView)findViewById(R.id.tv_description)).setText(mMovie.getOverview());
            ((TextView)findViewById(R.id.tv_releaseDate)).setText(mMovie.getReleaseDate());
            RatingBar rb = (findViewById(R.id.rb_voteAverage));
            rb.setRating(mMovie.getRating() / 2);
            rb.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Toast.makeText(view.getContext(),String .valueOf(((RatingBar)view).getRating() * 2) + "/10",Toast.LENGTH_SHORT ).show();
                    return true;
                }
            });

            Picasso.with(this).load(
                    getPosterUrl(this,mMovie.getPosterImage(),getString(R.string.poster_size_w500))).
                  into(((ImageView)findViewById(R.id.iv_detailMoviePoster)));
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

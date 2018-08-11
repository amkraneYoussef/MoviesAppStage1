package com.amkrane.youssef.moviesapp;

import java.io.Serializable;

class movie implements Serializable{
    private String originalTitle;
    private String posterImage;
    private String overview;
    private float rating;
    private String releaseDate;

    public movie()
    {

    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setPosterImage(String posterImage) {
        String url =
        this.posterImage = posterImage;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRating(String rating) {
        try
        {
            this.rating = Float.parseFloat(rating);
        }catch(Exception e)
        {
            this.rating =0;
        }
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {

        return originalTitle;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public String getOverview() {
        return overview;
    }

    public float getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}

package gr.android.moviesapp.data.model.home;

import com.google.gson.annotations.SerializedName;

public class MovieRemote {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("backdrop_path")
    private String backdrop_path;

    @SerializedName("release_date")
    private String releaseDate;

    // Additional fields for favorite handling
    private boolean favorite;

    public MovieRemote(int id, String title, String overview, String posterPath, double voteAverage, boolean isFavorite,String backdrop_path,String releaseDate) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.favorite = isFavorite;
        this.backdrop_path=backdrop_path;
        this.releaseDate=releaseDate;
    }

    public String getRelease_date() {
        return releaseDate;
    }

    public void setRelease_date(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}


package gr.android.moviesapp.domain.models;


import java.util.List;

public class MovieDetailsWithReviewsUi {

    private final DetailsBasicUi movieDetails;
    private final List<ReviewsUi> reviews;
    private final List<MovieUi> similarMovies;

    public MovieDetailsWithReviewsUi(DetailsBasicUi movieDetails, List<ReviewsUi> reviews,List<MovieUi> similarMovies) {
        this.movieDetails = movieDetails;
        this.reviews = reviews;
        this.similarMovies=similarMovies;
    }

    public DetailsBasicUi getMovieDetails() {
        return movieDetails;
    }

    public List<ReviewsUi> getReviews() {
            return (List<ReviewsUi>) reviews;
    }

    public List<MovieUi> getSimilarMovies() {
        return similarMovies;
    }
}

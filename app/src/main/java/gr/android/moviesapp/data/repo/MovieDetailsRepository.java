package gr.android.moviesapp.data.repo;

import java.util.List;

import gr.android.moviesapp.data.model.details.basicDetails.MovieDetailsRemote;
import gr.android.moviesapp.data.model.details.reviews.ReviewsRemote;
import gr.android.moviesapp.domain.models.DetailsBasicUi;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.models.ReviewsUi;
import io.reactivex.rxjava3.core.Observable;

public interface MovieDetailsRepository {

    Observable<DetailsBasicUi> getMovieById(Integer movie_id);
    Observable<List<ReviewsUi>> getReviewsById(Integer movie_id);
    Observable<List<MovieUi>> getSimilarMovies(Integer movie_id);
}

package gr.android.moviesapp.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import gr.android.moviesapp.data.repo.MovieDetailsRepository;
import gr.android.moviesapp.domain.models.DetailsBasicUi;
import gr.android.moviesapp.domain.models.MovieDetailsWithReviewsUi;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.models.ReviewsUi;
import io.reactivex.rxjava3.core.Observable;

public class DetailsUseCase {

    private MovieDetailsRepository movieDetailsRepository;

    @Inject
    public DetailsUseCase(MovieDetailsRepository movieDetailsRepository) {
        this.movieDetailsRepository = movieDetailsRepository;
    }

    public Observable<MovieDetailsWithReviewsUi> execute(Integer movieId) {
        Observable<DetailsBasicUi> movieDetailsObservable = movieDetailsRepository.getMovieById(movieId);
        Observable<List<ReviewsUi>> reviewsObservable = movieDetailsRepository.getReviewsById(movieId);
        Observable<List<MovieUi>> similarMoviesObservable = movieDetailsRepository.getSimilarMovies(movieId);

        return Observable.combineLatest(
                movieDetailsObservable,
                reviewsObservable,
                similarMoviesObservable,
                MovieDetailsWithReviewsUi::new
        );
    }

}

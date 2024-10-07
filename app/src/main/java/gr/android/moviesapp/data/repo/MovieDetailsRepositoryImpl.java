package gr.android.moviesapp.data.repo;

import java.util.List;
import java.util.stream.Collectors;

import gr.android.moviesapp.common.MovieMapper;
import gr.android.moviesapp.data.model.details.basicDetails.MovieDetailsRemote;
import gr.android.moviesapp.data.model.details.reviews.MovieDetailsReviewApiResponse;
import gr.android.moviesapp.data.model.home.MovieApiResponse;
import gr.android.moviesapp.data.networkServices.MovieApiService;
import gr.android.moviesapp.domain.models.DetailsBasicUi;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.models.ReviewsUi;
import io.reactivex.rxjava3.core.Observable;

public class MovieDetailsRepositoryImpl implements MovieDetailsRepository {

    private MovieApiService apiService;

    public MovieDetailsRepositoryImpl(MovieApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<DetailsBasicUi> getMovieById(Integer movie_id) {
        return apiService.getMovieById(movie_id)
                .map(MovieMapper::mapToUiModel);
    }

    @Override
    public Observable<List<ReviewsUi>> getReviewsById(Integer movie_id) {
        return apiService.getReviewsById(movie_id)
                .map(MovieDetailsReviewApiResponse::getResults)
                .map(movies ->
                        movies.stream()
                                .map(MovieMapper::mapToUiModel)
                                .collect(Collectors.toList())
                );
    }

    @Override
    public Observable<List<MovieUi>> getSimilarMovies(Integer movie_id){
        return apiService.getSimilarMovies(movie_id)
                .map(MovieApiResponse::getResults)
                .map(movies ->
                        movies.stream()
                                .map(MovieMapper::mapToUiMovie)
                                .collect(Collectors.toList())
                );
    }


}
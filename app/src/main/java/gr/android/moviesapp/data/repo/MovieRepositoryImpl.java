package gr.android.moviesapp.data.repo;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.inject.Inject;

import gr.android.moviesapp.data.model.home.MovieRemote;
import gr.android.moviesapp.data.model.home.MovieApiResponse;
import gr.android.moviesapp.data.networkServices.MovieApiService;
import gr.android.moviesapp.data.database.MovieDao;
import gr.android.moviesapp.data.database.MovieEntity;
import io.reactivex.rxjava3.core.Observable;

public class MovieRepositoryImpl implements MovieRepository {
    private MovieApiService apiService;
    private MovieDao movieDao;


    public MovieRepositoryImpl(MovieApiService apiService, MovieDao movieDao) {
        this.apiService = apiService;
        this.movieDao = movieDao;
    }

    @Override
    public Observable<List<MovieRemote>> getPopularMovies() {
        return apiService.getPopularMovies()
                .map(MovieApiResponse::getResults)
                .doOnNext(movies -> {
                    for (MovieRemote movie : movies) {
                        movie.setRelease_date(convertDateFormat(movie.getRelease_date()));
                        // Check if movie is already in local database and mark as favorite
                        MovieEntity localMovie = movieDao.getMovieById(movie.getId());
                        if (localMovie != null) {
                            movie.setFavorite(true);
                        }
                    }
                });
    }

    @Override
    public LiveData<List<MovieEntity>> getAllMovies() {
        return movieDao.getAll();
    }

    @Override
    public void toggleFavorite(MovieEntity movie) {
        if (movie.isFavorite()) {
            movieDao.delete(movie);
        } else {
            movie.setFavorite(!movie.isFavorite());
            movieDao.insert(movie);
        }
    }
    public static String convertDateFormat(String inputDate) {
        // Define the input date format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Define the output date format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        try {
            // Parse the input date string to a LocalDate object
            LocalDate date = LocalDate.parse(inputDate, inputFormatter);

            // Format the LocalDate object to the desired output format
            return date.format(outputFormatter);
        } catch (DateTimeParseException e) {
            // Handle the case where the input date string is not in the expected format
            System.err.println("Invalid date format: " + inputDate);
            return null;
        }
    }
}

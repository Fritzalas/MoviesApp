package gr.android.moviesapp.ui.homeScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import gr.android.moviesapp.common.MovieEntityMapper;
import gr.android.moviesapp.common.MovieMapper;
import gr.android.moviesapp.data.repo.MovieRepository;
import gr.android.moviesapp.data.database.MovieEntity;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.usecase.DetailsUseCase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MovieViewModel extends ViewModel {

    private final MovieRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final ExecutorService executorService;
    private Boolean hasError = false;

    private final MutableLiveData<List<MovieUi>> moviesLiveData = new MutableLiveData<>();
    public LiveData<List<MovieUi>> getMovies() {
        refreshMovies(); // Load initial data
        return moviesLiveData;
    }

    @Inject
    public MovieViewModel(MovieRepository repository, DetailsUseCase ignoredDetailsUseCase) {
        this.repository = repository;
        this.executorService = Executors.newSingleThreadExecutor();
        observeDatabaseChanges();
    }

    // Make this method public so it can be accessed from MovieListFragment
    public void refreshMovies() {
        // Sort the movies by voteAverage in descending order
        disposable.add(repository.getPopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(MovieMapper::mapToUiMovieList)
                .map(this::sortMoviesByVoteAverage)
                .subscribe(
                        moviesLiveData::postValue,
                        throwable -> {
                            Log.e("MovieViewModel", "Error fetching movies", throwable);
                            hasError = true;
                            observeDatabaseChanges();
                        }
                ));
    }

    private List<MovieUi> sortMoviesByVoteAverage(List<MovieUi> movies) {
        // Sort the movies by voteAverage in descending order
        movies.sort((m1, m2) -> Double.compare(m2.getVoteAverage(), m1.getVoteAverage()));
        return movies;
    }

    private void observeDatabaseChanges() {
        LiveData<List<MovieEntity>> allMovies = repository.getAllMovies();
        allMovies.observeForever(movieEntities -> {
            List<MovieUi> dbMovies = MovieEntityMapper.mapToUiMovieList(movieEntities);
            List<MovieUi> currentMovies = moviesLiveData.getValue();
            if (currentMovies == null) {
                currentMovies = new ArrayList<>();
            }

            // Create a map for quick lookup of current movies by ID
            Map<Integer, MovieUi> currentMoviesMap = new HashMap<>();
            for (MovieUi movie : currentMovies) {
                currentMoviesMap.put(movie.getId(), movie);
            }

            ArrayList<MovieUi> newMovieList = new ArrayList<>(currentMovies);

            if (!hasError) {
                for (MovieUi dbMovie : dbMovies) {
                    MovieUi currentMovie = currentMoviesMap.get(dbMovie.getId());
                    if (currentMovie != null) {
                        if (dbMovie.isFavorite() != currentMovie.isFavorite()) {
                            // Create a new instance of MovieUi with the updated favorite status
                            MovieUi updatedMovie = new MovieUi(
                                    currentMovie.getId(),
                                    currentMovie.getTitle(),
                                    currentMovie.getOverview(),
                                    currentMovie.getPosterPath(),
                                    currentMovie.getVoteAverage(),
                                    dbMovie.isFavorite(),
                                    dbMovie.getPosterPath(),
                                    dbMovie.getReleaseDate());
                            int index = newMovieList.indexOf(currentMovie);
                            newMovieList.set(index, updatedMovie);
                        }
                    }
                }
            } else {
                newMovieList = new ArrayList<>(dbMovies);
            }

            // Sort the new movie list by voteAverage in descending order
            sortMoviesByVoteAverage(newMovieList);
            moviesLiveData.postValue(newMovieList);
        });
    }

    public void toggleFavorite(MovieUi movie) {
        executorService.execute(() -> {
            repository.toggleFavorite(MovieEntityMapper.mapToMovieEntity(movie));
            refreshMovies(); // Refresh after toggling favorite
        });
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();
    }
}

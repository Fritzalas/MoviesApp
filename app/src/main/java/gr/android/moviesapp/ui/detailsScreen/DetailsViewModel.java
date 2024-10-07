package gr.android.moviesapp.ui.detailsScreen;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import gr.android.moviesapp.domain.models.MovieDetailsWithReviewsUi;
import gr.android.moviesapp.domain.usecase.DetailsUseCase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class DetailsViewModel extends ViewModel {
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final DetailsUseCase detailsUseCase;

    @Inject
    public DetailsViewModel(DetailsUseCase detailsUseCase) {
        this.detailsUseCase = detailsUseCase;
    }

    private final MutableLiveData<MovieDetailsWithReviewsUi> movieDetailsWithReviewsLiveData = new MutableLiveData<>();
    public LiveData<MovieDetailsWithReviewsUi> movieDetailsLiveData = movieDetailsWithReviewsLiveData;

    public void fetchMovieDetailsWithReviews(Integer movieId) {
        disposable.add(detailsUseCase.execute(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        detailsWithReviews -> movieDetailsWithReviewsLiveData.postValue(detailsWithReviews),
                        throwable -> Log.e("MovieViewModel", "Error fetching movie details with reviews", throwable)
                ));
    }
}

package gr.android.moviesapp.di;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import gr.android.moviesapp.data.repo.MovieDetailsRepository;
import gr.android.moviesapp.data.repo.MovieDetailsRepositoryImpl;
import gr.android.moviesapp.data.repo.MovieRepository;
import gr.android.moviesapp.data.repo.MovieRepositoryImpl;
import gr.android.moviesapp.data.networkServices.MovieApiService;
import gr.android.moviesapp.data.database.MovieDao;
import gr.android.moviesapp.data.database.MovieDatabase;

@Module
@InstallIn(SingletonComponent.class)
public abstract class AppModule {

    @Provides
    @Singleton
    public static MovieDatabase provideMovieDatabase(Application application) {
        return Room.databaseBuilder(application, MovieDatabase.class, "movie_db")
                .build();
    }

    @Provides
    @Singleton
    public static MovieDao provideMovieDao(MovieDatabase database) {
        return database.movieDao();
    }

    @Provides
    @Singleton
    public static MovieRepository provideMovieRepository(MovieApiService apiService, MovieDao movieDao) {
        return new MovieRepositoryImpl(apiService, movieDao);
    }

    @Provides
    @Singleton
    public static MovieDetailsRepository provideMovieDetailsRepository(MovieApiService apiService) {
        return new MovieDetailsRepositoryImpl(apiService);
    }
}


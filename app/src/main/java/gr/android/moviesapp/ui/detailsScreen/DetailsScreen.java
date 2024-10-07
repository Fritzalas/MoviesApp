package gr.android.moviesapp.ui.detailsScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import gr.android.moviesapp.R;
import gr.android.moviesapp.common.ListTransform;
import gr.android.moviesapp.common.TimeConversion;
import gr.android.moviesapp.common.ZoomOutPageTransformer;
import gr.android.moviesapp.common.formatdate;
import gr.android.moviesapp.databinding.DetailsLayoutBinding;
import gr.android.moviesapp.domain.models.MovieDetailsWithReviewsUi;
import gr.android.moviesapp.domain.models.MovieUi;


@AndroidEntryPoint
public class DetailsScreen extends Fragment implements SimilarMoviesAdapter.OnItemClickListenerYes {

    private DetailsLayoutBinding binding;
    private DetailsViewModel detailsViewModel;
    private final List<String> items= new ArrayList<>();
    ViewPager2 viewPager;
    private RecyclerView movieRecyclerView;
    private boolean isFavorite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DetailsLayoutBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieRecyclerView = view.findViewById(R.id.movieRecyclerView);
        viewPager = view.findViewById(R.id.viewPager);
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        // Fetch movie details and reviews
        if (getArguments() != null) {
            int movieId = getArguments().getInt("MOVIE_ID");
            isFavorite=getArguments().getBoolean("isFavorite");
            if (movieId != -1) {
                detailsViewModel.fetchMovieDetailsWithReviews(movieId);
            }
        }
        binding.toolbarButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            if (navController.getPreviousBackStackEntry() != null) {
                navController.popBackStack();
            }
        });
        initView();
        initObservers();
    }

    private void initView() {
    }

    private void initObservers() {
        detailsViewModel.movieDetailsLiveData.observe(getViewLifecycleOwner(), movieDetailsWithReviewsUi -> {
            if (movieDetailsWithReviewsUi != null) {
                // Update UI with the movie details and reviews
                updateUI(movieDetailsWithReviewsUi);
            }
        });
    }

    private void updateUI(MovieDetailsWithReviewsUi movieDetailsWithReviewsUi) {
        // TODO Implement UI in .xml file and assign values
        if(isFavorite){
            binding.favoriteIcon.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.ic_favorite_selected));
        }
        if (movieDetailsWithReviewsUi.getMovieDetails() != null && movieDetailsWithReviewsUi.getMovieDetails().getTitle() != null && !movieDetailsWithReviewsUi.getMovieDetails().getTitle().isEmpty()) {
            binding.title.setText(movieDetailsWithReviewsUi.getMovieDetails().getTitle());
        } else {
            binding.title.setText("No Movie Title");
        }
        Glide.with(binding.myImageView.getContext())
                .load("https://image.tmdb.org/t/p/w500" + movieDetailsWithReviewsUi.getMovieDetails().getBackdropPath())
                .apply(new RequestOptions().placeholder(R.drawable.loading))
                .error(R.drawable.noimage) // This sets a fallback image if there's an error
                .into(binding.myImageView);
        if (movieDetailsWithReviewsUi.getMovieDetails() != null &&
                movieDetailsWithReviewsUi.getMovieDetails().getHomepage() != null &&
                !movieDetailsWithReviewsUi.getMovieDetails().getHomepage().isEmpty()) {

            binding.insideImageButton.setOnClickListener(v -> shareText(movieDetailsWithReviewsUi.getMovieDetails().getHomepage()));
        } else {
            binding.insideImageButton.setVisibility(View.GONE);
        }
        if (movieDetailsWithReviewsUi.getMovieDetails() != null && movieDetailsWithReviewsUi.getMovieDetails().getGenres() != null && !movieDetailsWithReviewsUi.getMovieDetails().getGenres().isEmpty()) {
            binding.moviegenres.setText(ListTransform.joinGenreNamesWithComma(movieDetailsWithReviewsUi.getMovieDetails().getGenres()));
        } else {
            binding.moviegenres.setText("No Movie Genres");
        }
        if (movieDetailsWithReviewsUi.getMovieDetails() != null && movieDetailsWithReviewsUi.getMovieDetails().getReleaseDate() != null && !movieDetailsWithReviewsUi.getMovieDetails().getReleaseDate().isEmpty()) {
            binding.releasedate.setText(formatdate.convertDateFormat(movieDetailsWithReviewsUi.getMovieDetails().getReleaseDate()));
        } else {
            binding.releasedate.setText("No release date");
        }
        if (movieDetailsWithReviewsUi.getMovieDetails() != null ) {
            binding.movieRating.setRating((float) (movieDetailsWithReviewsUi.getMovieDetails().getVoteAverage() / 2));
        } else {
            binding.movieRating.setRating(0);
        }
        if (movieDetailsWithReviewsUi.getMovieDetails() != null && TimeConversion.convertMinutesToTime(movieDetailsWithReviewsUi.getMovieDetails().getRuntime()) != null) {
            binding.runtimebe.setText(TimeConversion.convertMinutesToTime(movieDetailsWithReviewsUi.getMovieDetails().getRuntime()));
        } else {
            binding.runtimebe.setText("No runtime available");
        }
        if (movieDetailsWithReviewsUi.getMovieDetails() != null && movieDetailsWithReviewsUi.getMovieDetails().getOverview() != null && !movieDetailsWithReviewsUi.getMovieDetails().getOverview().isEmpty()) {
            binding.movieDescription.setText(movieDetailsWithReviewsUi.getMovieDetails().getOverview());
        } else {
            binding.movieDescription.setText("No description available");
        }
        //Make reviews:
        for (int counter = 0; counter < movieDetailsWithReviewsUi.getReviews().size(); counter++) {
            if (movieDetailsWithReviewsUi.getReviews().get(counter).getAuthor() == null) {
                break;
            }
            items.add("Author: "+movieDetailsWithReviewsUi.getReviews().get(counter).getAuthor()+"\n"+"\n" +movieDetailsWithReviewsUi.getReviews().get(counter).getContent()+"\n"+"\n"+ "Created at: " +movieDetailsWithReviewsUi.getReviews().get(counter).getCreatedAt());
            if (counter >= 3 ||
                    (counter + 1 < movieDetailsWithReviewsUi.getReviews().size() &&
                            movieDetailsWithReviewsUi.getReviews().get(counter + 1).getAuthor() == null)) {
                break;
            }
        }
        int redColor = ContextCompat.getColor(getContext(), R.color.red);
        int whiteColor = ContextCompat.getColor(getContext(), R.color.white);
        CarouselAdapter adapter = new CarouselAdapter(items, redColor, whiteColor);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        if(items.isEmpty()){
            binding.Reviews.setText("No Reviews were found");
        }
        //Just a test to see if API gives response
        // List<MovieUi> similars = movieDetailsWithReviewsUi.getSimilarMovies();
        //binding.title.setText(similars.get(0).getTitle() );
        // binding.title.setText(similars.get(0).getBackdrop_path() );
        List<MovieUi> similars = new ArrayList<>();
        int maxSimilars = 6; // Maximum number of similar movies to display

        if (movieDetailsWithReviewsUi.getSimilarMovies() != null) {
            List<MovieUi> similarMovies = movieDetailsWithReviewsUi.getSimilarMovies();
            for (int counter = 0; counter < similarMovies.size(); counter++) {
                MovieUi movie = similarMovies.get(counter);
                if (movie.getBackdrop_path() != null) {
                    similars.add(movie);
                }
                if (similars.size() >= maxSimilars) {
                    break;
                }
            }
        }
        SimilarMoviesAdapter movieAdapter = new SimilarMoviesAdapter(similars, getContext(), this);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        movieRecyclerView.setAdapter(movieAdapter);
        if(similars.isEmpty()){
            binding.Similar.setText("No Similar Movies Were Found");
        }
    }
    private void shareText(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
    @Override
    public void onItemClick(int movieId) {
        // Handle the click event here
        Bundle bundle = new Bundle();
        bundle.putInt("MOVIE_ID", movieId);
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.detailsScreen, bundle);
    }
}
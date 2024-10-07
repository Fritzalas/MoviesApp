package gr.android.moviesapp.ui.homeScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import dagger.hilt.android.AndroidEntryPoint;
import gr.android.moviesapp.R;
import gr.android.moviesapp.databinding.FragmentMovieBinding;

@AndroidEntryPoint
public class MovieListFragment extends Fragment {

    private MovieViewModel viewModel;
    private MovieListAdapter adapter;
    private FragmentMovieBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMovieBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MovieListAdapter(
                movie -> viewModel.toggleFavorite(movie),
                (movieId,favorite) -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("MOVIE_ID", movieId);
                    bundle.putBoolean("isFavorite",favorite);
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_movieListFragment_to_detailsScreen, bundle);
                }
        );
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setItemAnimator(null);

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Trigger refresh action
            viewModel.refreshMovies();
        });

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.getMovies().observe(getViewLifecycleOwner(), movies -> {
            adapter.submitList(movies);
            swipeRefreshLayout.setRefreshing(false); // Stop the refresh animation
        });
    }
}

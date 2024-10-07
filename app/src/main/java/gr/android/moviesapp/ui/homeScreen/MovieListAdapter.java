package gr.android.moviesapp.ui.homeScreen;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import gr.android.moviesapp.R;
import gr.android.moviesapp.common.NetworkUtil;
import gr.android.moviesapp.databinding.ItemMovieBinding;
import gr.android.moviesapp.domain.models.MovieUi;

public class MovieListAdapter extends ListAdapter<MovieUi, MovieListAdapter.MovieViewHolder> {

    private final OnFavoriteClickListener favoriteClickListener;
    private final OnItemClickListener itemClickListener;

    protected MovieListAdapter(OnFavoriteClickListener favoriteClickListener, OnItemClickListener itemClickListener) {
        super(new DiffCallback());
        this.favoriteClickListener = favoriteClickListener;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieUi movie = getItem(position);
        holder.bind(movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ItemMovieBinding binding;

        MovieViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MovieUi movie) {
            Glide.with(binding.moviePoster.getContext())
                    .load("https://image.tmdb.org/t/p/w500" + movie.getBackdrop_path())
                    .apply(new RequestOptions().placeholder(R.drawable.loading))
                    .error(R.drawable.noimage)
                    .into(binding.moviePoster);
            if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
                binding.releaseDate.setText(movie.getReleaseDate());
            } else {
                binding.releaseDate.setText(""); // Optional: clear the text if you want
            }
            if (movie.getTitle() != null && !movie.getTitle().isEmpty()) {
                binding.movieTitle.setText(movie.getTitle());
            } else {
                binding.movieTitle.setText("Movie Title Not Available");
            }
            if (movie.getVoteAverage()>=0 || movie.getVoteAverage()<=10) {
                binding.movieRating.setRating((float) (movie.getVoteAverage() / 2));
            } else {
                binding.movieRating.setRating(0); // Optional: set to default rating if you want
            }

            if(movie.isFavorite()) {
                binding.favoriteIcon.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.ic_favorite_selected));
            } else {
                binding.favoriteIcon.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.ic_favorite_unselect));
            }

            binding.favoriteIcon.setOnClickListener(v -> {
                if (NetworkUtil.isConnectedToInternet(v.getContext())) {
                    favoriteClickListener.onFavoriteClick(movie);
                } else {
                    Toast.makeText(v.getContext(), "No internet connection. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            });

            binding.getRoot().setOnClickListener(view -> {
                if (NetworkUtil.isConnectedToInternet(view.getContext())) {
                    itemClickListener.onItemClick(movie.getId(), movie.isFavorite());
                } else {
                    Toast.makeText(view.getContext(), "No internet connection. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    static class DiffCallback extends DiffUtil.ItemCallback<MovieUi> {
        @Override
        public boolean areItemsTheSame(@NonNull MovieUi oldItem, @NonNull MovieUi newItem) {
            // Check if items have the same ID
            return oldItem.getId() == newItem.getId() && oldItem.isFavorite() == newItem.isFavorite();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MovieUi oldItem, @NonNull MovieUi newItem) {
            // Check if all properties (except ID) are the same
            return oldItem.getId() == newItem.getId() && oldItem.isFavorite() == newItem.isFavorite();
        }
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(MovieUi movie);
    }

    public interface OnItemClickListener {
        void onItemClick(int movieId,boolean favorite);
    }
}
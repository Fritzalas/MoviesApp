package gr.android.moviesapp.ui.detailsScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import gr.android.moviesapp.R;
import gr.android.moviesapp.common.NetworkUtil;
import gr.android.moviesapp.domain.models.MovieUi;
import io.reactivex.rxjava3.annotations.NonNull;

public class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.ViewHolder> {

    private final List<MovieUi> similarMovies;
    private final Context context;
    private final OnItemClickListenerYes itemClickListener;

    public SimilarMoviesAdapter(List<MovieUi> similarMovies, Context context, OnItemClickListenerYes itemClickListener) {
        this.similarMovies = similarMovies;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @androidx.annotation.NonNull
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.similar_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieUi movie = similarMovies.get(position);
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .apply(new RequestOptions().placeholder(R.drawable.loading))
                .transform(new RoundedCorners(64))
                .error(R.drawable.noimage)
                .into(holder.movieImage);

        holder.movieImage.setOnClickListener(view -> {
            if (NetworkUtil.isConnectedToInternet(view.getContext())) {
                itemClickListener.onItemClick(movie.getId());
            } else {
                Toast.makeText(view.getContext(), "No internet connection. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return similarMovies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;

        public ViewHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.similarImageView);
        }
    }

    public interface OnItemClickListenerYes {
        void onItemClick(int movieId);
    }
}

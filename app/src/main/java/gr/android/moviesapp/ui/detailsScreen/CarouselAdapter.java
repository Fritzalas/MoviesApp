package gr.android.moviesapp.ui.detailsScreen;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.android.moviesapp.R;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {

    private final List<String> items;
    private final int redColor;
    private final int whiteColor;

    public CarouselAdapter(List<String> items, int redColor, int whiteColor) {
        this.items = items;
        this.redColor = redColor;
        this.whiteColor = whiteColor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = items.get(position);
        SpannableString spannableString = new SpannableString(text);

        // Find the end index of the first line
        int endOfFirstLine = text.indexOf('\n');
        if (endOfFirstLine == -1) {
            endOfFirstLine = text.length(); // If there's no newline, the whole text is the first line
        }

        // Set the color for the first line
        spannableString.setSpan(new ForegroundColorSpan(redColor), 0, endOfFirstLine, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the color for the rest of the text
        if (endOfFirstLine < text.length()) {
            spannableString.setSpan(new ForegroundColorSpan(whiteColor), endOfFirstLine, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        holder.textView.setText(spannableString);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

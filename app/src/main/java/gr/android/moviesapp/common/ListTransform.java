package gr.android.moviesapp.common;

import java.util.List;
import java.util.stream.Collectors;

import gr.android.moviesapp.domain.models.GenreUi;

public class ListTransform {
    public static String joinGenreNamesWithComma(List<GenreUi> genreList) {
        return genreList.stream()
                .map(GenreUi::getName)
                .collect(Collectors.joining(" , "));
    }
}

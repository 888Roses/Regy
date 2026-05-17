package dev.rosenoire.regy.api.text;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ApiStatus.NonExtendable
public interface StringHelper {
    static boolean isWhitespaceDesignator(char character) {
        return Character.isWhitespace(character) || character == '_' || character == '-';
    }

    static char lastCharacterOf(StringBuilder builder) {
        return builder.charAt(builder.length() - 1);
    }

    static boolean isNullBlankOrEmpty(@Nullable String str) {
        return str == null || str.isBlank();
    }

    static String defaulted(@Nullable String str, @NonNull String fallback) {
        return isNullBlankOrEmpty(str) ? fallback : str;
    }

    static List<String> getWords(String source, Locale locale) {
        var iterator = BreakIterator.getWordInstance(locale);
        iterator.setText(source);
        var words = new ArrayList<String>();

        var start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            words.add(source.substring(start, end));
        }

        return words;
    }

    static List<String> getWords(String source) {
        return getWords(source, Locale.ROOT);
    }
}

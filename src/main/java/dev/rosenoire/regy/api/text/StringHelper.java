package dev.rosenoire.regy.api.text;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

@ApiStatus.NonExtendable
public interface StringHelper {
    static boolean isWhitespaceDesignator(char character) {
        return Character.isWhitespace(character) || character == '_' || character == '-';
    }

    static char lastCharacterOf(StringBuilder builder) {
        return  builder.charAt(builder.length() - 1);
    }

    static boolean isNullBlankOrEmpty(@Nullable String str) {
        return str == null || str.isBlank();
    }

    static String defaulted(@Nullable String str, @NonNull String fallback) {
        return isNullBlankOrEmpty(str) ? fallback : str;
    }
}

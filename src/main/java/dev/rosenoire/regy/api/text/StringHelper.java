package dev.rosenoire.regy.api.text;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface StringHelper {
    static boolean isWhitespaceDesignator(char character) {
        return Character.isWhitespace(character) || character == '_' || character == '-';
    }

    static char lastCharacterOf(StringBuilder builder) {
        return  builder.charAt(builder.length() - 1);
    }
}

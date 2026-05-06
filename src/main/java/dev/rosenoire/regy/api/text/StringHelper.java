package dev.rosenoire.regy.api.text;

public interface StringHelper {
    static boolean isWhitespaceDesignator(char character) {
        return Character.isWhitespace(character) || character == '_' || character == '-';
    }

    static char lastCharacterOf(StringBuilder builder) {
        return  builder.charAt(builder.length() - 1);
    }
}

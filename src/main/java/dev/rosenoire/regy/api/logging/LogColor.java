package dev.rosenoire.regy.api.logging;

@SuppressWarnings({"InfiniteRecursion"})
public final class LogColor {
    private LogColor() {
    }

    private static final int BLACK = 30;
    private static final int RED = 31;
    private static final int GREEN = 32;
    private static final int YELLOW = 33;
    private static final int BLUE = 34;
    private static final int MAGENTA = 35;
    private static final int CYAN = 36;
    private static final int WHITE = 37;

    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m";
    private static final char SEPARATOR = ';';
    private static final String END_COLOUR = PREFIX + SUFFIX;

    public static String getColor(int colour) {
        return getColor(colour, Attribute.NORMAL);
    }

    public static String getColor(int colour, Attribute attribute) {
        return getColor(colour, attribute.code());
    }

    public static String getColor(int colour, int attribute) {
        return PREFIX + attribute + SEPARATOR + colour + SUFFIX;
    }

    public static String format(String message) {
        return message
                .replace("§end", END_COLOUR)

                .replace("§bold§black", getColor(BLACK, Attribute.BOLD))
                .replace("§black§bold", getColor(BLACK, Attribute.BOLD))

                .replace("§bold§red", getColor(RED, Attribute.BOLD))
                .replace("§red§bold", getColor(RED, Attribute.BOLD))

                .replace("§bold§green", getColor(GREEN, Attribute.BOLD))
                .replace("§green§bold", getColor(GREEN, Attribute.BOLD))

                .replace("§bold§yellow", getColor(YELLOW, Attribute.BOLD))
                .replace("§yellow§bold", getColor(YELLOW, Attribute.BOLD))

                .replace("§bold§blue", getColor(BLUE, Attribute.BOLD))
                .replace("§blue§bold", getColor(BLUE, Attribute.BOLD))

                .replace("§bold§magenta", getColor(MAGENTA, Attribute.BOLD))
                .replace("§magenta§bold", getColor(MAGENTA, Attribute.BOLD))

                .replace("§bold§cyan", getColor(CYAN, Attribute.BOLD))
                .replace("§cyan§bold", getColor(CYAN, Attribute.BOLD))

                .replace("§bold§white", getColor(WHITE, Attribute.BOLD))
                .replace("§white§bold", getColor(WHITE, Attribute.BOLD))

                .replace("§italic§black", getColor(BLACK, Attribute.ITALIC))
                .replace("§black§italic", getColor(BLACK, Attribute.ITALIC))

                .replace("§italic§red", getColor(RED, Attribute.ITALIC))
                .replace("§red§italic", getColor(RED, Attribute.ITALIC))

                .replace("§italic§green", getColor(GREEN, Attribute.ITALIC))
                .replace("§green§italic", getColor(GREEN, Attribute.ITALIC))

                .replace("§italic§yellow", getColor(YELLOW, Attribute.ITALIC))
                .replace("§yellow§italic", getColor(YELLOW, Attribute.ITALIC))

                .replace("§italic§blue", getColor(BLUE, Attribute.ITALIC))
                .replace("§blue§italic", getColor(BLUE, Attribute.ITALIC))

                .replace("§italic§magenta", getColor(MAGENTA, Attribute.ITALIC))
                .replace("§magenta§italic", getColor(MAGENTA, Attribute.ITALIC))

                .replace("§italic§cyan", getColor(CYAN, Attribute.ITALIC))
                .replace("§cyan§italic", getColor(CYAN, Attribute.ITALIC))

                .replace("§italic§white", getColor(WHITE, Attribute.ITALIC))
                .replace("§white§italic", getColor(WHITE, Attribute.ITALIC))

                .replace("§underline§black", getColor(BLACK, Attribute.UNDERLINE))
                .replace("§black§underline", getColor(BLACK, Attribute.UNDERLINE))

                .replace("§underline§red", getColor(RED, Attribute.UNDERLINE))
                .replace("§red§underline", getColor(RED, Attribute.UNDERLINE))

                .replace("§underline§green", getColor(GREEN, Attribute.UNDERLINE))
                .replace("§green§underline", getColor(GREEN, Attribute.UNDERLINE))

                .replace("§underline§yellow", getColor(YELLOW, Attribute.UNDERLINE))
                .replace("§yellow§underline", getColor(YELLOW, Attribute.UNDERLINE))

                .replace("§underline§blue", getColor(BLUE, Attribute.UNDERLINE))
                .replace("§blue§underline", getColor(BLUE, Attribute.UNDERLINE))

                .replace("§underline§magenta", getColor(MAGENTA, Attribute.UNDERLINE))
                .replace("§magenta§underline", getColor(MAGENTA, Attribute.UNDERLINE))

                .replace("§underline§cyan", getColor(CYAN, Attribute.UNDERLINE))
                .replace("§cyan§underline", getColor(CYAN, Attribute.UNDERLINE))

                .replace("§underline§white", getColor(WHITE, Attribute.UNDERLINE))
                .replace("§white§underline", getColor(WHITE, Attribute.UNDERLINE))

                .replace("§reverse§black", getColor(BLACK, Attribute.REVERSE))
                .replace("§black§reverse", getColor(BLACK, Attribute.REVERSE))

                .replace("§reverse§red", getColor(RED, Attribute.REVERSE))
                .replace("§red§reverse", getColor(RED, Attribute.REVERSE))

                .replace("§reverse§green", getColor(GREEN, Attribute.REVERSE))
                .replace("§green§reverse", getColor(GREEN, Attribute.REVERSE))

                .replace("§reverse§yellow", getColor(YELLOW, Attribute.REVERSE))
                .replace("§yellow§reverse", getColor(YELLOW, Attribute.REVERSE))

                .replace("§reverse§blue", getColor(BLUE, Attribute.REVERSE))
                .replace("§blue§reverse", getColor(BLUE, Attribute.REVERSE))

                .replace("§reverse§magenta", getColor(MAGENTA, Attribute.REVERSE))
                .replace("§magenta§reverse", getColor(MAGENTA, Attribute.REVERSE))

                .replace("§reverse§cyan", getColor(CYAN, Attribute.REVERSE))
                .replace("§cyan§reverse", getColor(CYAN, Attribute.REVERSE))

                .replace("§reverse§white", getColor(WHITE, Attribute.REVERSE))
                .replace("§white§reverse", getColor(WHITE, Attribute.REVERSE))

                .replace("§hidden§black", getColor(BLACK, Attribute.HIDDEN))
                .replace("§black§hidden", getColor(BLACK, Attribute.HIDDEN))

                .replace("§hidden§red", getColor(RED, Attribute.HIDDEN))
                .replace("§red§hidden", getColor(RED, Attribute.HIDDEN))

                .replace("§hidden§green", getColor(GREEN, Attribute.HIDDEN))
                .replace("§green§hidden", getColor(GREEN, Attribute.HIDDEN))

                .replace("§hidden§yellow", getColor(YELLOW, Attribute.HIDDEN))
                .replace("§yellow§hidden", getColor(YELLOW, Attribute.HIDDEN))

                .replace("§hidden§blue", getColor(BLUE, Attribute.HIDDEN))
                .replace("§blue§hidden", getColor(BLUE, Attribute.HIDDEN))

                .replace("§hidden§magenta", getColor(MAGENTA, Attribute.HIDDEN))
                .replace("§magenta§hidden", getColor(MAGENTA, Attribute.HIDDEN))

                .replace("§hidden§cyan", getColor(CYAN, Attribute.HIDDEN))
                .replace("§cyan§hidden", getColor(CYAN, Attribute.HIDDEN))

                .replace("§hidden§white", getColor(WHITE, Attribute.HIDDEN))
                .replace("§white§hidden", getColor(WHITE, Attribute.HIDDEN))

                .replace("§bold", getColor(WHITE, Attribute.BOLD))
                .replace("§italic", getColor(WHITE, Attribute.ITALIC))
                .replace("§underline", getColor(WHITE, Attribute.UNDERLINE))
                .replace("§reverse", getColor(WHITE, Attribute.REVERSE))
                .replace("§hidden", getColor(WHITE, Attribute.HIDDEN))

                .replace("§black", getColor(BLACK))
                .replace("§red", getColor(RED))
                .replace("§green", getColor(GREEN))
                .replace("§yellow", getColor(YELLOW))
                .replace("§blue", getColor(BLUE))
                .replace("§magenta", getColor(MAGENTA))
                .replace("§cyan", getColor(CYAN))
                .replace("§white", getColor(WHITE))

                ;
    }

    public static String getPercentageColor(float percentage) {
        if (percentage == 1f) {
            return getColor(GREEN, Attribute.BOLD);
        }

        if (percentage > 0.75f) {
            return getColor(YELLOW, Attribute.BOLD);
        }

        if (percentage > 0.5f) {
            return getColor(YELLOW);
        }

        if (percentage > 0.25f) {
            return getColor(RED, Attribute.BOLD);
        }

        return getColor(WHITE);
    }

    public enum Attribute {
        NORMAL(0),
        BOLD(1),
        ITALIC(3),
        UNDERLINE(4),
        REVERSE(7),
        HIDDEN(8),

        ;

        private final int code;

        Attribute(int code) {
            this.code = code;
        }

        public int code() {
            return this.code;
        }
    }
}
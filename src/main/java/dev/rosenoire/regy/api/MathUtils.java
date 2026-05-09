package dev.rosenoire.regy.api;

public class MathUtils {
    public static int secs2ticks(float seconds) {
        return Math.round(seconds * 20f);
    }
}

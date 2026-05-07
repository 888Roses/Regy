package dev.rosenoire.regy.api;

public class RegyM {
    public static int secToTicks(float seconds) {
        return Math.round(seconds * 20f);
    }
}

package dev.rosenoire.regy.api.event;

public record WrappingCallback(Callback before, Callback after) {
    public static WrappingCallback create() {
        return new WrappingCallback(new Callback(), new Callback());
    }
}

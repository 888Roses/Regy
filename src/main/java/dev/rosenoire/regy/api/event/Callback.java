package dev.rosenoire.regy.api.event;


public class Callback extends AbstractEvent<Runnable> implements Runnable {
    @Override
    public void run() {
        this.forEachSubscriber(Runnable::run);
    }
}

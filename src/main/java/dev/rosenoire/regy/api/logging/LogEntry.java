package dev.rosenoire.regy.api.logging;

import dev.rosenoire.regy.api.data.NonNullConsumer;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class LogEntry {
    private final @NonNull Logger log;
    private final List<NonNullConsumer<Logger>> actions = new ArrayList<>();

    private LogEntry(@NonNull Logger log) {
        this.log = log;
    }

    public static LogEntry of(@NonNull Logger log) {
        return new LogEntry(log);
    }

    public static LogEntry of(@NonNull LogSupplier supplier) {
        return of(supplier.log());
    }

    public LogEntry add(NonNullConsumer<Logger> consumer) {
        this.actions.add(consumer);
        return this;
    }

    public LogEntry add(int index, NonNullConsumer<Logger> consumer) {
        this.actions.add(index, consumer);
        return this;
    }

    public void send() {
        this.actions.forEach(consumer -> consumer.accept(this.log));
    }

    public LogEntry info(String text) {
        return this.add(log -> log.info(LogColor.format(text)));
    }

    public LogEntry info(String text, Object... params) {
        return this.add(log -> log.info(LogColor.format(text), params));
    }

    public LogEntry info(int index, String text) {
        return this.add(index, log -> log.info(LogColor.format(text)));
    }

    public LogEntry info(int index, String text, Object... params) {
        return this.add(index, log -> log.info(LogColor.format(text), params));
    }

    public LogEntry info(String text, Throwable throwable) {
        return this.add(log -> log.info(LogColor.format(text), throwable));
    }

    public LogEntry info(String text, Throwable throwable, Object... params) {
        return this.add(log -> log.info(LogColor.format(text), throwable, params));
    }

    public LogEntry info(int index, String text, Throwable throwable) {
        return this.add(index, log -> log.info(LogColor.format(text), throwable));
    }

    public LogEntry info(int index, String text, Throwable throwable, Object... params) {
        return this.add(index, log -> log.info(LogColor.format(text), throwable, params));
    }

    public LogEntry warn(String text) {
        return this.add(log -> log.warn(LogColor.format(text)));
    }

    public LogEntry warn(String text, Object... params) {
        return this.add(log -> log.warn(LogColor.format(text), params));
    }

    public LogEntry warn(String text, Throwable throwable) {
        return this.add(log -> log.warn(LogColor.format(text), throwable));
    }

    public LogEntry warn(String text, Throwable throwable, Object... params) {
        return this.add(log -> log.warn(LogColor.format(text), throwable, params));
    }

    public LogEntry warn(int index, String text) {
        return this.add(index, log -> log.warn(LogColor.format(text)));
    }

    public LogEntry warn(int index, String text, Object... params) {
        return this.add(index, log -> log.warn(LogColor.format(text), params));
    }

    public LogEntry warn(int index, String text, Throwable throwable) {
        return this.add(index, log -> log.warn(LogColor.format(text), throwable));
    }

    public LogEntry warn(int index, String text, Throwable throwable, Object... params) {
        return this.add(index, log -> log.warn(LogColor.format(text), throwable, params));
    }

    public LogEntry error(String text) {
        return this.add(log -> log.error(LogColor.format(text)));
    }

    public LogEntry error(String text, Object... params) {
        return this.add(log -> log.error(LogColor.format(text), params));
    }

    public LogEntry error(int index, String text) {
        return this.add(index, log -> log.error(LogColor.format(text)));
    }

    public LogEntry error(int index, String text, Object... params) {
        return this.add(index, log -> log.error(LogColor.format(text), params));
    }

    public LogEntry error(String text, Throwable throwable) {
        return this.add(log -> log.error(LogColor.format(text), throwable));
    }

    public LogEntry error(String text, Throwable throwable, Object... params) {
        return this.add(log -> log.error(LogColor.format(text), throwable, params));
    }

    public LogEntry error(int index, String text, Throwable throwable) {
        return this.add(index, log -> log.error(LogColor.format(text), throwable));
    }

    public LogEntry error(int index, String text, Throwable throwable, Object... params) {
        return this.add(index, log -> log.error(LogColor.format(text), throwable, params));
    }
}

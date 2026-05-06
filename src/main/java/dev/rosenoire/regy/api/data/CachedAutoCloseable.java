package dev.rosenoire.regy.api.data;

import org.jspecify.annotations.NonNull;

/// Variant of [Cached] that can handle [AutoCloseable] inheritors.
/// Upon using [#clear()] or when calling [#close()] on an instance of
/// [CachedAutoCloseable], the contained cached value will also be closed, throwing
/// whatever exception in the process.
public class CachedAutoCloseable<A extends AutoCloseable> extends Cached<A> implements AutoCloseable {
    /// Creates a new instance of a [CachedAutoCloseable] value.
    /// @param provider   [NonNullSupplier] providing the cached value every time
    /// [#get()] is called and [#cached] is null.
    /// @param precompute whether to call [#get()] in the constructor or not.
    protected CachedAutoCloseable(@NonNull NonNullSupplier<A> provider, boolean precompute) {
        super(provider, precompute);
    }

    /// Creates a new instance of a [CachedAutoCloseable] value.
    /// @param provider   [NonNullSupplier] providing the cached value every time
    /// [#get()] is called and [#cached] is null.
    /// @param precompute whether to call [#get()] in the constructor or not.
    public static <A extends AutoCloseable> CachedAutoCloseable<A> ofAutoCloseable(@NonNull NonNullSupplier<A> provider, boolean precompute) {
        return new CachedAutoCloseable<>(provider, precompute);
    }

    /// Creates a new instance of a [CachedAutoCloseable] value.
    /// @param provider [NonNullSupplier] providing the cached value every time
    /// [#get()] is called and the cached value is `null`.
    /// @implNote this is a shortcut of:
    /// ```java
    /// of(provider, false)
    /// ```
    /// meaning that by using this method, precomputing the cached value is disabled.
    public static <A extends AutoCloseable> CachedAutoCloseable<A> ofAutoCloseable(@NonNull NonNullSupplier<A> provider) {
        return ofAutoCloseable(provider, false);
    }

    /// Clears the value currently cached in this [Cached].
    /// If the value is not currently `null`, also calls [AutoCloseable#close()] on it,
    /// throwing whatever thrown [Exception] in a [RuntimeException].
    /// For an exception-safe implementation of this method, see [#clearSafe].
    @Override
    public synchronized void clear() {
        try {
            closeCachedValue();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        super.clear();
    }

    /// Clears the value currently cached in this [Cached].
    /// If the value is not currently `null`, also calls [AutoCloseable#close()] on it,
    /// ignoring any thrown [Exception]. For an unsafe implementation of this method,
    /// see [#clear()].
    public synchronized void clearSafe() {
        try {
            clear();
        }
        catch (RuntimeException ignoredRuntimeException) {
        }
    }

    @Override
    public void close() throws Exception {
        closeCachedValue();
    }

    protected void closeCachedValue() throws Exception {
        var value = this.cached;

        if (value != null) {
            value.close();
            return;
        }

        synchronized (this) {
            value = this.cached;

            if (value != null) {
                value.close();
            }
        }
    }
}

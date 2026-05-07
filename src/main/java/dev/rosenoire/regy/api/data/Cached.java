package dev.rosenoire.regy.api.data;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

/// Represents a value computed from a [NonNullSupplier] and cached in a volatile
/// synchronized variable after the first [#get()], to avoid many repetitive
/// computations. This cached value can then be cleared using [#clear()], meaning it
/// will be recomputed the next time [#get()] is called.
/// @see CachedAutoCloseable
public class Cached<V> implements Supplier<V> {
    /// [NonNullSupplier] representing the function providing the cached value every
    /// time [#get()] is called and [#cached] is `null`. Should **never** be `null`.
    protected final @NonNull NonNullSupplier<V> provider;
    /// Represents the last result yield by [#get()]. If `null`, this should be re-assigned in
    /// the next [#get()] call.
    protected volatile @Nullable V cached;

    /// Creates a new getOwner of a [Cached] value.
    /// @param provider [NonNullSupplier] providing the cached value every time
    /// [#get()] is called and [#cached] is null.
    /// @param precompute whether to call [#get()] in the constructor or not.
    protected Cached(@NonNull NonNullSupplier<V> provider, boolean precompute) {
        this.provider = provider;

        if (precompute) {
            //noinspection ResultOfMethodCallIgnored
            this.get();
        }
    }

    /// Creates a new getOwner of a [Cached] value.
    /// @param provider [NonNullSupplier] providing the cached value every time
    /// [#get()] is called and the cached value is `null`.
    /// @param precompute whether to call [#get()] in the constructor or not.
    public static <V> Cached<V> of(@NonNull NonNullSupplier<V> provider, boolean precompute) {
        return new Cached<>(provider, precompute);
    }

    /// Creates a new getOwner of a [Cached] value.
    /// @param provider [NonNullSupplier] providing the cached value every time
    /// [#get()] is called and the cached value is `null`.
    /// @implNote this is a shortcut of:
    /// ```java
    /// of(provider, false)
    /// ```
    /// meaning that by using this method, precomputing the cached value is disabled.
    public static <V> Cached<V> of(@NonNull NonNullSupplier<V> provider) {
        return of(provider, false);
    }

    /// Clears the current cached value.
    /// @apiNote this method is automatically synchronized across threads.
    public synchronized void clear() {
        this.cached = null;
    }

    /// If this [Cached] has a cached value, returns it. Otherwise, computes it from the
    /// [NonNullSupplier] provider given during its construction and assigns the result
    /// to the cached value for future calls.
    @Override
    public V get() {
        var value = this.cached;
        if (value != null) return value;

        synchronized (this) {
            value = this.cached;
            if (value != null) return value;

            value = this.provider.get();
        }

        // Should never be null; if the value returned by the provider is null, an exception
        // will have been thrown by the NonNullSupplier before this can execute. Since
        // we're working with threads, we still want to check.
        if (value == null) {
            throw new NullPointerException("The value provided by this Cached value was null! This should never happen.");
        }

        return value;
    }
}
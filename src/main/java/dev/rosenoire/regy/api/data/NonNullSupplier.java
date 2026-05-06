package dev.rosenoire.regy.api.data;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;
import java.util.function.Supplier;

/// Implementation of Java's [Supplier] only providing non-`null` values.
///
/// @apiNote One may create a new supplier by calling [#ofSupplier(Supplier)].
/// @implNote Returning a `null` value should throw an exception. It may be checked
/// using the [dev.rosenoire.regy.api.data.NonNullType.Validator].
@FunctionalInterface
public interface NonNullSupplier<@NonNullType V> extends Supplier<V> {
    @Override
    @NonNull V get();

    /// Creates a new [NonNullSupplier] using a default Java [Supplier] as a template,
    /// throwing an [Exception] if the provided value is `null`.
    /// @param supplier base [Supplier] to call to get the result.
    static <@NonNullType V> NonNullSupplier<V> ofSupplier(Supplier<V> supplier) {
        return () -> NonNullType.Validator.validate(
                supplier.get(),
                () -> "Received null value from getter of NonNullSupplier. This is not allowed."
        );
    }

    @ApiStatus.NonExtendable
    default <@NonNullType T> @NonNull NonNullSupplier<T> map(Function<V, T> mapper) {
        return () -> mapper.apply(this.get());
    }
}
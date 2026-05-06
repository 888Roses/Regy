package dev.rosenoire.regy.api.data;

import org.jspecify.annotations.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;
import java.util.function.Supplier;

/// Implementation of [NonNull] applicable on type parameters.
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@NonNull
public @interface NonNullType {
    interface Validator {
        static <T> T validate(T value, Supplier<String> message) {
            Objects.requireNonNull(value, message);
            return value;
        }
    }
}

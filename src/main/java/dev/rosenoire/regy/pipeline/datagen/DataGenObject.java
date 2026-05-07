package dev.rosenoire.regy.pipeline.datagen;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyOwnable;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/// Represents any class that can give data to generate to an [AbstractRegy] instance.
public interface DataGenObject extends RegyOwnable {
    /// Gets called just before running data-gen for the [AbstractRegy] owning this
    /// [DataGenObject] and expects the user to provide the [DataGenProvider]
    /// providers that should be run by the data-gen.
    ///
    /// @param collector [DataGenProviderConsumer] collecting every provider
    /// needed to be run.
    void collectDataGenProviders(DataGenProviderConsumer collector);

    /// Extension of a [Consumer] accepting [DataGenProvider].
    /// Overrides the default [#accept(DataGenProvider)] to call the main method
    /// [#addProvider(DataGenProvider)] instead.
    @FunctionalInterface
    interface DataGenProviderConsumer extends Consumer<DataGenProvider> {
        void addProvider(DataGenProvider provider);

        @Override
        @ApiStatus.NonExtendable
        @ApiStatus.Internal
        default void accept(DataGenProvider provider) {
            this.addProvider(provider);
        }
    }
}
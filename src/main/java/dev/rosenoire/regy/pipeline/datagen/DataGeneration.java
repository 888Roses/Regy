package dev.rosenoire.regy.pipeline.datagen;

import dev.rosenoire.regy.common.RegyCommon;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyOwnable;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.DataGenerators;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import java.util.*;

public class DataGeneration implements RegyOwnable {
    private final AbstractRegy<?> regy;
    private final Map<String, DataGenerator> generatorStorage = new HashMap<>();
    private final List<FabricDataGenerator.Pack.Factory<? extends @NonNull DataGenerator>> generatorFunctionStorage = new ArrayList<>();
    private final List<FabricDataGenerator.Pack.RegistryDependentFactory<? extends @NonNull DataGenerator>> registryDependentGeneratorFunctionStorage = new ArrayList<>();
    private final List<DataGenProvider> providerStorage = new ArrayList<>();

    /// Represents whether the game is currently generating data-gen or not.
    private boolean isGeneratingData = false;

    public DataGeneration(AbstractRegy<?> regy) {
        this.regy = regy
                .onAfterSetupDatagen(instance -> this.isGeneratingData = true)
                .onAfterSetupDatagen(instance -> this.destockFunctionStorage())
                .onAfterSetupDatagen(instance -> this.generateProviderStorage());
    }

    @Override
    public @NonNull AbstractRegy<?> regy() {
        return regy;
    }

    /// Represents the [FabricDataGenerator.Pack] data-gen pack targeted by the
    /// generation ran by this [AbstractRegy]'s [DataGeneration].
    public FabricDataGenerator.Pack pack() {
        return this.regy().pack();
    }

    // TODO: Documentation!
    public <T extends DataGenerator & DataProvider> DataGeneration addGenerator(FabricDataGenerator.Pack.RegistryDependentFactory<@NonNull T> factory) {
        if (this.isGeneratingData) {
            // If we're currently generating data-gen, we don't want to add any more
            // generator to the pack since it might invalidate the already generated
            // data. Throw an error;
            // TODO: Add a setting to configure the severity of the exception SOMEWHERE.
            throw new IllegalStateException("Tried adding a data generating while the game was already generating data! This is not allowed!\nFor more information on how to fix this issue, please check the wiki section about data generation and what are the best practices.");
        }

        this.registryDependentGeneratorFunctionStorage.add(factory);
        return this;
    }

    // TODO: Documentation!
    public <T extends DataGenerator & DataProvider> DataGeneration addGenerator(FabricDataGenerator.Pack.Factory<@NonNull T> factory) {
        if (this.isGeneratingData) {
            // If we're currently generating data-gen, we don't want to add any more
            // generator to the pack since it might invalidate the already generated
            // data. Throw an error;
            // TODO: Add a setting to configure the severity of the exception SOMEWHERE.
            throw new IllegalStateException("Tried adding a data generating while the game was already generating data! This is not allowed!\nFor more information on how to fix this issue, please check the wiki section about data generation and what are the best practices.");
        }

        this.generatorFunctionStorage.add(factory);
        return this;
    }

    /// Called just after the [AbstractRegy] instance sets up datagen.
    /// Adds every added generator to the pack and clears the
    /// [#generatorFunctionStorage].
    @SuppressWarnings("LoggingSimilarMessage")
    protected void destockFunctionStorage() {
        for (var function : this.registryDependentGeneratorFunctionStorage) {
            var generator = this.pack().addProvider(function);
            var name = generator.getName();
            this.generatorStorage.put(name, generator);
            RegyCommon.log.info("Successfully destocked generator '{}' of type '{}'!", name, generator.getClass().getSimpleName());
        }

        for (var function : this.generatorFunctionStorage) {
            var generator = this.pack().addProvider(function);
            var name = generator.getName();
            this.generatorStorage.put(name, generator);
            RegyCommon.log.info("Successfully destocked generator '{}' of type '{}'!", name, generator.getClass().getSimpleName());
        }

        this.generatorFunctionStorage.clear();
    }

    // TODO: Documentation!
    protected void generateProviderStorage() {
        for (var provider : this.providerStorage) {
            provider.generate(this);
        }

        this.providerStorage.clear();
    }

    // TODO: Documentation!
    public <T extends DataGenProvider> DataGeneration addData(T dataProvider) {
        this.providerStorage.add(dataProvider);
        return this;
    }

    // TODO: Documentation!
    public <T extends DataGenObject> DataGeneration addData(T dataObject) {
        dataObject.collectDataGenProviders(this::addData);
        return this;
    }

    /// Attempts to retrieve a saved generator with the given name and the given type
    /// and return it.
    /// @param name name of the generator as it was saved when calling
    /// [#addGenerator(FabricDataGenerator.Pack.RegistryDependentFactory)].
    /// @apiNote This method uses hard casting to cast the generic [DataGenerator]
    /// type as it is saved in the stored generators map of this [DataGeneration] into
    /// the [T] type that was given. This means that if the searched generator is not of
    /// the given type, an exception will be thrown.
    /// @see DataGenerators
    @NullMarked
    @SuppressWarnings("unchecked")
    public <T extends DataGenerator & DataProvider> Optional<T> getGeneratorOptional(String name) {
        return Optional.ofNullable(this.generatorStorage.getOrDefault(name, null)).map(genericGenerator -> (T) genericGenerator);
    }
}

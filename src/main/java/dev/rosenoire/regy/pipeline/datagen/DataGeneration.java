package dev.rosenoire.regy.pipeline.datagen;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.client.AbstractClientRegy;
import dev.rosenoire.regy.pipeline.client.ClientRegyOwnable;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.DataGenerators;
import dev.rosenoire.regy.pipeline.datagen.post_processor.DataPostProcessor;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import java.util.*;

@SuppressWarnings("UnusedReturnValue")
public class DataGeneration implements ClientRegyOwnable {
    private final AbstractClientRegy<?, ?> client;

    private final Map<String, DataGenerator> generatorStorage = new HashMap<>();
    private final List<FabricDataGenerator.Pack.Factory<? extends @NonNull DataGenerator>> generatorFunctionStorage = new ArrayList<>();
    private final List<FabricDataGenerator.Pack.RegistryDependentFactory<? extends @NonNull DataGenerator>> registryDependentGeneratorFunctionStorage = new ArrayList<>();
    private final List<DataGenProvider> providerStorage = new ArrayList<>();

    private final HashSet<DataPostProcessor> postProcessorStorage = new HashSet<>();

    /// Represents whether the game is currently generating data-gen or not.
    private boolean isGeneratingData = false;

    public DataGeneration(AbstractClientRegy<?, ?> client) {
        this.client = client;
        this.client.onRunDatagen.after().subscribe(this::runDatagen);
    }

    private void runDatagen() {
        this.isGeneratingData = true;
        this.destockFunctionStorage();
        this.generateProviderStorage();
        this.generatePostProcessors();
    }

    @Override
    public @NonNull AbstractClientRegy<?, ?> client() {
        return this.client;
    }

    /// Represents the [FabricDataGenerator.Pack] data-gen pack targeted by the
    /// generation ran by this [AbstractRegy]'s [DataGeneration].
    public FabricDataGenerator.Pack pack() {
        return this.client().getPack();
    }

    // TODO: Documentation!
    public <T extends DataGenerator & DataProvider> DataGeneration addGenerator(
            FabricDataGenerator.Pack.RegistryDependentFactory<@NonNull T> factory
    ) {
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
        this.log().info("Destocking generator function storage...");

        this.log().info("|---- Destocking registry dependant generator function storage...");
        var destockedCount = 0;
        for (var function : this.registryDependentGeneratorFunctionStorage) {
            try {
                var generator = this.pack().addProvider(function);
                var name = generator.getName();
                var previousGenerator = this.generatorStorage.put(name, generator);
                destockedCount++;

                this.log().info("|------ Successfully destocked '{}' ({}).", name, generator.getClass().getSimpleName());

                if (previousGenerator != null) {
                    this.log().warn(
                            "|-------- Note: Destocking this generator replaced previously destocked generator '{}' ({})",
                            previousGenerator.getName(),
                            previousGenerator.getClass().getSimpleName()
                    );
                }
            }
            catch (Exception exception) {
                this.log().error("|------ Could not destock generator function: ", exception);
            }
        }
        this.log().info("|---- Successfully destocked registry dependant generator function storage! ({}/{})", destockedCount, this.registryDependentGeneratorFunctionStorage.size());
        this.registryDependentGeneratorFunctionStorage.clear();

        this.log().info("|---- Destocking generic generator function storage...");
        destockedCount = 0;
        for (var function : this.generatorFunctionStorage) {
            try {
                var generator = this.pack().addProvider(function);
                var name = generator.getName();
                var previousGenerator = this.generatorStorage.put(name, generator);
                destockedCount++;

                this.log().info("|------ Successfully destocked '{}' ({}).", name, generator.getClass().getSimpleName());

                if (previousGenerator != null) {
                    this.log().warn(
                            "|-------- Note: Destocking this generator replaced previously destocked generator '{}' ({})",
                            previousGenerator.getName(),
                            previousGenerator.getClass().getSimpleName()
                    );
                }
            }
            catch (Exception exception) {
                this.log().error("|------ Could not destock generator function: ", exception);
            }
        }
        this.log().info("|---- Successfully destocked generic generator function storage! ({}/{})", destockedCount, this.generatorFunctionStorage.size());
        this.generatorFunctionStorage.clear();

        this.log().info("|-- Successfully destocked generator function storage!");
    }

    // TODO: Documentation!
    protected void generateProviderStorage() {
        this.log().info("Generating provider storage...");
        var successfulCount = 0;

        for (var provider : this.providerStorage) {
            this.log().info("|---- Generating provider {}...", provider.getClass().getSimpleName());

            try {
                provider.generate(this);
                this.log().info("|------ Successfully generated provider!");
                successfulCount++;
            }
            catch (Exception exception) {
                this.log().error("|------ Could not generate provider: ", exception);
            }
        }

        this.log().info("|-- Successfully generated providers! ({}/{})", successfulCount, this.providerStorage.size());
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
    /// @see DataGenerators
    @NullMarked
    @SuppressWarnings("unchecked")
    public <T extends DataGenerator & DataProvider> Optional<T> getGeneratorOptional(String name) {
        return Optional
                .ofNullable(this.generatorStorage.getOrDefault(name, null))
                .map(genericGenerator -> (T) genericGenerator);
    }

    public DataGeneration registerPostProcessor(@NonNull DataPostProcessor postProcessor) {
        this.log().info("Registered new post processor ({}).", postProcessor.getClass().getSimpleName());
        this.postProcessorStorage.add(postProcessor);
        return this;
    }

    public PostProcessTargetStorage postProcessTargetStorage() {
        return this.regy().postProcessTargetStorage();
    }

    private void generatePostProcessors() {
        this.log().info(
                "Generating datagen for post processor storage... ({} processors {} targets)",
                this.postProcessorStorage.size(),
                this.regy().postProcessTargetStorage().size()
        );

        var successCount = 0;
        for (var postProcessor : this.postProcessorStorage) {
            this.log().info(
                    "|---- Running post processor {}...",
                    postProcessor.getClass().getSimpleName()
            );

            try {
                postProcessor.generate(this);
                this.log().info("|------ Success!");
                successCount++;
            }
            catch (Exception exception) {
                this.log().error("|------ Failure: ", exception);
            }
        }

        this.log().info(
                "|-- Successfully generated post processors! ({}/{})",
                successCount,
                postProcessorStorage.size()
        );
    }
}

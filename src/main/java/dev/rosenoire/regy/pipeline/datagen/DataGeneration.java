package dev.rosenoire.regy.pipeline.datagen;

import dev.rosenoire.regy.api.logging.LogColor;
import dev.rosenoire.regy.api.logging.LogEntry;
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
        LogEntry.of(this).info("|> RUNNING DATAGEN").send();

        this.isGeneratingData = true;
        this.destockFunctionStorage();
        this.generateProviderStorage();
        this.generatePostProcessors();

        LogEntry.of(this).info("<| FINISHED RUNNING DATAGEN").send();
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
        var log = LogEntry.of(this);

        log.info("§white|§end--§cyan|>§end Destocking Generator Function Storages...");

        log.info("§white|§end  §cyan|§end--§yellow|>§end Destocking §whiteRegistry Dependent Generator Function Storage§end...");
        var destockedCount = 0;
        for (var function : this.registryDependentGeneratorFunctionStorage) {
            try {
                var generator = this.pack().addProvider(function);
                var name = generator.getName();
                var previousGenerator = this.generatorStorage.put(name, generator);

                destockedCount++;

                log.info(
                        "§white|§end  §cyan|§end  §yellow|§end  > Destocked §green\"{}\"§end §cyan({})§end",
                        name,
                        generator.getClass().getSimpleName()
                );

                if (previousGenerator != null) {
                    log.info(
                            "§white|§end  §cyan|§end  §yellow|§end    ↪ /!\\ Destocking this generator replaced previously destocked generator §green\"{}\"§end §cyan({})§end",
                            previousGenerator.getName(),
                            previousGenerator.getClass().getSimpleName()
                    );
                }
            }
            catch (Exception exception) {
                log.error("§white|§end  §cyan|§end  §yellow|§end  > Could not destock generator:");
                log.error("§white|§end  §cyan|§end  §yellow|§end    ↪ ", exception);
            }
        }

        log.info(
                "§white|§end  §cyan|§end--§yellow<|§end Destocked {}{}/{}§end generators",
                LogColor.getPercentageColor(destockedCount / (float) this.registryDependentGeneratorFunctionStorage.size()),
                destockedCount,
                this.registryDependentGeneratorFunctionStorage.size()
        );

        this.registryDependentGeneratorFunctionStorage.clear();

        log.info("§white|§end  §cyan|§end--§yellow|>§end Destocking §whiteGeneric Generator Function Storage§end...");
        destockedCount = 0;
        for (var function : this.generatorFunctionStorage) {
            try {
                var generator = this.pack().addProvider(function);
                var name = generator.getName();
                var previousGenerator = this.generatorStorage.put(name, generator);

                destockedCount++;

                log.info(
                        "§white|§end  §cyan|§end  §yellow|§end  > Destocked §green\"{}\"§end §cyan({})§end",
                        name,
                        generator.getClass().getSimpleName()
                );

                if (previousGenerator != null) {
                    log.info(
                            "§white|§end  §cyan|§end  §yellow|§end    ↪ /!\\ Destocking this generator replaced previously destocked generator §green\"{}\"§end §cyan({})§end",
                            previousGenerator.getName(),
                            previousGenerator.getClass().getSimpleName()
                    );
                }
            }
            catch (Exception exception) {
                log.error("§white|§end  §cyan|§end  §yellow|§end  > Could not destock generator:");
                log.error("§white|§end  §cyan|§end  §yellow|§end    ↪ ", exception);
            }
        }

        log.info(
                "§white|§end  §cyan|§end--§yellow<|§end Destocked {}{}/{}§end generators",
                LogColor.getPercentageColor(destockedCount / (float) this.generatorFunctionStorage.size()),
                destockedCount,
                this.generatorFunctionStorage.size()
        );

        this.generatorFunctionStorage.clear();

        log.info("§white|§end--§cyan<|§end Destocked Generator Function Storages.");
        log.send();
    }

    // TODO: Documentation!
    protected void generateProviderStorage() {
        var log = LogEntry.of(this);
        log.info("§white|§end--§cyan|>§end Generating §whiteDataGenProviders§end from Storage...");

        var successfulCount = 0;

        for (var provider : this.providerStorage) {
            try {
                provider.generate(this);
                successfulCount++;

                log.info("§white|§end  §cyan|§end  Generating §whiteDataGenProvider§end §cyan{}§end", provider.getClass().getSimpleName());
            }
            catch (Exception exception) {
                log.error("§white|§end  §cyan|§end  Couldn't generate §whiteDataGenProvider§end §cyan{}§end", provider.getClass().getSimpleName());
                log.error("                         ↪ ", exception);
            }
        }

        log.info(
                "§white|§end--§cyan<|§end Generated {}{}/{}§end §whiteDataGenProviders§end",
                LogColor.getPercentageColor(successfulCount / (float) this.providerStorage.size()),
                successfulCount,
                this.providerStorage.size()
        );

        this.providerStorage.clear();

        log.send();
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
        this.postProcessorStorage.add(postProcessor);

        LogEntry.of(this)
                .info(
                        "|> §whiteDataGeneration:§end Registered DataPostProcessor §cyan{}§end.",
                        postProcessor.getClass().getSimpleName()
                )
                .send();

        return this;
    }

    public PostProcessTargetStorage postProcessTargetStorage() {
        return this.regy().postProcessTargetStorage();
    }

    private void generatePostProcessors() {
        var log = LogEntry.of(this);
        log.info("§white|§end--§cyan|>§end Generating §whitePostProcessors§end from Storage...");
        log.info(
                "§white|§end  §cyan|§end  Detected {} §whiteDataPostProcessor(s)§end, {} §whitePostProcessTarget(s)§end",
                this.postProcessorStorage.size(),
                this.regy().postProcessTargetStorage().size()
        );

        var successCount = 0;
        for (var postProcessor : this.postProcessorStorage) {
            try {
                log.info(
                        "§white|§end  §cyan|§end  §yellow|§end  > Generating §cyan{}§end... §white(DataPostProcessor)§end",
                        postProcessor.getClass().getSimpleName()
                );

                postProcessor.generate(this);

                successCount++;
            }
            catch (Exception exception) {
                log.error(
                        "§white|§end  §cyan|§end  §yellow|§end  > Couldn't generate §cyan{}§end... §white(DataPostProcessor)§end",
                        postProcessor.getClass().getSimpleName()
                );
                log.error(
                        "                                         ↪ ",
                        exception
                );
            }
        }

        log.info(
                "§white|§end--§cyan<|§end Generated {}{}/{}§end §whiteDataGenProviders§end",
                LogColor.getPercentageColor(successCount / (float) this.postProcessorStorage.size()),
                successCount,
                this.postProcessorStorage.size()
        );

        log.send();
    }
}

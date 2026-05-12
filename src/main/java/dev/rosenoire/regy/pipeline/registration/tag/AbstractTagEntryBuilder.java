package dev.rosenoire.regy.pipeline.registration.tag;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.TagDataGenerator;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class AbstractTagEntryBuilder<P, T, E extends AbstractTagEntry<T>, G extends TagDataGenerator<T, G> & DataProvider, B extends AbstractTagEntryBuilder<P, T, E, G, B>> extends AbstractEntryBuilder<E, P> implements DataGenObject {
    protected final List<@NonNull Consumer<@NonNull TagAppender<T, T>>> appenderInstructions = new ArrayList<>();

    protected final ResourceKey<Registry<T>> registryResourceKey;
    protected final BiFunction<TagKey<T>, Identifier, E> entryFactory;
    protected final String dataGeneratorName;

    public AbstractTagEntryBuilder(@NonNull AbstractRegy<?> regy, P parent, String identifier, ResourceKey<Registry<T>> registryResourceKey, BiFunction<TagKey<T>, Identifier, E> entryFactory, String dataGeneratorName) {
        super(regy, parent, identifier);
        this.registryResourceKey = registryResourceKey;
        this.entryFactory = entryFactory;
        this.dataGeneratorName = dataGeneratorName;
    }

    @Override
    public @NonNull E register() {
        var tag = TagKey.create(this.registryResourceKey, identifier());
        var entry = getRegy().entry(this.entryFactory.apply(tag, regyIdentifier()));

        this.state = new DataGenState<>(tag);
        getRegy().dataGeneration().addData(this);

        return entry;
    }

    @Override
    protected Identifier regyIdentifier() {
        return identifier().withPrefix("tag/" + registryResourceKey.identifier().getPath() + "/");
    }

    public B self() {
        //noinspection unchecked
        return (B) this;
    }

    public B instruction(@NonNull Consumer<@NonNull TagAppender<T, T>> instruction) {
        this.appenderInstructions.add(instruction);
        return self();
    }

    // region data generation

    protected DataGenState<T> state;

    public DataGenState<T> getState() {
        return state;
    }

    @Override
    public void collectDataGenProviders(DataGenProviderConsumer collector) {
        collector.addProvider(this::dataGenTagProvider);
    }

    private void dataGenTagProvider(DataGeneration dataGeneration) {
        dataGeneration.<G>getGeneratorOptional(dataGeneratorName).ifPresent(generator -> {
            generator.tag(state.key(), builder -> {
                synchronized (this.appenderInstructions) {
                    this.appenderInstructions.forEach(instruction -> instruction.accept(builder));
                }

                return builder;
            });
        });
    }

    public record DataGenState<T>(TagKey<T> key) {
    }

    // endregion
}

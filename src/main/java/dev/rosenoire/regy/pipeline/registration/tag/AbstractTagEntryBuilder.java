package dev.rosenoire.regy.pipeline.registration.tag;

import dev.rosenoire.regy.api.data.NonNullConsumer;
import dev.rosenoire.regy.api.logging.LogEntry;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.TagDataGenerator;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTagEntryBuilder
        <
                PARENT,
                TYPE,
                ENTRY extends AbstractTagEntry<TYPE>,
                GENERATOR extends TagDataGenerator<TYPE, GENERATOR> & DataProvider,
                SELF extends AbstractTagEntryBuilder<PARENT, TYPE, ENTRY, GENERATOR, SELF>
                >
        extends AbstractEntryBuilder<ENTRY, PARENT> {

    /// [ResourceKey] representing the [Registry] represented by the type of [TagKey]
    /// contained by this [AbstractTagEntryBuilder].
    protected final @NonNull ResourceKey<Registry<TYPE>> registry;

    /// [GenericTagFactory] responsible with creating an instance of [ENTRY] from the
    /// given parameters.
    protected final @NonNull GenericTagFactory<TYPE, ENTRY> factory;

    /// Represents the name of generator used to generate the tag data generation.
    /// This is used in combination with [DataGeneration#getGeneratorOptional(String)]
    /// to attempt to obtain the generator.
    protected final @NonNull String generatorName;

    protected final List<@NonNull NonNullConsumer<TagDefinition>> instructionStorage = new ArrayList<>();

    public AbstractTagEntryBuilder(
            @NonNull AbstractRegy<?> regy,
            PARENT parent,
            @NonNull String identifier,
            @NonNull ResourceKey<Registry<TYPE>> registry,
            @NonNull GenericTagFactory<TYPE, ENTRY> factory,
            @NonNull String generatorName
    ) {
        super(regy, parent, identifier);
        this.registry = registry;
        this.factory = factory;
        this.generatorName = generatorName;
    }

    @Override
    public @NonNull ENTRY register() {
        var log = LogEntry.of(this);

        log.info("|> §bold§cyan({})§end §green\"{}\"§end §blue({})§end",
                this.getClass().getSimpleName(),
                this.identifier(),
                "registry: \"" + this.registry.identifier() + "\""
        );

        log.info("|  > Creating TagKey...");
        var tag = TagKey.create(this.registry, this.identifier());

        log.info("|  > Creating TagEntry...");
        var entry = regy().entry(this.factory.bake(
                tag,
                this.regyIdentifier(),
                this.identifier()
        ));

        log.info("|  > Adding TagKey as PostProcess target...");
        this.regy().postProcessTargetStorage().addPostProcessTarget(new GenericTagPostProcessTarget(
                this.generatorName,
                tag,
                this.instructionStorage
        ));

        log.send();
        return entry;
    }

    @Override
    protected Identifier regyIdentifier() {
        return identifier().withPrefix("tag/" + registry.identifier().getPath() + "/");
    }

    public SELF self() {
        //noinspection unchecked
        return (SELF) this;
    }

    public SELF instruction(@NonNull NonNullConsumer<TagDefinition> instruction) {
        this.instructionStorage.add(instruction);
        return self();
    }
}
package dev.rosenoire.regy.pipeline.registration.block;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.factory.BlockFactory;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class BlockEntryBuilder<B extends Block, P> extends AbstractEntryBuilder<BlockEntry<B>, P> {
    /// Factory used to create the final instance of [B] of the to-be-registered [BlockEntry].
    protected final BlockFactory<B> factory;
    /// [ResourceKey] of [Block] representing the key that will be used to register the entry.
    protected final ResourceKey<Block> resourceKey;
    /// [BlockBehaviour.Properties] used to register the [BlockEntry].
    protected BlockBehaviour.@NonNull Properties properties;
    protected @Nullable ItemEntryBuilder<?, BlockEntryBuilder<B, P>> unbakedItem;
    protected @NonNull BlockRenderMode renderMode = BlockRenderMode.SOLID;
    private final HashSet<TagKey<Block>> tagStorage = new HashSet<>();

    public BlockEntryBuilder(AbstractRegy<?> regy, P parent, String identifier, BlockFactory<B> factory) {
        super(regy, parent, identifier);

        this.factory = factory;
        this.resourceKey = ResourceKey.create(Registries.BLOCK, identifier());
        this.properties = BlockBehaviour.Properties.of();
    }

    @Override
    public @NonNull BlockEntry<B> register() {
        regy().log.info("Starting registration for block '{}'...", identifier());

        regy().log.info("|---- Setting property ID and registering...");
        this.properties.setId(resourceKey);
        var instance = this.factory.bake(this.properties);
        Registry.register(BuiltInRegistries.BLOCK, resourceKey, instance);

        regy().log.info("|---- Creating Block Entry...");
        var entry = regy().entry(new BlockEntry<>(
                instance,
                this.resourceKey,
                this.renderMode,
                this.tagStorage
        ));

        if (this.unbakedItem != null) {
            regy().log.info("|---- Registering unbaked item...");
            this.unbakedItem.register();
            regy().log.info("|---- Finished registering unbaked item...");
        }

        regy().log.info("|-- Finished registration successfully!");
        return entry;
    }

    @Override
    protected Identifier regyIdentifier() {
        return getRegyIdentifierFromRegistry(this.resourceKey);
    }

    // region modifiers

    public <A> A transform(Function<BlockEntryBuilder<B, P>, A> transformer) {
        return transformer.apply(this);
    }

    // region rendering

    public BlockEntryBuilder<B, P> renderMode(@NonNull BlockRenderMode renderMode) {
        this.renderMode = renderMode;
        return this;
    }

    public BlockEntryBuilder<B, P> solid() {
        return renderMode(BlockRenderMode.SOLID);
    }

    public BlockEntryBuilder<B, P> cutout() {
        return renderMode(BlockRenderMode.CUTOUT);
    }

    public BlockEntryBuilder<B, P> translucent() {
        return renderMode(BlockRenderMode.TRANSLUCENT);
    }

    // endregion

    // region properties

    public BlockEntryBuilder<B, P> properties(BlockBehaviour.@NonNull Properties properties) {
        this.properties = properties;
        return this;
    }

    public BlockEntryBuilder<B, P> properties(UnaryOperator<BlockBehaviour.@NonNull Properties> function) {
        return this.properties(function.apply(this.properties));
    }

    public BlockEntryBuilder<B, P> properties(BlockBehaviour source) {
        return this.properties(BlockBehaviour.Properties.ofFullCopy(source));
    }

    public BlockEntryBuilder<B, P> properties(BlockEntry<?> source) {
        return this.properties(source.get());
    }

    // endregion

    // region item

    public <I extends Item> ItemEntryBuilder<I, BlockEntryBuilder<B, P>> item(BlockItemFactory<B, I> factory) {
        //noinspection OptionalGetWithoutIsPresent
        ItemEntryBuilder<I, BlockEntryBuilder<B, P>> builder = this.regy()
                .item(
                        path(),
                        this,
                        (entryBuilder, properties) ->
                                factory.blockItem(this.entry().get().get(), properties)
                );

        builder.properties(Item.Properties::useBlockDescriptionPrefix);

        this.unbakedItem = builder;
        return builder;
    }

    public ItemEntryBuilder<BlockItem, BlockEntryBuilder<B, P>> item() {
        return item(BlockItem::new);
    }

    public BlockEntryBuilder<B, P> simpleItem() {
        return item().build();
    }

    // endregion

    // region tags

    public BlockEntryBuilder<B, P> tag(TagKey<Block> tag) {
        if (this.tagStorage.contains(tag)) {
            return this;
        }

        this.tagStorage.add(tag);
        return this;
    }

    // endregion

    // endregion
}
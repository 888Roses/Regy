package dev.rosenoire.regy.pipeline.datagen.provider.tag;

import dev.rosenoire.regy.pipeline.datagen.ProviderContext;
import dev.rosenoire.regy.pipeline.datagen.ProviderType;
import dev.rosenoire.regy.pipeline.datagen.provider.DefaultDatagenProvider;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class ItemTagDatagenProvider extends FabricTagProvider.ItemTagProvider implements DefaultDatagenProvider {
    private final ProviderContext ctx;

    public ItemTagDatagenProvider(ProviderContext ctx) {
        super(ctx.dataOutput(), ctx.registryLookup());
        this.ctx = ctx;
    }

    @Override
    public ProviderType getType() {
        return ProviderType.ITEM_TAG;
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        for (var entry : ctx.getOwner().datagenTargets.entrySet()) {
            if (entry.getKey() instanceof DatagenTagTarget tagTarget && entry.getValue() instanceof ItemEntry<? extends Item> itemEntry) {
                for (var tag : tagTarget.getAllTags()) {
                    tag.cast(Registries.ITEM).ifPresent(key -> {
                        valueLookupBuilder(key)
                                .add(itemEntry.get())
                                .setReplace(false);
                    });
                }
            }
        }
    }
}

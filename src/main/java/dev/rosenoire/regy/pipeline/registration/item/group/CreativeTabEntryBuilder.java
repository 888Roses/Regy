package dev.rosenoire.regy.pipeline.registration.item.group;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import dev.rosenoire.regy.api.data.RegistryUtils;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.DataGenerators;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.LangDataGenerator;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("unused")
public class CreativeTabEntryBuilder<P> extends AbstractEntryBuilder<CreativeTabEntry, P> implements DataGenObject {
    private static final Identifier DEFAULT_BACKGROUND_TEXTURE = CreativeModeTab.createTextureLocation("items");

    private final ResourceKey<CreativeModeTab> resourceKey;

    private @Nullable String translatableTitle;
    private @NonNull Component titleComponent = Component.empty();
    private @NonNull NonNullSupplier<ItemStack> iconSupplier = () -> ItemStack.EMPTY;
    private boolean showName;
    private boolean showScrollbar;
    private @NonNull Identifier backgroundTexture = DEFAULT_BACKGROUND_TEXTURE;
    private boolean registerAsMainTab;

    public CreativeTabEntryBuilder(@NonNull AbstractRegy<?> owner, P parent, String identifier) {
        super(owner, parent, identifier);
        this.resourceKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, identifier());
    }

    @Override
    public @NonNull CreativeTabEntry register() {
        var builder = FabricItemGroup.builder()
                .title(titleComponent)
                .icon(iconSupplier)
                .backgroundTexture(this.backgroundTexture);

        if (!showName) builder.hideTitle();
        if (!showScrollbar) builder.noScrollBar();

        return Optional.of(builder)
                .map(CreativeModeTab.Builder::build)
                .map(tab -> Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, resourceKey, tab))
                .map(registered -> new CreativeTabEntry(registered, resourceKey))
                .map(registered -> {
                    if (registerAsMainTab) getRegy().creativeTabMapper.setMainTab(registered.get());
                    return registered;
                })
                .orElseThrow(this::throwRegisterNullEntryException);
    }

    @Override
    protected Identifier regyIdentifier() {
        return getRegyIdentifierFromRegistry(this.resourceKey);
    }

    // region modifiers

    public CreativeTabEntryBuilder<P> mainTab() {
        this.registerAsMainTab = true;
        return this;
    }

    public CreativeTabEntryBuilder<P> name(Component component) {
        this.titleComponent = component;
        return this;
    }

    public CreativeTabEntryBuilder<P> name(@NonNull String lang) {
        this.translatableTitle = lang;
        return name(Component.translatable(lang));
    }

    public CreativeTabEntryBuilder<P> iconStack(@NonNull NonNullSupplier<ItemStack> supplier) {
        this.iconSupplier = supplier;
        return this;
    }

    public CreativeTabEntryBuilder<P> icon(@NonNull NonNullSupplier<Item> supplier) {
        return iconStack(supplier.map(Item::getDefaultInstance));
    }

    public CreativeTabEntryBuilder<P> showName(boolean show) {
        this.showName = show;
        return this;
    }

    public CreativeTabEntryBuilder<P> showScrollbar(boolean show) {
        this.showScrollbar = show;
        return this;
    }

    public CreativeTabEntryBuilder<P> texture(@NonNull Identifier texture) {
        this.backgroundTexture = texture;
        return this;
    }

    // endregion

    // region data generation

    @Override
    public void collectDataGenProviders(DataGenProviderConsumer collector) {
        if (translatableTitle != null) {
            collector.addProvider(dataGen ->
                    dataGen.<LangDataGenerator>getGeneratorOptional(DataGenerators.LANG).ifPresent(generator ->
                            generator.add(RegistryUtils.toDescriptionId(resourceKey), translatableTitle)
                    )
            );
        }
    }

    // endregion
}

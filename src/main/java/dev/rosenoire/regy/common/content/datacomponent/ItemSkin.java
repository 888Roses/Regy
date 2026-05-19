package dev.rosenoire.regy.common.content.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rosenoire.regy.api.MathUtils;
import dev.rosenoire.regy.skins.item.client.ItemSkinClientProxy;
import it.unimi.dsi.fastutil.ints.IntUnaryOperator;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public record ItemSkin(int index, List<String> skins) {
    public static final String DEFAULT_SKIN_IDENTIFIER = "default";
    public static final List<String> DEFAULT_SKINS = List.of(DEFAULT_SKIN_IDENTIFIER);

    public static final Codec<ItemSkin> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Codec.INT.optionalFieldOf("index", 0).forGetter(ItemSkin::index),
                    Codec.STRING.listOf().optionalFieldOf("skins", DEFAULT_SKINS).forGetter(ItemSkin::skins)
            )
            .apply(instance, ItemSkin::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemSkin> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ItemSkin::index,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), ItemSkin::skins,
            ItemSkin::new
    );

    public static Builder create() {
        return new Builder();
    }

    public Builder mutable() {
        return new Builder(this.index, this.skins);
    }

    public static class Builder {
        protected int index;
        protected final List<String> skins;

        protected Builder(int index) {
            this.index = index;
            this.skins = new ArrayList<>(DEFAULT_SKINS);
        }

        protected Builder(int index, List<String> skins) {
            this.index = index;
            this.skins = new ArrayList<>(skins);
        }

        protected Builder() {
            this(0);
        }

        public Builder transform(Consumer<Builder> transformer) {
            transformer.accept(this);
            return this;
        }

        public Builder index(int index) {
            this.index = index;
            return this;
        }

        public Builder transformIndex(IntUnaryOperator transformer) {
            return index(transformer.apply(this.index));
        }

        public Builder incrementIndex(int increment) {
            return transformIndex(current -> MathUtils.wrapInCollection(current + increment, this.skins));
        }

        public Builder incrementIndex() {
            return this.incrementIndex(1);
        }

        public Builder decrementIndex(int decrement) {
            return this.incrementIndex(-decrement);
        }

        public Builder decrementIndex() {
            return this.decrementIndex(1);
        }

        public Builder addSkin(String skin) {
            this.skins.add(skin);
            return this;
        }

        public ItemSkin build() {
            return new ItemSkin(this.index, this.skins);
        }
    }
}

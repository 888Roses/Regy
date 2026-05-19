package dev.rosenoire.regy.test.common.index;

import com.mojang.serialization.Codec;
import dev.rosenoire.regy.pipeline.registration.item.component.DataComponentEntry;
import net.minecraft.network.codec.ByteBufCodecs;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

public interface AllDataComponentTypes {
    DataComponentEntry<Integer> UWU_METER = REGY
            .<Integer>component("uwu_meter")
            .codec(Codec.INT)
            .streamCodec(ByteBufCodecs.INT)
            .register();

    static void register() {
    }
}

package dev.rosenoire.regy.pipeline.datagen.impl.adapter;

import dev.rosenoire.regy.pipeline.client.ClientEntryBuilderAdapter;

public interface DefaultAdapters {
    ClientEntryBuilderAdapter ITEM = new ItemAdapter();
    ClientEntryBuilderAdapter BLOCK = new BlockAdapter();
    ClientEntryBuilderAdapter POTION = new PotionAdapter();
    ClientEntryBuilderAdapter SOUND = new SoundAdapter();
}

package dev.rosenoire.regy.pipeline.datagen.post_processor;

import dev.rosenoire.regy.pipeline.datagen.DataGeneration;

public interface DataPostProcessor {
    void generate(DataGeneration dataGeneration);
}

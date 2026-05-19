package dev.rosenoire.regy.pipeline.datagen.impl.post_processor;

import dev.rosenoire.regy.pipeline.datagen.post_processor.DataPostProcessor;

public interface DefaultPostProcessors {
    DataPostProcessor TAG = new GenericTagDataProcessor();
}
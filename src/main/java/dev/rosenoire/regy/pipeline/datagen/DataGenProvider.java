package dev.rosenoire.regy.pipeline.datagen;

/// Represents a manipulator class transforming [DataGenObject] to be generated
/// by the [DataGenerator] stored in the instance of [AbstractRegy].
public interface DataGenProvider {
    void generate(DataGeneration dataGen);
}
package dev.rosenoire.regy.pipeline;

import dev.rosenoire.regy.pipeline.datagen.v2.impl.generator.LangDataGenerator;
import dev.rosenoire.regy.pipeline.datagen.v2.impl.generator.ModelDataGenerator;

/// Represents a default implementation of the [AbstractRegy] class.
/// This class is `final`; if you wish to extend the behaviour of a Regy registry,
/// extend [AbstractRegy] instead.
public final class Regy extends AbstractRegy<Regy> {
    private Regy(String modNamespace) {
        super(modNamespace);

        onBeforeSetupDatagen(instance -> instance.dataGeneration()
                .addGenerator(LangDataGenerator::new)
                .addGenerator(ModelDataGenerator::new)
        );
    }

    /// Creates a new getOwner of the default Regy class using the given `namespace`
    /// as the mod namespace for registration, etc.
    public static Regy create(String modNamespace) {
        return new Regy(modNamespace);
    }
}

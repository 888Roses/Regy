package dev.rosenoire.regy.pipeline.datagen.impl.post_processor;

import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.TagDataGenerator;
import dev.rosenoire.regy.pipeline.datagen.post_processor.DataPostProcessor;
import dev.rosenoire.regy.pipeline.registration.tag.GenericTagPostProcessTarget;

public class GenericTagDataProcessor implements DataPostProcessor {
    @Override
    public void generate(DataGeneration dataGeneration) {
        for (var target : dataGeneration.postProcessTargetStorage().getPostProcessTargetsOfClass(GenericTagPostProcessTarget.class)) {
            try {
                dataGeneration.getGeneratorOptional(target.generatorName()).ifPresent(generator -> {
                    if (generator instanceof TagDataGenerator<?, ?> tagDataGenerator) {
                        //noinspection unchecked
                        tagDataGenerator.tag(target.key(), definition -> {
                            target.instructionStorage().forEach(instruction -> instruction.accept(definition));
                            return definition;
                        });
                    }
                });
            }
            catch (Exception ignoredException) {
            }
        }
    }
}

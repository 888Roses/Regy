package dev.rosenoire.regy.pipeline.datagen.impl.post_processor;

import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.TagDataGenerator;
import dev.rosenoire.regy.pipeline.datagen.post_processor.DataPostProcessor;
import dev.rosenoire.regy.pipeline.registration.tag.GenericTagPostProcessTarget;

import java.util.concurrent.atomic.AtomicInteger;

public interface DefaultPostProcessors {
    DataPostProcessor TAG = new GenericTagDataProcessor();

    class GenericTagDataProcessor implements DataPostProcessor {
        @Override
        public void generate(DataGeneration dataGeneration) {
            dataGeneration.log().info("|-- Generating generic tag post data...");
            var targets = dataGeneration.postProcessTargetStorage().getPostProcessTargetsOfClass(GenericTagPostProcessTarget.class);
            AtomicInteger successCount = new AtomicInteger();
            for (var target : targets) {
                dataGeneration.log().info("|---- Generating tag '{}'...", target.key().location());

                try {
                    dataGeneration.getGeneratorOptional(target.generatorName()).ifPresent(generator -> {
                        if (generator instanceof TagDataGenerator<?, ?> tagDataGenerator) {
                            //noinspection unchecked
                            tagDataGenerator.tag(target.key(), definition -> {
                                target.instructionStorage().forEach(instruction -> instruction.accept(definition));
                                successCount.getAndIncrement();
                                dataGeneration.log().info("|------ Success!");
                                return definition;
                            });
                        }
                    });
                }
                catch (Exception exception) {
                    dataGeneration.log().error("|------ Failed: ", exception);
                }
            }

            dataGeneration.log().info(
                    "|---- Finished generating generic tag post data successfully! ({}/{})",
                    successCount,
                    targets.size()
            );
        }
    }
}

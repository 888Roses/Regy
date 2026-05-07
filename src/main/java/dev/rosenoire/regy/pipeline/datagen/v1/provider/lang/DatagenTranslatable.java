package dev.rosenoire.regy.pipeline.datagen.v1.provider.lang;

public interface DatagenTranslatable {
    Translation getDatagenTranslation();

    record Translation(String key, String value) {}
}

package dev.rosenoire.regy.pipeline.datagen.provider.lang;

public interface DatagenTranslatable {
    Translation getDatagenTranslation();

    record Translation(String key, String value) {}
}

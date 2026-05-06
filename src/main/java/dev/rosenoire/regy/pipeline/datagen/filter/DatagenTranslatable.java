package dev.rosenoire.regy.pipeline.datagen.filter;

public interface DatagenTranslatable {
    Translation getDatagenTranslation();

    record Translation(String key, String value) {}
}

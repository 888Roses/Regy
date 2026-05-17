package dev.rosenoire.regy.tooltips.builder;

public record SummaryKey(String summary) implements TooltipKey {
    @Override
    public void generate(TooltipBuilder builder, int index) {
        // Since there's only one summary per item there's no need to use the index here.
        builder.internal_addTranslation(builder.internal_getDescriptionIdWithName("summary"), this.summary());
    }
}

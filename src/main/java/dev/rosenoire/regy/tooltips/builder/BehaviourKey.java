package dev.rosenoire.regy.tooltips.builder;

public record BehaviourKey(String condition, String behaviour) implements TooltipKey {
    @Override
    public void generate(TooltipBuilder builder, int index) {
        builder.internal_addTranslation(builder.internal_getDescriptionIdWithName("behaviour" + index), this.behaviour());
        builder.internal_addTranslation(builder.internal_getDescriptionIdWithName("condition" + index), this.condition());
    }
}

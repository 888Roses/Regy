package dev.rosenoire.regy.tooltips.builder;

import dev.rosenoire.regy.api.data.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class TooltipBuilder {
    public final String baseDescId;
    private final BiConsumer<String, String> method;
    private final List<TooltipKey> tooltip = new ArrayList<>();

    public TooltipBuilder(String baseDescId, BiConsumer<String, String> method) {
        this.baseDescId = baseDescId;
        this.method = method;
    }

    public TooltipBuilder insert(TooltipKey key) {
        this.tooltip.add(key);
        return this;
    }

    public TooltipBuilder summary(String summary) {
        return insert(new SummaryKey(summary));
    }

    public TooltipBuilder behaviour(String condition, String behaviour) {
        return insert(new BehaviourKey(condition, behaviour));
    }

    public void internal_addTranslation(String key, String value) {
        this.method.accept(key, value);
    }

    public String internal_getDescriptionIdWithName(String suffix) {
        return baseDescId + ".tooltip." + suffix;
    }

    public void build() {
        var classCounter = new HashMap<Class<? extends TooltipKey>, Integer>();
        for (var key : this.tooltip) {
            var clazz = key.getClass();
            var index = DataUtils.getOrAddAndGetFallback(classCounter, clazz, 1);
            key.generate(this, index);
            classCounter.put(clazz, index + 1);
        }
    }
}

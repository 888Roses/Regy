package dev.rosenoire.regy.api.text;

import dev.rosenoire.regy.api.data.NonNullType;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public enum NamingConventions implements StringRepresentable {
    HUMAN_TEXT("human_text", input ->
            Arrays.stream(input.toLowerCase(Locale.ROOT).split("_"))
                    .map(StringUtils::capitalize)
                    .collect(Collectors.joining(" "))
    ),
    SENTENCE("sentence", input -> String
            .join(" ", input.toLowerCase(Locale.ROOT).split("_"))
            .transform(StringUtils::capitalize)
    ),

    ;

    private final String name;
    private final UnaryOperator<@NonNullType String> transformer;

    NamingConventions(String name, UnaryOperator<String> transformer) {
        this.name = name;
        this.transformer = transformer;
    }

    @Override
    public @NonNull String getSerializedName() {
        return name;
    }

    public @NonNull String transform(@NonNull String input) {
        return this.transformer.apply(input);
    }
}

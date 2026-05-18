package dev.rosenoire.regy.test.data;

import dev.rosenoire.regy.test.common.index.AllItems;

import static dev.rosenoire.regy.test.client.TestModClient.REGY;

public interface AllItemData {
    static void init() {
        REGY.item(AllItems.ELDRITCH_BLESSING)
                .tooltip(builder -> builder
                        .summary("Launches your home-grown vegetables at Enemies. Can be powered with _Air Pressure_ from a _Backtank_.")
                        .behaviour("When R-Clicked", "_Shoots_ a suitable item from your _Inventory_.")
                        .behaviour("While wearing Backtank", "_No Durability_ will be used. Instead, _Air Pressure_ is drained from the Tank.")
                )
                .register();
    }
}

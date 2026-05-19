package dev.rosenoire.regy.test.data;

import dev.rosenoire.regy.test.common.index.AllBlocks;

import static dev.rosenoire.regy.test.client.TestModClient.REGY;

public interface AllBlockData {
    static void init() {
        REGY.block(AllBlocks.ELDEN_BLOCK)
                .register();
    }
}

package dev.rosenoire.regy.test.common.content.block;

import dev.rosenoire.regy.test.common.foundation.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GridConstrainedBlock extends Block {
    protected final int gridSize;

    public GridConstrainedBlock(Properties properties, int gridSize) {
        super(properties);
        this.gridSize = gridSize;
    }

    public void forEachBlockInGrid(@NonNull BlockPos blockPos, @NonNull Consumer<@NonNull BlockPos> consumer) {
        var x = Math.floor(blockPos.getX() / (double) gridSize) * gridSize;
        var z = Math.floor(blockPos.getZ() / (double) gridSize) * gridSize;
        var cellStart = BlockPos.containing(x, blockPos.getY(), z);
        var cellEnd = cellStart.offset(gridSize - 1, 0, gridSize - 1);
        var cell = AABB.encapsulatingFullBlocks(cellStart, cellEnd);

        Util.forEachBlockPos(cell, consumer);
    }

    public List<BlockPos> getCellNeighbouringPos(@NonNull BlockPos blockPos) {
        var list = new ArrayList<BlockPos>();
        forEachBlockInGrid(blockPos, list::add);
        return list;
    }

    @Override
    protected @NonNull BlockState updateShape(
            @NonNull BlockState blockState,
            @NonNull LevelReader levelReader,
            @NonNull ScheduledTickAccess scheduledTickAccess,
            @NonNull BlockPos blockPos,
            @NonNull Direction direction,
            @NonNull BlockPos neighbourPosition,
            @NonNull BlockState neighbourState,
            @NonNull RandomSource random
    ) {
        if (neighbourState.is(this)) {
            return blockState;
        }

        var cellNeighbours = getCellNeighbouringPos(blockPos);
        if (!cellNeighbours.contains(neighbourPosition)) {
            return blockState;
        }

        return Blocks.AIR.defaultBlockState();
    }

    @Override
    protected void onPlace(
            @NonNull BlockState blockState,
            @NonNull Level level,
            @NonNull BlockPos blockPos,
            @NonNull BlockState oldState,
            boolean notify
    ) {
        forEachBlockInGrid(blockPos, pos -> {
            if (pos.equals(blockPos)) {
                return;
            }

            if (level.getBlockState(pos).is(this)) {
                return;
            }

            level.setBlock(pos, this.defaultBlockState(), UPDATE_ALL);
        });
    }

    private boolean canReplacePosition(@NonNull Level level, @NonNull BlockPos blockPos, @NonNull BlockPlaceContext context) {
        return context.replacingClickedOnBlock() || level.getBlockState(blockPos).canBeReplaced(context);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NonNull BlockPlaceContext context) {
        var cellNeighbours = getCellNeighbouringPos(context.getClickedPos());
        var level = context.getLevel();

        for (var neighbourPosition : cellNeighbours) {
            if (!canReplacePosition(level, neighbourPosition, context)) {
                return null;
            }
        }

        return super.getStateForPlacement(context);
    }
}

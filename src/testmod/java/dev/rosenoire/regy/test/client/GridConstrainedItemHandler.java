package dev.rosenoire.regy.test.client;

import dev.rosenoire.regy.foundation.extensions.BlockItemExtension;
import dev.rosenoire.regy.test.common.content.block.GridConstrainedBlock;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class GridConstrainedItemHandler {
    private static final HashSet<NaiveRenderData> renderedPositions = new HashSet<>();

    private GridConstrainedItemHandler() {
    }

    public static void tick(Minecraft mc) {
        renderedPositions.clear();

        var player = mc.player;
        var level = mc.level;

        if (player == null || level == null) {
            return;
        }

        if (mc.screen != null) {
            return;
        }

        var target = mc.hitResult;

        var itemStack = player.getMainHandItem();
        var interactionHand = InteractionHand.MAIN_HAND;

        if (itemStack.isEmpty()) {
            itemStack = player.getOffhandItem();
            interactionHand = InteractionHand.OFF_HAND;
        }

        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof GridConstrainedBlock gridConstrainedBlock)) {
            return;
        }

        if (!(target instanceof BlockHitResult blockHitResult) || blockHitResult.getType() == HitResult.Type.MISS || !(blockItem instanceof BlockItemExtension extension)) {
            return;
        }

        var context = new BlockPlaceContext(level, player, interactionHand, itemStack, blockHitResult);

        var blockPos = context.getClickedPos();
        var gridPositions = new ArrayList<BlockPos>();
        gridConstrainedBlock.forEachBlockInGrid(blockPos, gridPositions::add);

        synchronized (renderedPositions) {
            var nonNullBlockState = gridConstrainedBlock.defaultBlockState();
            for (var gridPosition : gridPositions) {
                var contextPos = gridPosition.relative(
                        context.getClickedFace().getOpposite(),
                        level.getBlockState(gridPosition).canBeReplaced() ? 0 : 1
                );

                var gridPositionContext = new BlockPlaceContext(
                        level,
                        player,
                        interactionHand,
                        itemStack,
                        blockHitResult.withPosition(contextPos)
                );

                var placedBlockstate = extension.regy$getPlacementState(gridPositionContext);
                var isValidPlacement = placedBlockstate != null;

                if (isValidPlacement) {
                    nonNullBlockState = placedBlockstate;
                }

                renderedPositions.add(NaiveRenderData.create(level, gridPosition, nonNullBlockState, isValidPlacement));
            }
        }
    }

    record NaiveRenderData(BlockState state, BlockPos pos, int light, int overlay, boolean isValidPlacement) {
        public static NaiveRenderData create(ClientLevel level, BlockPos blockPos, @NonNull BlockState blockState, boolean isValidPlacement) {
            return new NaiveRenderData(
                    blockState,
                    blockPos,
                    LevelRenderer.getLightColor(level, blockPos),
                    OverlayTexture.NO_OVERLAY,
                    isValidPlacement
            );
        }
    }

    public static void render(WorldRenderContext ctx) {
        synchronized (renderedPositions) {
            if (renderedPositions.isEmpty()) {
                return;
            }

            var gameRenderer = ctx.gameRenderer();
            var mc = gameRenderer.getMinecraft();
            var level = mc.level;

            if (level == null) {
                return;
            }

            var matrix = ctx.matrices();
            var camera = gameRenderer.getMainCamera();
            var cameraPos = camera.position();

            var blockRenderer = mc.getBlockRenderer();

            matrix.pushPose();
            matrix.translate(cameraPos.multiply(-1, -1, -1));

            if (renderedPositions.stream().allMatch(NaiveRenderData::isValidPlacement)) {
                for (var data : renderedPositions) {
                    matrix.pushPose();
                    matrix.translate(new Vec3(data.pos()));

                    var model = blockRenderer.getBlockModel(data.state());

                    ctx.commandQueue().submitBlockStateModel(
                            matrix,
                            chunkSectionLayer -> RenderTypes.translucentMovingBlock(),
                            model,
                            1f,
                            1f,
                            1f,
                            data.light(),
                            OverlayTexture.RED_OVERLAY_V,
                            0,
                            level,
                            data.pos(),
                            data.state()
                    );

                    matrix.popPose();
                }
            }

            matrix.popPose();
        }
    }
}

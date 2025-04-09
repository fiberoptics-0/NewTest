package dev.fiberoptics.newtest.block;

import com.mojang.serialization.MapCodec;
import dev.fiberoptics.newtest.block.entity.ExampleMachineBlockEntity;
import dev.fiberoptics.newtest.block.entity.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ExampleMachineBlock extends BaseEntityBlock {
    public static final MapCodec<ExampleMachineBlock> CODEC;

    public ExampleMachineBlock(ResourceLocation registryName) {
        this(Properties.of());
    }

    protected ExampleMachineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player,BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof ExampleMachineBlockEntity blockEntity) {
            player.openMenu(blockEntity,pos);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(newState.getBlock() != state.getBlock()) {
            if(level.getBlockEntity(pos) instanceof ExampleMachineBlockEntity blockEntity) {
                blockEntity.drop();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntityTypes.EXAMPLE_MACHINE_BE.get(),ExampleMachineBlockEntity::ticker);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ExampleMachineBlockEntity(blockPos, blockState);
    }

    static {
        CODEC = simpleCodec(ExampleMachineBlock::new);
    }
}

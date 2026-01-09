package com.figueiredo.everdalemod.block.custom;

import com.figueiredo.everdalemod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.Nullable;

public class CornCropBlock extends CropBlock {
    public CornCropBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(this.getAgeProperty(), 0)
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public static final int MAX_AGE = 8;
    public static final int AGE_TO_GROW_TOP = 8; // Stage at which crop will generate block above
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 8);

    private static final VoxelShape[] LOWER_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
    };
    private static final VoxelShape[] UPPER_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
    };

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return UPPER_SHAPE_BY_AGE[pState.getValue(AGE) - AGE_TO_GROW_TOP];
        } else {
            return LOWER_SHAPE_BY_AGE[pState.getValue(AGE)];
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(HALF) == DoubleBlockHalf.UPPER && pFacing == Direction.DOWN && pFacingState.getBlock() != this) {
            Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(HALF) ==  DoubleBlockHalf.UPPER) return;

        int currentAge = pState.getValue(AGE);
        int maxAge = getMaxAge();

        if (currentAge < maxAge) {
            if (pRandom.nextInt(5) == 0) { // example growth chance
                growCrops(pLevel, pPos, pState);
            }
        }
    }

    @Override
    public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {

        int currentAge =  pState.getValue(AGE);
        int newAge = currentAge + 1;
        if (newAge <= getMaxAge()) {
            if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState newBlockState =  pState.setValue(AGE, newAge);
                pLevel.setBlock(pPos, newBlockState, 2);

                // generate or updates block above if the threshold was hit
                if (newAge >= AGE_TO_GROW_TOP && pLevel.getBlockState(pPos.above(1)).is(Blocks.AIR) ||
                        pLevel.getBlockState(pPos.above(1)).getBlock() == this) {
                    pLevel.setBlock(pPos.above(1), newBlockState.setValue(HALF, DoubleBlockHalf.UPPER).setValue(AGE, newAge), 3);
                }
            } else if (pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                BlockState newBlockState =  pState.setValue(AGE, newAge);
                pLevel.setBlock(pPos, newBlockState, 2);
                pLevel.setBlock(pPos.below(1),  newBlockState.setValue(HALF, DoubleBlockHalf.LOWER).setValue(AGE, newAge), 3);
            }
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return super.mayPlaceOn(state, world, pos);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        if (pState.getValue(HALF) ==  DoubleBlockHalf.LOWER) {
            // Only check for valid soil
            BlockState soil =  pLevel.getBlockState(pPos.below(1));
            return isCorrectSoil(soil);
        } else if (pState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            // UPPER half requires bottom to survive
            BlockState below =  pLevel.getBlockState(pPos.below(1));
            return  below.getBlock() == this && below.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
        // default choice
        return false;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockPos = pContext.getClickedPos();
        Level level = pContext.getLevel();

        if (blockPos.getY() >= level.getMaxBuildHeight() - 1) return null;
        if (!isCorrectSoil(level.getBlockState(blockPos.below()))) return null;

        return defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER).setValue(AGE, 0);
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.CORN_SEEDS.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF, AGE);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return SoundType.CROP;
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (pLevel.isClientSide ) return;

        DoubleBlockHalf half = pState.getValue(HALF);
        BlockPos otherPos = half == DoubleBlockHalf.LOWER ? pPos.above(1) : pPos.below(1);
        BlockState otherState = pLevel.getBlockState(otherPos);

        if (otherState.is(this)) {
            pLevel.destroyBlock(otherPos, false);
            if (!isMature(pState)) pLevel.playSound(null, pPos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        if (!isMature(pState)) pLevel.playSound(null, pPos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    private boolean isCorrectSoil(BlockState pState) {
        return pState.is(Blocks.FARMLAND);
    }

    private boolean isMature(BlockState pState) {
        return pState.getValue(AGE) >= MAX_AGE;
    }
}

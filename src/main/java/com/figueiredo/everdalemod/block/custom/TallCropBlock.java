package com.figueiredo.everdalemod.block.custom;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.custom.util.TallCropData;
import com.figueiredo.everdalemod.datagen.util.TallCropDefaults;
import com.figueiredo.everdalemod.block.custom.util.TallCropShapeProfile;
import com.figueiredo.everdalemod.block.custom.util.TallCropShapes;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class TallCropBlock extends CropBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 100);

    private final Supplier<TallCropData> data;

    public TallCropBlock(Properties pProperties, Supplier<TallCropData> tallCropDataSupplier) {
        super(pProperties);
        this.data = tallCropDataSupplier;

        this.registerDefaultState(this.defaultBlockState()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(AGE, 0));

    }

    protected TallCropData data() {
        TallCropData data = this.data.get();
        if (data == null) {
            EverdaleMod.LOGGER.warn(
                    "TallCropData not loaded yet for block {} - using Fallback",
                    this
            );
            return TallCropDefaults.FALLBACK_DATA;
        } else {
            return data;
        }
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return data().maxAge();
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        TallCropShapeProfile shapeProfile = data().shapeProfile();

        VoxelShape[] up = TallCropShapes.UPPER.get(shapeProfile);
        VoxelShape[] down = TallCropShapes.LOWER.get(shapeProfile);

        if (up == null || down == null) {
            EverdaleMod.LOGGER.warn(
                    "Missing voxel shapes for profile {}",
                    shapeProfile
            );
            return Shapes.block();
        }

        if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return TallCropShapes.LOWER.get(shapeProfile)[pState.getValue(AGE)];
        }

        return TallCropShapes.UPPER.get(shapeProfile)[pState.getValue(AGE) - ageToGrowTop()];
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(HALF) == DoubleBlockHalf.UPPER && pFacing == Direction.DOWN && pFacingState.getBlock() != this) {
            return Blocks.AIR.defaultBlockState();
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
                if (newAge >= ageToGrowTop() && (pLevel.getBlockState(pPos.above(1)).is(Blocks.AIR) ||
                        pLevel.getBlockState(pPos.above(1)).getBlock() == this)) {
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
    protected ItemLike getBaseSeedId() {
        return ForgeRegistries.ITEMS.getValue(data().seedItem());
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
        }

        if (!isMature(pState)) pLevel.playSound(null, pPos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    private boolean isCorrectSoil(BlockState pState) {
        return pState.is(Blocks.FARMLAND);
    }

    private boolean isMature(BlockState pState) {
        return pState.getValue(AGE) >= getMaxAge();
    }

    private int ageToGrowTop() {
        return data().ageToGrowTop();
    }
}
package com.figueiredo.everdalemod.block.custom.crops;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.custom.crops.util.*;
import com.figueiredo.everdalemod.datagen.util.SimpleCropDefaults;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SimpleCropBlock extends CropBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 100);

    private final Supplier<SimpleCropData> data;

    public SimpleCropBlock(Properties pProperties, Supplier<SimpleCropData> simpleCropDataSupplier) {
        super(pProperties);
        this.data = simpleCropDataSupplier;

        this.registerDefaultState(this.defaultBlockState()
                .setValue(AGE, 0));

    }

    protected SimpleCropData data() {
        SimpleCropData data = this.data.get();
        if (data == null) {
            EverdaleMod.LOGGER.warn(
                    "SimpleCropData not loaded yet for block {} - using Fallback",
                    this
            );
            return SimpleCropDefaults.FALLBACK_DATA;
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
        SimpleCropShapeProfile shapeProfile = data().shapeProfile();

        VoxelShape[] profile = SimpleCropShapes.get(shapeProfile);

        if (profile == null) {
            EverdaleMod.LOGGER.warn(
                    "Missing voxel shapes for profile {}",
                    shapeProfile
            );
            return Shapes.block();
        }

        EverdaleMod.LOGGER.warn("age is {}", getMaxAge());
        int currentAge = pState.getValue(AGE) > getMaxAge() ? getMaxAge() : pState.getValue(AGE);
        return SimpleCropShapes.get(shapeProfile)[currentAge];
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
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
            BlockState newBlockState =  pState.setValue(AGE, newAge);
            pLevel.setBlock(pPos, newBlockState, 2);
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return super.mayPlaceOn(state, world, pos);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        // Only check for valid soil
        BlockState soil =  pLevel.getBlockState(pPos.below(1));
        return isCorrectSoil(soil);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockPos = pContext.getClickedPos();
        Level level = pContext.getLevel();

        if (blockPos.getY() >= level.getMaxBuildHeight() - 1) return null;
        if (!isCorrectSoil(level.getBlockState(blockPos.below()))) return null;

        return defaultBlockState().setValue(AGE, 0);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ForgeRegistries.ITEMS.getValue(data().seedItem());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return SoundType.CROP;
    }

    private boolean isCorrectSoil(BlockState pState) {
        return pState.is(Blocks.FARMLAND);
    }
}
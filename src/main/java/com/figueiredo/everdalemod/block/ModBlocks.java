package com.figueiredo.everdalemod.block;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.custom.CornCropBlock;
import com.figueiredo.everdalemod.block.custom.StrawberryCropBlock;
import com.figueiredo.everdalemod.block.custom.TallCropBlock;
import com.figueiredo.everdalemod.block.custom.loader.TallCropDataLoader;
import com.figueiredo.everdalemod.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, EverdaleMod.MOD_ID);

    public static final RegistryObject<Block> TIN_BLOCK =
            registerBlock("tin_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> RAW_TIN_BLOCK =
            registerBlock("raw_tin_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)));

    public static final RegistryObject<Block> TIN_ORE_BLOCK =
            registerBlock("tin_ore_block", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(1.6F)
                    .requiresCorrectToolForDrops(),
                    UniformInt.of(3, 6)));
    public static final RegistryObject<Block> DEEPSLATE_TIN_ORE_BLOCK =
            registerBlock("deepslate_tin_ore_block", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(1.8F)
                    .requiresCorrectToolForDrops(),
                    UniformInt.of(3, 6)));

    public static final RegistryObject<Block> STRAWBERRY_CROP = BLOCKS.register("strawberry_crop",
                    () -> new StrawberryCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noCollission().noOcclusion()));
    public static final RegistryObject<Block> CORN_CROP = BLOCKS.register("corn_crop",
                    () -> new TallCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noCollission().noOcclusion(),
                            () -> TallCropDataLoader.get(
                                    new ResourceLocation(EverdaleMod.MOD_ID, "corn"))));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> blockRegistry = BLOCKS.register(name, block);
        registerBlockItem(name, blockRegistry);
        return blockRegistry;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

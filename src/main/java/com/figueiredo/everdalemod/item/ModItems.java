package com.figueiredo.everdalemod.item;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.ModBlocks;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, EverdaleMod.MOD_ID);

    public static final RegistryObject<Item> TIN =
            ITEMS.register("tin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_TIN =
            ITEMS.register("raw_tin", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STRAWBERRY =
            ITEMS.register("strawberry", () -> new Item(new Item.Properties().food(ModFoods.STRAWBERRY)));
    public static final RegistryObject<Item> CORN =
            ITEMS.register("corn", () -> new Item(new Item.Properties().food(ModFoods.CORN)));

    public static final RegistryObject<Item> STRAWBERRY_SEEDS = ITEMS.register("strawberry_seeds",
            () -> new ItemNameBlockItem(ModBlocks.STRAWBERRY_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> CORN_SEEDS = ITEMS.register("corn_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CORN_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> TIN_SWORD =
            ITEMS.register("tin_sword", () -> new SwordItem(ModToolTiers.TIN, 3, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> TIN_PICKAXE =
            ITEMS.register("tin_pickaxe", () -> new PickaxeItem(ModToolTiers.TIN, 1, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> TIN_AXE =
            ITEMS.register("tin_axe", () -> new AxeItem(ModToolTiers.TIN, 6, -3.1f, new Item.Properties()));
    public static final RegistryObject<Item> TIN_SHOVEL =
            ITEMS.register("tin_shovel", () -> new ShovelItem(ModToolTiers.TIN, 1, -3, new Item.Properties()));
    public static final RegistryObject<Item> TIN_HOE =
            ITEMS.register("tin_hoe", () -> new HoeItem(ModToolTiers.TIN, -1, -2, new Item.Properties()));

    public static final RegistryObject<Item> TIN_HELMET =
            ITEMS.register("tin_helmet", () -> new ArmorItem(ModArmorMaterials.TIN, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> TIN_CHESTPLATE =
            ITEMS.register("tin_chestplate", () -> new ArmorItem(ModArmorMaterials.TIN, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> TIN_LEGGINGS =
            ITEMS.register("tin_leggings", () -> new ArmorItem(ModArmorMaterials.TIN, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> TIN_BOOTS =
            ITEMS.register("tin_boots", () -> new ArmorItem(ModArmorMaterials.TIN, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

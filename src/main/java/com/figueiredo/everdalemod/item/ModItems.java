package com.figueiredo.everdalemod.item;

import com.figueiredo.everdalemod.EverdaleMod;
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

    public static final RegistryObject<Item> TIN_SWORD =
            ITEMS.register("tin_sword", () -> new SwordItem(ModToolTiers.TIN, 3, 0.5f, new Item.Properties()));
    public static final RegistryObject<Item> TIN_PICKAXE =
            ITEMS.register("tin_pickaxe", () -> new PickaxeItem(ModToolTiers.TIN, 0, 0, new Item.Properties()));
    public static final RegistryObject<Item> TIN_AXE =
            ITEMS.register("tin_axe", () -> new AxeItem(ModToolTiers.TIN, 0, 0, new Item.Properties()));
    public static final RegistryObject<Item> TIN_SHOVEL =
            ITEMS.register("tin_shovel", () -> new ShovelItem(ModToolTiers.TIN, 0, 0, new Item.Properties()));
    public static final RegistryObject<Item> TIN_HOE =
            ITEMS.register("tin_hoe", () -> new HoeItem(ModToolTiers.TIN, 0, 0, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

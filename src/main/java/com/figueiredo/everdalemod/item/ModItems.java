package com.figueiredo.everdalemod.item;

import com.figueiredo.everdalemod.EverdaleMod;
import net.minecraft.world.item.Item;
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

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

package com.figueiredo.everdalemod.item;

import com.figueiredo.everdalemod.EverdaleMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EverdaleMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> EVERDALE_TAB =
        CREATIVE_MODE_TABS.register("everdale_tab", () -> CreativeModeTab.builder()
                .icon(() -> new ItemStack(ModItems.TIN.get()))
                .title(Component.translatable("everdale_tab.title"))
                .displayItems((pParameters, pOutput) -> {
                    pOutput.accept(ModItems.TIN.get());
                    pOutput.accept(ModItems.RAW_TIN.get());
                })
                .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

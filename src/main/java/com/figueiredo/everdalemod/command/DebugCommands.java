package com.figueiredo.everdalemod.command;

import com.figueiredo.everdalemod.EverdaleMod;
import com.figueiredo.everdalemod.block.custom.crops.ChunkData;
import com.figueiredo.everdalemod.block.custom.crops.util.SoilContentInformation;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EverdaleMod.MOD_ID)
public class DebugCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("checksoil")
                        .executes(context -> {
                            // Directly get the player
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            ServerLevel level = player.serverLevel();

                            // Ray trace the block the player is looking at (max 5 blocks away)
                            HitResult hit = player.pick(5.0, 0.0f, false);
                            if (hit.getType() != HitResult.Type.BLOCK) {
                                player.sendSystemMessage(Component.literal("You are not looking at a block."));
                                return 1;
                            }

                            BlockPos pos = ((BlockHitResult) hit).getBlockPos();
                            ChunkData data = ChunkData.get(level, new ChunkPos(pos));
                            SoilContentInformation soil = data.get(pos);

                            if (soil == null) {
                                player.sendSystemMessage(Component.literal(
                                        "Soil at " + pos + "does not contain information yet"
                                ));
                            } else {
                                player.sendSystemMessage(Component.literal(
                                        "Soil at " + pos + ": N=" + soil.nitrogen() +
                                                " P=" + soil.phosphorus() +
                                                " K=" + soil.potassium()
                                ));
                            }



                            return 1; // command success
                        })
        );
    }
}

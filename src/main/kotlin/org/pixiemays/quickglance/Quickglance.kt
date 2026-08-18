package org.pixiemays.quickglance

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import org.pixiemays.quickglance.network.FurnaceSyncPayload
import java.util.HashMap
import java.util.UUID

object Quickglance : ModInitializer {
    private val wasActive: MutableMap<UUID, Boolean> = HashMap()

    override fun onInitialize() {
        PayloadTypeRegistry.clientboundPlay().register(FurnaceSyncPayload.TYPE, FurnaceSyncPayload.CODEC)

        ServerTickEvents.END_SERVER_TICK.register { server ->
            for (player in server.playerList.players) {
                val hit = getTargetBlockServer(player, 5.0)
                val blockEntity = if (hit != null) player.level().getBlockEntity(hit.blockPos) else null

                if (blockEntity is AbstractFurnaceBlockEntity) {
                    wasActive.put(player.uuid, true)
                    ServerPlayNetworking.send(
                        player,
                        FurnaceSyncPayload(
                            true,
                            hit!!.blockPos,
                            blockEntity.getItem(0),
                            blockEntity.getItem(1),
                            blockEntity.getItem(2)
                        )
                    )
                } else if (wasActive.getOrDefault(player.uuid, false)) {
                    wasActive.put(player.uuid, false)
                    ServerPlayNetworking.send(player, FurnaceSyncPayload.EMPTY)
                }
            }
        }
    }

    private fun getTargetBlockServer(player: ServerPlayer, reachDistance: Double): BlockHitResult? {
        val eyePos = player.getEyePosition(1.0f)
        val lookVec = player.getViewVector(1.0f)
        val endPos = eyePos.add(lookVec.x * reachDistance, lookVec.y * reachDistance, lookVec.z * reachDistance)

        val clipContext = ClipContext(
            eyePos, endPos,
            ClipContext.Block.OUTLINE,
            ClipContext.Fluid.NONE,
            player
        )

        val result = player.level().clip(clipContext)
        return if (result.type == HitResult.Type.BLOCK) result else null
    }
}
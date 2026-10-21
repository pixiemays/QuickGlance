package org.pixiemays.quickglance

//? if fabric {
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
//? if >=26.1 {
/*import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
*///? } elif >=1.20.5 {
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
//? } else {
/*import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs*/
//? }
//? }

//? if neoforge {
/*import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.tick.ServerTickEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
*///? }

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.CampfireBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import org.pixiemays.quickglance.compat.QuickglanceTags
import org.pixiemays.quickglance.network.FurnaceSyncPayload
import java.util.HashMap
import java.util.UUID

//? if fabric {
object Quickglance : ModInitializer {
    override fun onInitialize() {
        //? if >=26.1 {
        /*PayloadTypeRegistry.clientboundPlay().register(FurnaceSyncPayload.TYPE, FurnaceSyncPayload.CODEC)
        *///? } elif >=1.20.5 {
        PayloadTypeRegistry.playS2C().register(FurnaceSyncPayload.TYPE, FurnaceSyncPayload.CODEC)
        //? }

        ServerTickEvents.END_SERVER_TICK.register { server ->
            QuickglanceCommon.tick(server)
        }
    }
}
//? }

//? if neoforge {
/*@Mod("quickglance")
class QuickglanceNeoForge {
    init {
        NeoForge.EVENT_BUS.addListener(::onServerTick)
    }

    private fun onServerTick(event: ServerTickEvent.Post) {
        QuickglanceCommon.tick(event.server)
    }
}

@EventBusSubscriber(modid = "quickglance", value = [Dist.DEDICATED_SERVER])
object QuickglanceNeoForgeServer {
    @JvmStatic
    @SubscribeEvent
    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar("1")
        registrar.playToClient(
            FurnaceSyncPayload.TYPE,
            FurnaceSyncPayload.CODEC
        ) { _, _ -> }
    }
}
*///? }

object QuickglanceCommon {
    private val wasActive: MutableMap<UUID, Boolean> = HashMap()

    fun tick(server: net.minecraft.server.MinecraftServer) {
        for (player in server.playerList.players) {
            val hit = getTargetBlockServer(player, 5.0)
            val pos = hit?.blockPos
            val level = player.level()
            val state = if (pos != null) level.getBlockState(pos) else null
            val blockEntity = if (pos != null) level.getBlockEntity(pos) else null

            val containerInfo = if (hit != null && state != null) extractContainerInfo(blockEntity, state) else null

            if (containerInfo != null && hit != null) {
                wasActive[player.uuid] = true
                sendPayload(
                    player,
                    FurnaceSyncPayload(
                        true,
                        hit.blockPos,
                        containerInfo.first,
                        containerInfo.second
                    )
                )
            } else if (wasActive.getOrDefault(player.uuid, false)) {
                wasActive[player.uuid] = false
                sendPayload(player, FurnaceSyncPayload.EMPTY)
            }
        }
    }

    fun extractContainerInfo(blockEntity: BlockEntity?, state: BlockState): Pair<String, List<ItemStack>>? {
        if (state.`is`(QuickglanceTags.FURNACE_LIKE) || blockEntity is AbstractFurnaceBlockEntity) {
            if (blockEntity is Container) {
                val items = listOf(
                    if (blockEntity.containerSize > 0) blockEntity.getItem(0) else ItemStack.EMPTY,
                    if (blockEntity.containerSize > 1) blockEntity.getItem(1) else ItemStack.EMPTY,
                    if (blockEntity.containerSize > 2) blockEntity.getItem(2) else ItemStack.EMPTY
                )
                return Pair("furnace", items)
            }
        }

        if (state.`is`(QuickglanceTags.CAMPFIRE_LIKE) || blockEntity is CampfireBlockEntity) {
            if (blockEntity is CampfireBlockEntity) {
                return Pair("campfire", blockEntity.items.toList())
            } else if (blockEntity is Container) {
                val count = minOf(blockEntity.containerSize, 4)
                val items = (0 until count).map { blockEntity.getItem(it) }
                return Pair("campfire", items)
            }
        }

        if (state.`is`(QuickglanceTags.ROW_LIKE)) {
            if (blockEntity is Container) {
                val size = blockEntity.containerSize
                val items = if (size <= 9) {
                    (0 until size).map { blockEntity.getItem(it) }
                } else {
                    (0 until size).map { blockEntity.getItem(it) }.filter { !it.isEmpty }
                }
                return Pair("row", items)
            }
        }

        return null
    }

    fun sendPayload(player: ServerPlayer, payload: FurnaceSyncPayload) {
        //? if fabric {
            //? if >=1.21 {
            ServerPlayNetworking.send(player, payload)
            //? } else {
            /*val buf = PacketByteBufs.create()
            payload.write(buf)
            ServerPlayNetworking.send(player, FurnaceSyncPayload.ID, buf)*/
            //? }
        //? } elif neoforge {
        /*PacketDistributor.sendToPlayer(player, payload)
        *///? }
    }

    fun getTargetBlockServer(player: ServerPlayer, reachDistance: Double): BlockHitResult? {
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
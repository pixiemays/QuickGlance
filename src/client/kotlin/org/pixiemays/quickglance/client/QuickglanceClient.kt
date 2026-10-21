package org.pixiemays.quickglance.client

import eu.midnightdust.lib.config.MidnightConfig
//? if fabric {
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
//? if >=26.1 {
/*import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.minecraft.resources.ResourceLocation
*///? } elif >=1.21 {
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.resources.ResourceLocation as ResourceLocation
//? } else {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.resources.ResourceLocation as ResourceLocation*/
//? }
//? }
//? if neoforge {
/*import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.minecraft.resources.ResourceLocation
*///? }
import org.pixiemays.quickglance.network.FurnaceSyncPayload

//? if fabric {
class QuickglanceClient : ClientModInitializer {

    override fun onInitializeClient() {
        //? if >=1.21 {
        ClientPlayNetworking.registerGlobalReceiver(FurnaceSyncPayload.TYPE) { payload, _ ->
            FurnaceClientCache.update(payload)
        }
        //? } else {
        /*ClientPlayNetworking.registerGlobalReceiver(FurnaceSyncPayload.ID) { _, _, buf, _ ->
            FurnaceClientCache.update(FurnaceSyncPayload.read(buf))
        }*/
        //? }
        MidnightConfig.init("quickglance", QuickglanceConfig::class.java)

        //? if >=26.1 {
        /*HudElementRegistry.addLast(
            ResourceLocation.fromNamespaceAndPath("quickglance", "furnace_hud"),
            QuickglanceHud.hudLayer()
        )
        *///? } else {
        HudRenderCallback.EVENT.register { drawContext, _ ->
            QuickglanceHud.render(drawContext)
        }
        //? }
    }
}
//? }

//? if neoforge {
/*@EventBusSubscriber(modid = "quickglance", value = [Dist.CLIENT])
object QuickglanceNeoForgeClient {
    init {
        MidnightConfig.init("quickglance", QuickglanceConfig::class.java)
    }

    @JvmStatic
    @SubscribeEvent
    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar("1")
        registrar.playToClient(
            FurnaceSyncPayload.TYPE,
            FurnaceSyncPayload.CODEC
        ) { payload, _ ->
            FurnaceClientCache.update(payload)
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun registerLayers(event: RegisterGuiLayersEvent) {
        event.registerAbove(
            VanillaGuiLayers.HOTBAR,
            ResourceLocation.fromNamespaceAndPath("quickglance", "furnace_hud")
        ) { graphics, _ ->
            QuickglanceHud.render(graphics)
        }
    }
}
*///? }
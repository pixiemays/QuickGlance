package org.pixiemays.quickglance.client

import eu.midnightdust.lib.config.MidnightConfig
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.minecraft.resources.Identifier
import org.pixiemays.quickglance.network.FurnaceSyncPayload

class QuickglanceClient : ClientModInitializer {

    override fun onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(FurnaceSyncPayload.TYPE) { payload, _ ->
            FurnaceClientCache.update(payload)
        }
        MidnightConfig.init("quickglance", QuickglanceConfig::class.java)
        HudElementRegistry.addLast(
            Identifier.fromNamespaceAndPath("quickglance", "furnace_hud"),
            QuickglanceHud.hudLayer()
        )
    }
}
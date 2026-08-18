package org.pixiemays.quickglance.client

import org.pixiemays.quickglance.network.FurnaceSyncPayload

object FurnaceClientCache {
    var lastData: FurnaceSyncPayload? = null
        private set

    fun update(payload: FurnaceSyncPayload) {
        if (payload.active) {
            lastData = payload
        }
    }
}
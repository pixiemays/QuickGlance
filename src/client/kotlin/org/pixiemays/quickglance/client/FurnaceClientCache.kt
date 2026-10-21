package org.pixiemays.quickglance.client

import org.pixiemays.quickglance.network.FurnaceSyncPayload

object FurnaceClientCache {
    var lastData: FurnaceSyncPayload? = null
        private set
    var isServerActive: Boolean = false
        private set

    fun update(payload: FurnaceSyncPayload) {
        isServerActive = payload.active
        if (payload.active) {
            lastData = payload
        }
    }
}
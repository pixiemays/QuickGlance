package org.pixiemays.quickglance.client

import eu.midnightdust.lib.config.MidnightConfig

class QuickglanceConfig : MidnightConfig() {
    companion object {
        @Entry(category = "animation")
        @JvmField
        var animDurationMs: Int = 150

        @Entry(category = "animation")
        @JvmField
        var gracePeriodMs: Int = 100

        @Entry(category = "position")
        @JvmField
        var offsetX: Int = 14

        @Entry(category = "position")
        @JvmField
        var offsetY: Int = 22
    }
}
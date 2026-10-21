//? if fabric {
package org.pixiemays.quickglance.client

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import eu.midnightdust.lib.config.MidnightConfig

class QuickglanceModMenu: ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            MidnightConfig.getScreen(parent, "quickglance")
        }
    }
}
//? }
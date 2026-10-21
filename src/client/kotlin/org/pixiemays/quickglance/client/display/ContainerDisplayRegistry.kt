package org.pixiemays.quickglance.client.display

object ContainerDisplayRegistry {
    private val displays = mutableMapOf<String, ContainerDisplay>()
    val DEFAULT: ContainerDisplay = RowContainerDisplay()

    init {
        register("furnace", FurnaceContainerDisplay())
        register("campfire", CampfireContainerDisplay())
        register("row", RowContainerDisplay())
        register("grid", RowContainerDisplay())
    }

    fun register(type: String, display: ContainerDisplay) {
        displays[type.lowercase()] = display
    }

    fun get(type: String): ContainerDisplay {
        return displays[type.lowercase()] ?: DEFAULT
    }
}

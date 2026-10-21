package org.pixiemays.quickglance.client.display

import net.minecraft.world.item.ItemStack

data class DisplayDimensions(val width: Int, val height: Int)

interface ContainerDisplay {
    fun getDimensions(items: List<ItemStack>): DisplayDimensions
    fun renderSlots(
        items: List<ItemStack>,
        startX: Int,
        startY: Int,
        drawSlot: (ItemStack, Int, Int) -> Unit
    )
}

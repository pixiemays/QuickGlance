package org.pixiemays.quickglance.client.display

import net.minecraft.world.item.ItemStack

class CampfireContainerDisplay : ContainerDisplay {
    override fun getDimensions(items: List<ItemStack>): DisplayDimensions {
        return DisplayDimensions(44, 44)
    }

    override fun renderSlots(
        items: List<ItemStack>,
        startX: Int,
        startY: Int,
        drawSlot: (ItemStack, Int, Int) -> Unit
    ) {
        val offsets = arrayOf(
            Pair(4, 4),
            Pair(22, 4),
            Pair(4, 22),
            Pair(22, 22)
        )
        for (i in 0 until minOf(items.size, offsets.size)) {
            val (dx, dy) = offsets[i]
            drawSlot(items[i], startX + dx, startY + dy)
        }
    }
}

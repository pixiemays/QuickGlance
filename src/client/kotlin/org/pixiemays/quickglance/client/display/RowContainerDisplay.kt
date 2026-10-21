package org.pixiemays.quickglance.client.display

import net.minecraft.world.item.ItemStack
import kotlin.math.max

class RowContainerDisplay : ContainerDisplay {
    override fun getDimensions(items: List<ItemStack>): DisplayDimensions {
        val count = max(items.size, 1)
        val cols = if (count > 9) 9 else count
        val rows = (count + cols - 1) / cols
        val width = 8 + cols * 18
        val height = 8 + rows * 18
        return DisplayDimensions(width, height)
    }

    override fun renderSlots(
        items: List<ItemStack>,
        startX: Int,
        startY: Int,
        drawSlot: (ItemStack, Int, Int) -> Unit
    ) {
        val count = max(items.size, 1)
        val cols = if (count > 9) 9 else count

        for ((index, item) in items.withIndex()) {
            val col = index % cols
            val row = index / cols
            val x = startX + 4 + col * 18
            val y = startY + 4 + row * 18
            drawSlot(item, x, y)
        }
    }
}

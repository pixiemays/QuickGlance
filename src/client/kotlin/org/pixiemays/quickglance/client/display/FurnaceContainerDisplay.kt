package org.pixiemays.quickglance.client.display

import net.minecraft.world.item.ItemStack

class FurnaceContainerDisplay : ContainerDisplay {
    override fun getDimensions(items: List<ItemStack>): DisplayDimensions {
        return DisplayDimensions(64, 44)
    }

    override fun renderSlots(
        items: List<ItemStack>,
        startX: Int,
        startY: Int,
        drawSlot: (ItemStack, Int, Int) -> Unit
    ) {
        if (items.isNotEmpty()) {
            drawSlot(items[0], startX + 4, startY + 4)
        }
        if (items.size > 1) {
            drawSlot(items[1], startX + 4, startY + 24)
        }
        if (items.size > 2) {
            drawSlot(items[2], startX + 40, startY + 14)
        }
    }
}

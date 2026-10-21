package org.pixiemays.quickglance.client

//? if fabric && >=26.1 {
/*import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement
*///? }
//? if >=26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor
*///? } else {
import net.minecraft.client.gui.GuiGraphics
//? }

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import org.pixiemays.quickglance.client.display.ContainerDisplayRegistry
import org.pixiemays.quickglance.compat.QuickglanceTags

object QuickglanceHud {
    private var isLooking = false
    private var lastPos: BlockPos? = null
    private var appearedAt = 0L
    private var disappearedAt = 0L
    private var lastSeenTime = 0L

    //? if fabric && >=26.1 {
    /*fun hudLayer(): HudElement {
        return HudElement { graphics: GuiGraphicsExtractor, _ ->
            render(graphics)
        }
    }
    *///? }

    //? if >=26.1 {
    /*fun render(graphics: GuiGraphicsExtractor) {
    *///? } else {
    fun render(graphics: GuiGraphics) {
    //? }
        val animDurationMs = QuickglanceConfig.animDurationMs.toFloat()
        val gracePeriodMs = QuickglanceConfig.gracePeriodMs.toLong()
        val client = Minecraft.getInstance()

        val hit = client.hitResult
        var currentContainerPos: BlockPos? = null

        if (hit != null && hit.type == HitResult.Type.BLOCK) {
            val pos = (hit as BlockHitResult).blockPos
            val level = client.level
            val state = level?.getBlockState(pos)
            val be = level?.getBlockEntity(pos)
            val isCompatibleBlock = be is AbstractFurnaceBlockEntity
                || (state != null && (state.`is`(QuickglanceTags.FURNACE_LIKE) || state.`is`(QuickglanceTags.CAMPFIRE_LIKE) || state.`is`(QuickglanceTags.ROW_LIKE)))
                || (FurnaceClientCache.isServerActive && FurnaceClientCache.lastData?.pos == pos)

            if (isCompatibleBlock) {
                currentContainerPos = pos
            }
        }

        val now = System.currentTimeMillis()

        if (currentContainerPos != null) {
            lastSeenTime = now
            lastPos = currentContainerPos

            if (!isLooking) {
                isLooking = true
                appearedAt = now
            }
        } else {
            if (isLooking && (now - lastSeenTime) > gracePeriodMs) {
                isLooking = false
                disappearedAt = now
            }
        }

        if (!isLooking && (now - disappearedAt) >= animDurationMs) {
            return
        }

        val data = FurnaceClientCache.lastData
        if (data == null || data.items.all { it.isEmpty }) {
            return
        }

        var progress = 0f
        if (isLooking) {
            val elapsed = (now - appearedAt).toFloat()
            val rawProgress = Mth.clamp(elapsed / animDurationMs, 0f, 1f)
            val inv = 1f - rawProgress
            progress = 1f - (inv * inv * inv)
        } else {
            val elapsed = (now - disappearedAt).toFloat()
            val rawProgress = Mth.clamp(elapsed / animDurationMs, 0f, 1f)
            val inv = 1f - rawProgress
            progress = (inv * inv * inv)
        }

        val display = ContainerDisplayRegistry.get(data.displayType)
        val dimensions = display.getDimensions(data.items)
        val panelW = dimensions.width
        val panelH = dimensions.height

        val window = client.window
        val centerX = window.guiScaledWidth / 2
        val centerY = window.guiScaledHeight / 2

        val startX = centerX + QuickglanceConfig.offsetX
        val startY = centerY - QuickglanceConfig.offsetY

        val baseAlpha = 0x90
        val animAlpha = Mth.clamp((baseAlpha * progress).toInt(), 0, 255)
        val fillColor = (animAlpha shl 24) or 0x000000

        val scale = Mth.lerp(progress, 0.0f, 1.0f)

        val pivotX = startX + panelW / 2f
        val pivotY = startY + panelH / 2f

        val matrices = graphics.pose()

        //? if >=26.1 {
        /*matrices.pushMatrix()
        matrices.translate(pivotX, pivotY)
        matrices.scale(scale, scale)
        matrices.translate(-pivotX, -pivotY)
        *///? } else {
        matrices.pushPose()
        matrices.translate(pivotX, pivotY, 0f)
        matrices.scale(scale, scale, 1f)
        matrices.translate(-pivotX, -pivotY, 0f)
        //? }

        graphics.fill(startX, startY, startX + panelW, startY + panelH, fillColor)

        fun drawSlot(stack: ItemStack, x: Int, y: Int) {
            if (stack.isEmpty) return
            //? if >=26.1 {
            /*graphics.item(stack, x, y)
            graphics.itemDecorations(client.font, stack, x, y)
            *///? } else {
            graphics.renderItem(stack, x, y)
            graphics.renderItemDecorations(client.font, stack, x, y)
            //? }
        }

        display.renderSlots(data.items, startX, startY, ::drawSlot)

        //? if >=26.1 {
        /*matrices.popMatrix()
        *///? } else {
        matrices.popPose()
        //? }
    }
}
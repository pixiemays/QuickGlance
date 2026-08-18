package org.pixiemays.quickglance.client

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

object QuickglanceHud {
    private var isLooking = false
    private var lastPos: BlockPos? = null
    private var appearedAt = 0L
    private var disappearedAt = 0L
    private var lastSeenFurnaceTime = 0L

    fun hudLayer(): HudElement {
        return HudElement { graphics: GuiGraphicsExtractor, _ ->
            val animDurationMs = QuickglanceConfig.animDurationMs.toFloat()
            val gracePeriodMs = QuickglanceConfig.gracePeriodMs.toLong()
            val client = Minecraft.getInstance()

            val hit = client.hitResult
            var currentFurnacePos: BlockPos? = null

            if (hit != null && hit.type == HitResult.Type.BLOCK) {
                val pos = (hit as BlockHitResult).blockPos
                val be = client.level?.getBlockEntity(pos)
                if (be is AbstractFurnaceBlockEntity) {
                    currentFurnacePos = pos
                }
            }

            val now = System.currentTimeMillis()

            if (currentFurnacePos != null) {
                lastSeenFurnaceTime = now
                lastPos = currentFurnacePos

                if (!isLooking) {
                    isLooking = true
                    appearedAt = now
                }
            } else {
                if (isLooking && (now - lastSeenFurnaceTime) > gracePeriodMs) {
                    isLooking = false
                    disappearedAt = now
                }
            }

            if (!isLooking && (now - disappearedAt) >= animDurationMs) {
                return@HudElement
            }

            val data = FurnaceClientCache.lastData
            if (data == null || (data.input.isEmpty && data.fuel.isEmpty && data.result.isEmpty)) {
                return@HudElement
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

            val window = client.window
            val centerX = window.guiScaledWidth / 2
            val centerY = window.guiScaledHeight / 2

            val startX = centerX + QuickglanceConfig.offsetX
            val startY = centerY - QuickglanceConfig.offsetY
            val panelW = 64
            val panelH = 44

            val baseAlpha = 0x90
            val animAlpha = Mth.clamp((baseAlpha * progress).toInt(), 0, 255)
            val fillColor = (animAlpha shl 24) or 0x000000

            val scale = Mth.lerp(progress, 0.0f, 1.0f)

            val pivotX = startX + panelW / 2f
            val pivotY = startY + panelH / 2f

            val matrices = graphics.pose()

            matrices.pushMatrix()
            matrices.translate(pivotX, pivotY)
            matrices.scale(scale, scale)
            matrices.translate(-pivotX, -pivotY)

            graphics.fill(startX, startY, startX + panelW, startY + panelH, fillColor)

            fun drawSlot(stack: ItemStack, x: Int, y: Int) {
                if (stack.isEmpty) return
                graphics.item(stack, x, y)
                graphics.itemDecorations(client.font, stack, x, y)
            }

            drawSlot(data.input, startX + 4, startY + 4)
            drawSlot(data.fuel, startX + 4, startY + 24)
            drawSlot(data.result, startX + 40, startY + 14)

            matrices.popMatrix()
        }
    }
}
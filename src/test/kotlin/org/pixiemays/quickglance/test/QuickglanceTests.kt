package org.pixiemays.quickglance.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.max

class QuickglanceTests {

    @Test
    fun testAnimationEasingCurve() {
        fun easeOutCubic(t: Float): Float {
            val inv = 1f - t.coerceIn(0f, 1f)
            return 1f - (inv * inv * inv)
        }

        fun easeInCubic(t: Float): Float {
            val inv = 1f - t.coerceIn(0f, 1f)
            return inv * inv * inv
        }

        assertEquals(0.0f, easeOutCubic(0.0f), 0.0001f)
        assertEquals(0.875f, easeOutCubic(0.5f), 0.0001f)
        assertEquals(1.0f, easeOutCubic(1.0f), 0.0001f)

        assertEquals(1.0f, easeInCubic(0.0f), 0.0001f)
        assertEquals(0.125f, easeInCubic(0.5f), 0.0001f)
        assertEquals(0.0f, easeInCubic(1.0f), 0.0001f)
    }

    @Test
    fun testAlphaMaskCalculation() {
        val baseAlpha = 0x90 // 144 in decimal
        fun calculateFillColor(progress: Float): Int {
            val clampedProgress = progress.coerceIn(0f, 1f)
            val animAlpha = (baseAlpha * clampedProgress).toInt().coerceIn(0, 255)
            return (animAlpha shl 24) or 0x000000
        }

        val zeroAlpha = calculateFillColor(0.0f)
        assertEquals(0, zeroAlpha ushr 24)

        val fullAlpha = calculateFillColor(1.0f)
        assertEquals(0x90, fullAlpha ushr 24)

        val halfAlpha = calculateFillColor(0.5f)
        assertEquals(72, halfAlpha ushr 24)
    }

    @Test
    fun testReachVectorComputation() {
        val reachDistance = 5.0
        val eyeX = 10.0
        val eyeY = 64.0
        val eyeZ = 10.0
        val lookX = 0.0
        val lookY = 0.0
        val lookZ = 1.0

        val endX = eyeX + lookX * reachDistance
        val endY = eyeY + lookY * reachDistance
        val endZ = eyeZ + lookZ * reachDistance

        assertEquals(10.0, endX, 0.0001)
        assertEquals(64.0, endY, 0.0001)
        assertEquals(15.0, endZ, 0.0001)
    }

    @Test
    fun testHudDisplayPanelLayout() {
        val windowWidth = 854
        val windowHeight = 480
        val centerX = windowWidth / 2
        val centerY = windowHeight / 2
        val offsetX = 14
        val offsetY = 22

        val startX = centerX + offsetX
        val startY = centerY - offsetY

        assertEquals(441, startX)
        assertEquals(218, startY)
    }

    @Test
    fun testRowContainerDisplayDimensions() {
        fun calculateRowDimensions(itemCount: Int): Pair<Int, Int> {
            val count = max(itemCount, 1)
            val cols = if (count > 9) 9 else count
            val rows = (count + cols - 1) / cols
            val width = 8 + cols * 18
            val height = 8 + rows * 18
            return Pair(width, height)
        }

        // Single item
        assertEquals(Pair(26, 26), calculateRowDimensions(1))
        // 5 items (hopper)
        assertEquals(Pair(98, 26), calculateRowDimensions(5))
        // 9 items (single full row)
        assertEquals(Pair(170, 26), calculateRowDimensions(9))
        // 27 items (chest: 9x3)
        assertEquals(Pair(170, 62), calculateRowDimensions(27))
    }

    @Test
    fun testCampfireLayoutOffsets() {
        val offsets = arrayOf(
            Pair(4, 4),
            Pair(22, 4),
            Pair(4, 22),
            Pair(22, 22)
        )
        assertEquals(4, offsets.size)
        assertEquals(Pair(4, 4), offsets[0])
        assertEquals(Pair(22, 4), offsets[1])
        assertEquals(Pair(4, 22), offsets[2])
        assertEquals(Pair(22, 22), offsets[3])
    }
}

package org.pixiemays.quickglance.compat

import net.minecraft.core.registries.Registries
//? if >=26.1 {
/*import net.minecraft.resources.ResourceLocation
*///? } else {
import net.minecraft.resources.ResourceLocation as ResourceLocation
//? }
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object QuickglanceTags {
    //? if >=1.21 {
    /*val FURNACE_LIKE: TagKey<Block> = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("quickglance", "furnace_like"))
    val CAMPFIRE_LIKE: TagKey<Block> = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("quickglance", "campfire_like"))
    val ROW_LIKE: TagKey<Block> = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("quickglance", "row_like"))
    *///? } else {
    val FURNACE_LIKE: TagKey<Block> = TagKey.create(Registries.BLOCK, ResourceLocation("quickglance", "furnace_like"))
    val CAMPFIRE_LIKE: TagKey<Block> = TagKey.create(Registries.BLOCK, ResourceLocation("quickglance", "campfire_like"))
    val ROW_LIKE: TagKey<Block> = TagKey.create(Registries.BLOCK, ResourceLocation("quickglance", "row_like"))
    //? }
}

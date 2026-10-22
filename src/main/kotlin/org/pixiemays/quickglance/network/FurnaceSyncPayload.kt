package org.pixiemays.quickglance.network

import net.minecraft.core.BlockPos
//? if >=1.21 {
/*import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
*///? } else {
import net.minecraft.network.FriendlyByteBuf
//? }

//? if >=26.1 {
/*import net.minecraft.resources.ResourceLocation
*///? } else {
import net.minecraft.resources.ResourceLocation as ResourceLocation
//? }

import net.minecraft.world.item.ItemStack

data class FurnaceSyncPayload(
    val active: Boolean,
    val pos: BlockPos,
    val displayType: String,
    val items: List<ItemStack>
) //? if >=1.21 {
/*: CustomPacketPayload
*///? }
{
    // Convenience constructor for classic furnace slots (backwards compatibility)
    constructor(active: Boolean, pos: BlockPos, input: ItemStack, fuel: ItemStack, result: ItemStack) :
        this(active, pos, "furnace", listOf(input, fuel, result))

    val input: ItemStack get() = items.getOrElse(0) { ItemStack.EMPTY }
    val fuel: ItemStack get() = items.getOrElse(1) { ItemStack.EMPTY }
    val result: ItemStack get() = items.getOrElse(2) { ItemStack.EMPTY }

//? if >=1.21 {
    /*override fun type(): CustomPacketPayload.Type<FurnaceSyncPayload> = TYPE

    companion object {
        val ID: ResourceLocation = ResourceLocation.fromNamespaceAndPath("quickglance", "container_sync")
        val TYPE: CustomPacketPayload.Type<FurnaceSyncPayload> = CustomPacketPayload.Type(ID)

        val EMPTY: FurnaceSyncPayload = FurnaceSyncPayload(
            false, BlockPos.ZERO, "furnace", emptyList()
        )

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, FurnaceSyncPayload> =
            StreamCodec.composite(
                ByteBufCodecs.BOOL, FurnaceSyncPayload::active,
                BlockPos.STREAM_CODEC, FurnaceSyncPayload::pos,
                ByteBufCodecs.STRING_UTF8, FurnaceSyncPayload::displayType,
                ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list(36)), FurnaceSyncPayload::items,
                ::FurnaceSyncPayload
            )
    }
*///? } else {
    fun write(buf: FriendlyByteBuf) {
        buf.writeBoolean(active)
        buf.writeBlockPos(pos)
        buf.writeUtf(displayType)
        buf.writeVarInt(items.size)
        for (item in items) {
            buf.writeItem(item)
        }
    }

    companion object {
        val ID: ResourceLocation = ResourceLocation("quickglance", "container_sync")
        val EMPTY: FurnaceSyncPayload = FurnaceSyncPayload(
            false, BlockPos.ZERO, "furnace", emptyList()
        )

        fun read(buf: FriendlyByteBuf): FurnaceSyncPayload {
            val active = buf.readBoolean()
            val pos = buf.readBlockPos()
            val displayType = buf.readUtf()
            val size = buf.readVarInt()
            val items = ArrayList<ItemStack>(size)
            for (i in 0 until size) {
                items.add(buf.readItem())
            }
            return FurnaceSyncPayload(active, pos, displayType, items)
        }
    }
//? }
}
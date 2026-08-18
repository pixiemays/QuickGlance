package org.pixiemays.quickglance.network

import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

data class FurnaceSyncPayload(
    val active: Boolean,
    val pos: BlockPos,
    val input: ItemStack,
    val fuel: ItemStack,
    val result: ItemStack
) : CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<FurnaceSyncPayload> = TYPE

    companion object {
        val ID: Identifier = Identifier.fromNamespaceAndPath("quickglance", "furnace_sync")
        val TYPE: CustomPacketPayload.Type<FurnaceSyncPayload> = CustomPacketPayload.Type(ID)

        val EMPTY: FurnaceSyncPayload = FurnaceSyncPayload(
            false, BlockPos.ZERO, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY
        )

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, FurnaceSyncPayload> =
            StreamCodec.composite(
                ByteBufCodecs.BOOL, FurnaceSyncPayload::active,
                BlockPos.STREAM_CODEC, FurnaceSyncPayload::pos,
                ItemStack.OPTIONAL_STREAM_CODEC, FurnaceSyncPayload::input,
                ItemStack.OPTIONAL_STREAM_CODEC, FurnaceSyncPayload::fuel,
                ItemStack.OPTIONAL_STREAM_CODEC, FurnaceSyncPayload::result,
                ::FurnaceSyncPayload
            )
    }
}
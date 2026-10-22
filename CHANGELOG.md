# Changelog

All notable changes to this project will be documented in this file.

## [1.1.0] - 2026-10-19

### Added
- **Multi-version support**: Minecraft 26.3, 1.21.1, and 1.20.1 managed through Stonecutter (`dev.kikugie.stonecutter`).
- **NeoForge loader**: Native support for NeoForge on 26.3 and 1.21.1 in addition to Fabric.
- **Datapack block tags**:
  - `#quickglance:furnace_like`: 3-slot furnace layout (furnace, blast furnace, smoker).
  - `#quickglance:campfire_like`: 4-slot 2x2 layout (campfire, soul campfire).
  - `#quickglance:row_like`: dynamic adaptive row/grid layout (brewing stand, hopper, dispenser, dropper, etc.).
- **Decoupled display architecture**: `ContainerDisplay` interface and `ContainerDisplayRegistry` separating server payload sync from client rendering.
- **Display renderers**:
  - `FurnaceContainerDisplay`: classic 64x44 3-slot layout.
  - `CampfireContainerDisplay`: compact 44x44 2x2 layout.
  - `RowContainerDisplay`: dynamic width/height supporting single rows (1–9 slots) and wrapped grids (>9 slots).
- **Configuration UI**: MidnightLib integration with ModMenu (Fabric) and Mods menu (NeoForge).
- **Datapack documentation**: [DATAPACKS.md](https://github.com/pixiemays/QuickGlance/blob/master/DATAPACKS.md) in English and Russian.

### Changed
- Network payload modernized to `FurnaceSyncPayload` carrying generic `items: List<ItemStack>` and `displayType: String`.
- Protocol abstraction: `CustomPacketPayload` + `StreamCodec` (26.3, 1.21.1), `PacketByteBuf` (1.20.1).
- Rendering abstraction: `HudElementRegistry` / `GuiGraphicsExtractor` (26.3), `HudRenderCallback` / `GuiGraphics` (1.21.1, 1.20.1).
- Target Java toolchains automatically aligned via Foojay resolver (Java 25, 21, 17).

### Removed
- Legacy single-target build configuration.
- Hardcoded furnace-only client inspection.

---

## [1.0.0] - 2026-10-19
- Initial release: Fabric 26.2 furnace glance HUD with ease-out cubic animation and grace period.

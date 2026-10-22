<div align="center">

# QuickGlance

**A sleek, minimalist HUD mod for Minecraft that displays container contents in real-time right where you look — without opening the GUI.**

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/quickglance?color=00AF5C&label=Modrinth&logo=modrinth)](https://modrinth.com/mod/quickglance)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1658041?color=F16436&label=CurseForge&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/quickglance)
[![GitHub Release](https://img.shields.io/github/v/release/pixiemays/QuickGlance?color=7289DA&logo=github)](https://github.com/pixiemays/QuickGlance/releases)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE.txt)

[Features](#features) • [Compatibility](#compatibility) • [Installation](#installation) • [Configuration](#configuration) • [Datapacks](#datapack-support) • [Building](#building-from-source)

</div>

---

## Features

- **Fluid Animations**: Smooth cubic ease-out fade-in/out transitions when glancing at containers.
- **Anti-Flicker Grace Period**: Configurable look-away grace window prevents jitter when your crosshair quickly sweeps past blocks.
- **Adaptive Layouts**:
  - **Furnace-like**: Input, fuel, and smelting result (Furnace, Blast Furnace, Smoker).
  - **Campfire-like**: 2×2 grid layout displaying all four cooking slots (Campfire, Soul Campfire).
  - **Row-like**: Auto-scaling horizontal rows (1–9 items) and compact wrapped grids for larger containers (Brewing Stand, Hopper, Dispenser, Dropper, Jukebox, Chiseled Bookshelf, and modded vaults).
- **Multi-Loader Support**: Works on Fabric, Quilt, and NeoForge across multiple Minecraft releases.
- **In-Game Configuration**: Full MidnightLib GUI integration accessible directly from ModMenu (Fabric) or the Mods menu (NeoForge).
- **Datapack Extensible**: Add custom modded blocks or redefine existing container layouts using simple vanilla JSON tags.

---

## Compatibility

| Minecraft Version | Fabric | Quilt | NeoForge |
|---|:---:|:---:|:---:|
| **26.3** | Yes | Yes | Yes |
| **1.21 / 1.21.1** | Yes | Yes | Yes |
| **1.20.1** | Yes | Yes | — |

> **Requirement**: QuickGlance is designed for both **client and server**. When installed on the server, inventory synchronization packets are delivered with minimal bandwidth. Singleplayer works out of the box.

---

## Installation

1. Make sure you have your preferred mod loader installed ([Fabric Loader](https://fabricmc.net/), [Quilt](https://quiltmc.org/), or [NeoForge](https://neoforged.net/)).
2. For Fabric/Quilt, ensure [Fabric API](https://modrinth.com/mod/fabric-api) / [Quilted Fabric API](https://modrinth.com/mod/qsl) is present.
3. Download the matching **QuickGlance** `.jar` for your loader and game version:
   - [Modrinth](https://modrinth.com/mod/quickglance)
   - [CurseForge](https://www.curseforge.com/minecraft/mc-mods/quickglance)
   - [GitHub Releases](https://github.com/pixiemays/QuickGlance/releases)
4. Place the `.jar` into your Minecraft `.minecraft/mods` directory (and your server's `mods` folder if running multiplayer).

---

## Configuration

Press the Mod Settings button in **ModMenu** (Fabric) or the **Mods** list (NeoForge), or edit `config/quickglance.json`:

| Option | Default | Description |
|---|:---:|---|
| `animDurationMs` | `150` | Fade transition time in milliseconds. |
| `gracePeriodMs` | `100` | Delay in milliseconds before the HUD hides after moving the crosshair away. |
| `offsetX` | `14` | Horizontal pixel offset relative to the crosshair. |
| `offsetY` | `22` | Vertical pixel offset relative to the crosshair. |

---

## Datapack Support

Want to add QuickGlance support for modded containers or custom blocks? QuickGlance is fully datapack-driven. You can tag any block entity without writing any code:

- `#quickglance:furnace_like`: 3-slot furnace layout.
- `#quickglance:campfire_like`: 2×2 grid layout.
- `#quickglance:row_like`: dynamic item row / grid.

For complete guides, JSON examples, and version-specific paths, check [DATAPACKS.md](https://github.com/pixiemays/QuickGlance/blob/master/DATAPACKS.md) *(available in English and Russian)*.

---

## Building from Source

QuickGlance uses [Stonecutter](https://github.com/kikugie/stonecutter) to manage multiple Minecraft versions and loaders simultaneously.

Requirements:
- JDK 21 (or newer, aligned with toolchains)

Commands:
```bash
# Clone the repository
git clone https://github.com/pixiemays/QuickGlance.git
cd QuickGlance

# Build all versions (Fabric + NeoForge across 26.3, 1.21.1, 1.20.1)
./gradlew buildAll

# Or build an active Stonecutter target
./gradlew build
```

Compiled jars will be generated in `build/libs/`.

---

## License

QuickGlance is licensed under the [GNU General Public License v3.0](LICENSE.txt).

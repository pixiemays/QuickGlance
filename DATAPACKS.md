# QuickGlance — Datapack Compatibility Guide / Руководство по датапакам

[English](#english) | [Русский](#русский)

---

<a name="english"></a>
## English

QuickGlance allows players, server admins, and modpack creators to add compatibility for custom blocks and modded containers using vanilla **datapack block tags**. No Java or Kotlin code is required!

### Available Tags

| Tag Identifier | Display Layout | Slot Mapping | Default Vanilla Blocks |
|---|---|---|---|
| `#quickglance:furnace_like` | Classic 3-slot furnace panel (64×44) | Slot 0 = input, Slot 1 = fuel, Slot 2 = output | `furnace`, `blast_furnace`, `smoker` |
| `#quickglance:campfire_like` | 2×2 grid panel (44×44) | Slots 0–3 mapped to four corners | `campfire`, `soul_campfire` |
| `#quickglance:row_like` | Dynamic row / wrapped grid | Slots 1–9 as single row; >9 items as wrapped 9-column grid | `brewing_stand`, `chiseled_bookshelf`, `jukebox`, `dispenser`, `dropper`, `hopper` |

> **Note on Large Containers**: For `#quickglance:row_like` containers with more than 9 slots (e.g., chests or modded vaults), QuickGlance automatically filters out empty slots so only non-empty items are rendered.

---

### Datapack Directory Structure

Depending on your Minecraft version, place tag files in the corresponding path:

**Minecraft 1.21.1+ / 26.3:**
```text
my_datapack/
├── pack.mcmeta
└── data/
    └── quickglance/
        └── tags/
            └── block/
                ├── furnace_like.json
                ├── campfire_like.json
                └── row_like.json
```

**Minecraft 1.20.1 (Legacy plural path):**
```text
my_datapack/
├── pack.mcmeta
└── data/
    └── quickglance/
        └── tags/
            └── blocks/
                ├── furnace_like.json
                ├── campfire_like.json
                └── row_like.json
```

---

### Example Configurations

#### 1. Add Chests and Barrels to Row Display
File: `data/quickglance/tags/block/row_like.json`
```json
{
  "replace": false,
  "values": [
    "minecraft:chest",
    "minecraft:trapped_chest",
    "minecraft:barrel",
    "#c:chests"
  ]
}
```

#### 2. Add Modded Kilns to Furnace Display
File: `data/quickglance/tags/block/furnace_like.json`
```json
{
  "replace": false,
  "values": [
    "create:blaze_burner",
    "farmersdelight:skillet"
  ]
}
```

#### 3. Add Modded Grills to Campfire Display
File: `data/quickglance/tags/block/campfire_like.json`
```json
{
  "replace": false,
  "values": [
    "farmersdelight:stove"
  ]
}
```

#### `pack.mcmeta` Example:
```json
{
  "pack": {
    "pack_format": 48,
    "description": "QuickGlance custom container compat"
  }
}
```
*(Use `pack_format: 15` for 1.20.1, `pack_format: 48` for 1.21.1, `pack_format: 70` for 26.3).*

---

### Installation
1. Put your datapack folder or `.zip` into `<world>/datapacks/`.
2. Run `/reload` in-game or restart the server.
3. Look at the registered block to view its HUD!

---

<a name="русский"></a>
## Русский

QuickGlance позволяет игрокам, администраторам серверов и создателям сборок добавлять поддержку любых пользовательских и модовых контейнеров через стандартные **теги блоков в датапаках**. Писать код на Java или Kotlin не требуется!

### Доступные теги

| Идентификатор тега | Тип отображения | Соответствие слотов | Ванильные блоки по умолчанию |
|---|---|---|---|
| `#quickglance:furnace_like` | Классическая плашка печи (64×44) | Слот 0 = вход, Слот 1 = топливо, Слот 2 = выход | `furnace`, `blast_furnace`, `smoker` |
| `#quickglance:campfire_like` | Сетка 2×2 (44×44) | Слоты 0–3 по четырем углам | `campfire`, `soul_campfire` |
| `#quickglance:row_like` | Динамическая строка / сетка | 1–9 слотов в одну строчку; >9 слотов сеткой в 9 колонок | `brewing_stand`, `chiseled_bookshelf`, `jukebox`, `dispenser`, `dropper`, `hopper` |

> **Примечание для больших контейнеров**: Для контейнеров в теге `#quickglance:row_like`, имеющих более 9 слотов (например, сундуки), мод автоматически скрывает пустые ячейки и выводит только предметы, находящиеся внутри.

---

### Структура директорий датапака

В зависимости от версии Minecraft файлы тегов размещаются по следующим путям:

**Для Minecraft 1.21.1+ и 26.3:**
```text
my_datapack/
├── pack.mcmeta
└── data/
    └── quickglance/
        └── tags/
            └── block/
                ├── furnace_like.json
                ├── campfire_like.json
                └── row_like.json
```

**Для Minecraft 1.20.1 (исторический путь `blocks`):**
```text
my_datapack/
├── pack.mcmeta
└── data/
    └── quickglance/
        └── tags/
            └── blocks/
                ├── furnace_like.json
                ├── campfire_like.json
                └── row_like.json
```

---

### Примеры настройки

#### 1. Добавить сундуки и бочки в строчный HUD
Файл: `data/quickglance/tags/block/row_like.json`
```json
{
  "replace": false,
  "values": [
    "minecraft:chest",
    "minecraft:trapped_chest",
    "minecraft:barrel",
    "#c:chests"
  ]
}
```

#### 2. Добавить модовые печи в печной HUD
Файл: `data/quickglance/tags/block/furnace_like.json`
```json
{
  "replace": false,
  "values": [
    "create:blaze_burner",
    "farmersdelight:skillet"
  ]
}
```

#### 3. Добавить модовые жаровни в костровой HUD
Файл: `data/quickglance/tags/block/campfire_like.json`
```json
{
  "replace": false,
  "values": [
    "farmersdelight:stove"
  ]
}
```

#### Пример `pack.mcmeta`:
```json
{
  "pack": {
    "pack_format": 48,
    "description": "QuickGlance custom container compat"
  }
}
```
*(Для 1.20.1: `pack_format: 15`, для 1.21.1: `pack_format: 48`, для 26.3: `pack_format: 70`).*

---

### Установка в игру
1. Скопируйте папку с датапаком или `.zip` архив в папку `<мир>/datapacks/`.
2. Выполните команду `/reload` в игре или перезапустите сервер.
3. Наведите прицел на блок — HUD содержимого отобразится автоматически!

<div align="center">

<img src="https://cdn.modrinth.com/data/GSDqwXdk/db8d854f518848de2b092674c4e639bef3ece4b3_96.webp" alt="QuickGlance Logo" width="96" height="96" />

# QuickGlance

**Минималистичный HUD-мод для Minecraft, отображающий содержимое контейнеров в реальном времени прямо по направлению взгляда — без необходимости открывать интерфейс.**

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/quickglance?color=00AF5C&label=Modrinth&logo=modrinth)](https://modrinth.com/mod/quickglance)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1658041?color=F16436&label=CurseForge&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/quickglance)
[![GitHub Release](https://img.shields.io/github/v/release/pixiemays/QuickGlance?color=7289DA&logo=github)](https://github.com/pixiemays/QuickGlance/releases)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](LICENSE.txt)

[English](README.md) • **Русский**

[Возможности](#возможности) • [Совместимость](#совместимость) • [Установка](#установка) • [Настройка](#настройка) • [Датапаки](#поддержка-датапаков) • [Сборка](#сборка-из-исходников)

</div>

---

## Возможности

- **Плавная анимация**: Мягкие переходы появления и затухания (cubic ease-out) при взгляде на контейнеры.
- **Защита от мерцания (Grace Period)**: Настраиваемая задержка перед скрытием интерфейса предотвращает дергание при быстром движении прицела мимо блоков.
- **Адаптивные макеты**:
  - **Печи**: Слот сырья, топливо и готовый результат (обычная печь, плавильня, коптильня).
  - **Костры**: Сетка 2×2, отображающая все 4 готовящихся предмета (обычный костер и костер душ).
  - **Ряды и сетки**: Автоматически масштабируемые горизонтальные ряды (1–9 слотов) и компактные сетки для более вместительных контейнеров (зельеварка, воронка, раздатчик, выбрасыватель, проигрыватель, резная книжная полка и хранилища из модов).
- **Мультилоадер**: Поддержка Fabric, Quilt и NeoForge на актуальных версиях Minecraft.
- **Внутриигровая настройка**: Полная интеграция с интерфейсом MidnightLib через ModMenu (Fabric) или меню Mods (NeoForge).
- **Поддержка датапаков**: Легкое добавление любых блоков из модов и кастомизация макетов через стандартные JSON-теги без строчки кода.

---

## Совместимость

| Версия Minecraft | Fabric | Quilt | NeoForge |
|---|:---:|:---:|:---:|
| **26.3** | Да | Да | Да |
| **1.21 / 1.21.1** | Да | Да | Да |
| **1.20.1** | Да | Да | — |

> **Требование**: QuickGlance разработан для установки на **клиент и сервер**. При установке на сервер синхронизация содержимого происходит легковесными сетевыми пакетами с минимальной нагрузкой. В одиночной игре всё работает «из коробки».

---

## Установка

1. Убедитесь, что установлен нужный загрузчик модов ([Fabric Loader](https://fabricmc.net/), [Quilt](https://quiltmc.org/) или [NeoForge](https://neoforged.net/)).
2. Для Fabric/Quilt установите [Fabric API](https://modrinth.com/mod/fabric-api) / [Quilted Fabric API](https://modrinth.com/mod/qsl).
3. Скачайте подходящий файл **QuickGlance** `.jar` для вашей версии и загрузчика:
   - [Modrinth](https://modrinth.com/mod/quickglance)
   - [CurseForge](https://www.curseforge.com/minecraft/mc-mods/quickglance)
   - [GitHub Releases](https://github.com/pixiemays/QuickGlance/releases)
4. Поместите файл `.jar` в папку `.minecraft/mods` (и в папку `mods` сервера для игры по сети).

---

## Настройка

Открыть настройки можно через кнопку в **ModMenu** (Fabric), в списке **Mods** (NeoForge) либо отредактировав файл `config/quickglance.json`:

| Параметр | По умолчанию | Описание |
|---|:---:|---|
| `animDurationMs` | `150` | Длительность анимации появления/исчезновения в миллисекундах. |
| `gracePeriodMs` | `100` | Задержка в миллисекундах перед скрытием HUD после отвода прицела с блока. |
| `offsetX` | `14` | Смещение интерфейса по горизонтали относительно прицела. |
| `offsetY` | `22` | Смещение интерфейса по вертикали относительно прицела. |

---

## Поддержка датапаков

Хотите добавить поддержку контейнеров из других модов? QuickGlance полностью управляется через датапаки. Любой блок можно разметить соответствующим тегом:

- `#quickglance:furnace_like`: 3-слотовый макет печи.
- `#quickglance:campfire_like`: макет сетки 2×2.
- `#quickglance:row_like`: динамический ряд / сетка предметов.

Подробные примеры, структура папок и готовые JSON-файлы описаны в [DATAPACKS.md](https://github.com/pixiemays/QuickGlance/blob/master/DATAPACKS.md) *(раздел на русском языке доступен в документе)*.

---

## Сборка из исходников

Для параллельной сборки под несколько версий и загрузчиков проект использует [Stonecutter](https://github.com/kikugie/stonecutter).

Требования:
- JDK 21 (или новее)

Команды:
```bash
# Клонирование репозитория
git clone https://github.com/pixiemays/QuickGlance.git
cd QuickGlance

# Сборка всех версий (Fabric + NeoForge для 26.3, 1.21.1, 1.20.1)
./gradlew buildAll

# Либо сборка конкретной активной цели Stonecutter
./gradlew build
```

Собранные файлы jar будут находиться в папке `build/libs/`.

---

## Лицензия

QuickGlance распространяется под лицензией [GNU General Public License v3.0](LICENSE.txt).

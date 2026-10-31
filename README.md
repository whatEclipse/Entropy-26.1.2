# Entropy

A Fabric mod for Minecraft **26.1.2** that adds new weapons, a player-to-player contract system, a few foods, custom music discs and an amethyst building set.

> **Status:** early development (`0.3.0`). Some content is unfinished and may change or be removed between versions.

## Features

### Weapons

| Item | Notes |
|------|-------|
| **Scythe** | Iron-tier weapon with extended reach, sweep attacks and a hoe-style mining ability. Has two exclusive enchantments (see below). |
| **Atlan Trident** | A golden trident that can be thrown and supports Riptide. |
| **Shark Tooth Cleaver** | Short-reach, fast iron-tier blade. Currently marked in-game as not fully implemented. |

#### Scythe

- Iron material, 7 attack damage, 1782 durability, Epic rarity, repairable with iron.
- 3.5 block attack range, with a sweep attack on full-strength hits.
- Mines blocks in the `mineable/hoe` tag at 9x speed.
- Can be enchanted with **Beheading** when the [Enchancement](https://modrinth.com/) mod is installed (the Scythe is included in the `entropy:beheading_items` tag alongside axes).

**Enchantments** (Scythe only, max level 1)

- **Spewing**: right-click to launch a blood projectile that deals 12 damage to everything it passes through. It costs you 3 magic damage, has a 1 second cooldown, and transfers half the remaining duration of your active potion effects onto the projectile, which applies them to whatever it hits.
- **Reeling**: critical hits pull the target toward you and briefly stun it.

#### Stunned effect

A custom status effect applied by Reeling. Stunned mobs drop their target and stop navigating; stunned entities cannot attack or deal damage.

#### Atlan Trident

- Gold material, 9 attack damage, Epic rarity, enchantable.
- Throwable (spawns a `Thrown Atlan Trident` entity) and works with Riptide in water or rain.
- Found in **buried treasure** chests.

### Contracts

Contracts let two players put an agreement in writing.

1. Craft an **Open Contract**: feather + ink sac + paper (shapeless).
2. Write in it and **Bind** it. This records you as the first signer.
3. Another player uses the **Add Witness** button, which turns it into a **Closed Contract** that is read-only and lists both names.

### Food

| Item | How to get it |
|------|---------------|
| **Dripstone Salt** | 2 dripstone blocks |
| **Butter** | Milk bucket + salt (makes 3), or Farmer's Delight milk bottle + salt (makes 1) |
| **Buttered Bread** | Bread + butter (makes 2) |

### Music discs

- **Delirio** (Gianni Morandi)
- **Perfect Funeral** (StarbornNebula)

Both are crafted from an existing music disc plus ingredients (rose bush or lilac, phantom membrane, and amethyst shards for Perfect Funeral). Song data and audio for "I will never be on your side again" (Winsweep) is also included, but its disc item is currently disabled in code.

### Building blocks

Amethyst Bricks, Amethyst Brick Stairs, Slab and Wall, Amethyst Pillar and Chiseled Amethyst. All are craftable from amethyst blocks and interconvertible with each other.

## Obtaining the Scythe

1. Find a **Scythe Smithing Template** in ruined portal chests (70% chance per chest).
2. At a smithing table, combine the template with a **Netherite Hoe** and an **Iron Ingot**.

## Requirements

| Dependency | Version |
|------------|---------|
| Minecraft | 26.1.2 |
| Java | 25 or newer |
| Fabric Loader | 0.19.3 or newer |
| Fabric API | required |

**Recommended:** Combat Amenities (the Scythe and Atlan Trident ship with back-slot display transforms).

**Optional integrations:** Enchancement (Beheading enchantment), Farmer's Delight (milk bottle butter recipe).

## Building

```bash
git clone <this-repository>
cd Entropy-26.1.2
./gradlew build
```

The compiled jar is written to `build/libs/`. A JDK 25 is required (CI uses the Microsoft build of OpenJDK 25).

To regenerate data-generated assets (jukebox songs, sound definitions, tags):

```bash
./gradlew runDatagen
```

Generated files are written to `src/main/generated`.

### Project layout

```
src/main/java/net/entropy/
  Entropy.java          Mod entrypoint
  items/                Weapons and contract items
  entity/projectile/    Blood Scythe and Thrown Atlan Trident
  effects/              Stunned effect
  registry/             Items, blocks, entities, effects, enchantments, data components
  datagen/              Data generators
  mixin/                Contract signing and attack-strength capture
  network/              Witness signing packet
src/client/java/        Client-only code: GUIs, particles, entity renderers
src/main/resources/     Assets, recipes, enchantments, loot modifiers, lang files
```

`build.gradle` also pulls in two Modrinth mods as development dependencies.

## Credits

- Author: whatEclipse
- Music: Gianni Morandi ("Delirio"), StarbornNebula ("perfect funeral"), Winsweep ("I will never be on your side again."). Music is the property of its respective artists.

## License

Released under [CC0 1.0 Universal](LICENSE).

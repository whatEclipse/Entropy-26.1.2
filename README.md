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

- Netherite level hoe, 10 attack damage, 1782 durability, Epic rarity, repairable with iron.
- 3.5 block attack range, with a sweep attack on full-strength hits.
- Mines blocks in the `mineable/hoe` tag as fast as netherite hoes.
- Can be enchanted with **Beheading** when the [Enchancement](https://modrinth.com/mod/enchancement) mod is installed (the Scythe is included in the `entropy:beheading_items` tag alongside axes).

**Enchantments** (Scythe only, max level 1)

- **Spewing**: right-click to launch a blood projectile that deals 12 damage to everything it passes through. It costs you 3 self damage, has a 1 second cooldown, and transfers half the remaining duration of your active potion effects onto the projectile, which applies them to whatever it hits.
- **Reeling**: critical hits pull the target toward you and briefly applying the **Stunned** effect.

#### Stunned effect

A custom status effect applied by Reeling. Stunned mobs drop their target and stop navigating; stunned entities cannot attack or deal damage.

#### Atlan Trident

- Gold material, 10 attack damage, Epic rarity, enchantable like a normal trident.
- Throwable (spawns a `Thrown Atlan Trident` entity) and works with Riptide in water or rain.
- Found in **buried treasure** chests with a 100% probability (**Might change in the future!!**).

### Contracts

Contracts let two players put an agreement in writing.

1. Craft an **Open Contract**: feather + ink sac + paper (shapeless).
2. Write in it and **Bind** it. This records you as the first signer.
3. Another player uses the **Add Witness** button, which turns it into a **Closed Contract** that is read-only and lists both names.

### Food

| Item | Shaped / Shapeless | How to get it | Output |
|------|---------------|---------------|--------------| 
| **Dripstone Salt** | Shapeless | 2 dripstone blocks | 1 | 
| **Butter** | Shapeless | Milk bucket + salt, or Farmer's Delight milk bottle + salt | 3 / 1 | 
| **Buttered Bread** | Shapeless | Bread + butter | 2 | 

### Music discs

- **Delirio** (Gianni Morandi) 
- **Perfect Funeral** (StarbornNebula)

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

**Optional integrations:** [Enchancement](https://modrinth.com/mod/enchancement) ; [Farmer's Delight](https://modrinth.com/mod/farmers-delight) ; [Combat Amenities](https://modrinth.com/mod/combat-amenities)

## Credits

- Author: whatEclipse
- Music: Gianni Morandi ("Delirio"), StarbornNebula ("perfect funeral"). **Music is the property of its respective artists.**

## License

Released under [CC0 1.0 Universal](LICENSE).

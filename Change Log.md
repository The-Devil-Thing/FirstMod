01/02/2025
* Ran the pre-built mod in the NeoForge.

09/02/2025

Rearranged the structure of the mod.

Added:
* Items
    * Mercuric Droplets
    * Mercury Drop
* Blocks
    * Pedestal (Still has Stone Texture)
* Creative Mod Tab

Added respective json for models/textures and more.

Added Recipes for the Items.

Added Loot Table and Mining requirement to Pedestal.

11/02/2025

Downgraded to 1.21.1

Added:
* Blocks
    * Infuser (Still has Diamond Block Texture)

12/02/2025

Added:
* Items
    * Seed Item (Just a base item)

06/03/2025

Added:
* Tags
    * Infusable - All the items that could be infused into the seed.
        * Currently has:
            * c:ingots
            * c:gems
            * Redstone Dust

07/03/2025

Added:
* Blocks
    * Nether Mercury Ore (Retextured Nether Quartz Ore)

* Data Gen
    * Loot Table
    * Block State
    * Block and Item tag
    * Item Model
    * Recipe

23/03/2025

Added:
* Data Components
  * Infusion Percentage
  * Infused Item

Added logic for infusion by right-clicking on the infuser with the seed in main hand and an infusable item in the offhand.

Currently, all items require 10 of them to be infused fully in the seed.

26/03/2025

Added:
* Config
  * Main Config
    * Max number of tiers
    * Base amount for infusion
    * List of multiplicative index for the tiers
  * Second Config
    * The items and the tier they should be in.

* Datapack
  * Datapack for dynamic generation of 'infusables' tag based on config file. 

* Data Component
  * Tier - Denotes the tier of Seed.

* Added tiers to SeedItem.
  * Can be increased by having another seed in offhand while right-clicking the seed on infuser.

Removed:
* Data Generator:
  * Data Generator for 'infusables' tag. (Added dynamic generation)

Upgrade to v1.21.1-0.0.2

29/03/2025

Added:
* Ore Generation
  * Nether Mercury Ore

31/03/2025

Added:
* Mercuric Droplets as Extra Loot for Some Ore blocks.

Changed:
* Distribution of Nether Mercury Ore.

04/04/2025

Added:
* Loot Item Condition
  * Loot Item Block Tag Condition
    * Checks for block tags

* Enchantment
  * Mercury Filter
    * Just shell with no functionality yet.

07/04/2025

Changed:
* Infuser converted to Block Entity
  * Added Renderer
  * Can hold 1 item like a item frame

10/07/2025

Added:
* Menu for the Infuser
  * Currently a Furnace Texture.
  * Currently just has 1 slot (even if multiple are visible).

Changed:
* Removed adding/removing of the item to Infuser's inventory on right-clicking.
* Can add/remove item from inventory from the menu now.

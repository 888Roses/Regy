# Regy
A fabric registering wrapper to make initialization a piece of cake :-)

## Why use Regy?
- **Easy to use** Regy is easier than Minecraft's default registry system, especially for beginners.
- **Complete** Regy contains registering methods for every registry in Minecraft. No half Mojang, half Regy situation.
- **Expandable** Regy is made with expandability at its core, making it very simple to add new data generators, registries, etc.

## How to use?
Every mod needs to declare its own wrapper extending the class `AbstractRegy`.
Luckily, there is a default implementation that has everything you need simply called `Regy`:
```java
// In your ModInitializer class (e.g. ExampleMod.java)
public static final Regy REGY = Regy.create(MOD_ID);
```
Then, you can start creating your objects! Let's create a simple block.

### Creating a Simple Block
Let's first create a block with the identifier `example_block` inheriting from the base
Minecraft class `Block`:
```java
REGY.block("example_block", Block::new)
```
Using `AbstractRegy#block` gives us a `BlockEntryBuilder` of whatever type of
`Block` we gave it as second argument (in our case, it was just a `Block`, so we get
a `BlockEntryBuilder<Block>`). In order to register our block, we simply need to call
`register()`:
```java
// In a ModBlocks.java class or any kind of index class:
public static final BlockEntry<Block> EXAMPLE_BLOCK = REGY
        .block("example_block", Block::new)
        .register();
```
The `register()` method returns a `BlockEntry` of whatever block type the builder was.
If not specified, the block will also have a name generated from its identifier,
(e.g. `example_block` -> `Example Block`) and a default `BlockState` of a full block
expecting a texture located under:
```
resources/assets/MOD_ID/textures/block/BLOCK_ID.png
```
This won't, though, generate an item for the block. So let us add the item for our block;
This is as simple as adding a couple of lines!
```java
public static final BlockEntry<Block> EXAMPLE_BLOCK = REGY
        .block("example_block", Block::new)
        .item()
        .build()
        .register();
```
The `item()` method redirects us to an `ItemEntry` of a `BlockItem` for our block,
allowing us to customize our item as much as we want. Then, `build()` registers the
item and redirects us back to the `BlockEntry`.
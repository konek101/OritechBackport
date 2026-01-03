<br/>
<p align="center">
  <a href="https://github.com/rearth/Oritech">
    <img src="https://github.com/Rearth/Oritech/assets/10100603/d459b3fa-ef6f-4675-99d7-c44a78a3cf71" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Oritech - 1.20.1 Forge Backport</h3>

<div align="center">
  A minecraft Forge 1.20.1 backport of the Oritech tech mod.
  <br/>
  <br/>
  <strong>⚠️ WORK IN PROGRESS - Requires major source code rewrites ⚠️</strong>
  <br/>
  <br/>
  <a href="https://moddedmc.org/en/mod/oritech/docs"><strong>Explore the original docs»</strong></a>
  <br/>
  <br/>
  <a href="https://github.com/konek101/OritechBackport/issues">Report Bug</a>
  .
  <a href="https://github.com/konek101/OritechBackport/issues">Request Feature</a>
  <br/>
  <br/>
</div>

---

## About This Backport

This is an attempt to backport the Oritech mod from Minecraft 1.21.1 (NeoForge/Fabric) to Minecraft 1.20.1 (Forge 47.4.10).

### ⚠️ Important: Scope of Work Required

After testing the build with network access, **4315+ compilation errors** were discovered. The core issue is that:

1. **owo-lib** is a Fabric-only library with no Forge 1.20.1 port. The mod's GUI system, configuration, and serialization all depend on owo-lib.
2. **Minecraft 1.21 APIs** used throughout the code don't exist in 1.20.1 (StreamCodec, RecipeInput, RecipeOutput, RegistryFriendlyByteBuf, etc.)

This means a proper backport would require **rewriting the entire mod**, not just adapting APIs.

### Backport Status

#### ✅ Build System (Complete)
- ForgeGradle configuration for MC 1.20.1 / Forge 47.4.10
- Gradle properties and wrapper configured
- Java 17 toolchain setup
- mods.toml in Forge format
- Mixin configurations for Java 17
- All data files converted from NeoForge to Forge format (biome modifiers, recipe conditions)
- Basic Forge mod entrypoint created

#### ❌ Source Code (Requires Major Rewrite)
The 454 Java source files have **4315+ compilation errors** due to:

| Issue | Description | Affected Files |
|-------|-------------|----------------|
| owo-lib | No Forge 1.20.1 port exists | All GUI, config, serialization code |
| StreamCodec | MC 1.21 networking API | Network packets, recipes |
| RecipeInput/Output | MC 1.21 recipe API | All recipe handling |
| DataComponents | MC 1.21 item data system | All item/block data storage |
| ResourceLocation | API signature changes | Every file using resource locations |

### Recommendations

For a Forge 1.20.1 version of Oritech, consider:

1. **Starting from Oritech 1.20.4 Fabric branch** - This was Fabric-only but much closer to 1.20.1 APIs
2. **Using Sinytra Connector** - Run the Fabric version on Forge via compatibility layer
3. **Complete rewrite** - Rebuild from scratch for Forge using Forge-native APIs

### Key Differences Between NeoForge 1.21 and Forge 1.20.1

| Feature | NeoForge 1.21 | Forge 1.20.1 |
|---------|---------------|--------------|
| Java Version | 21 | 17 |
| Mod Loader | NeoForge | MinecraftForge |
| Event Bus | `net.neoforged.bus.api.*` | `net.minecraftforge.eventbus.api.*` |
| Capabilities | `RegisterCapabilitiesEvent` | `AttachCapabilitiesEvent` |
| Networking | Payload/StreamCodec system | SimpleChannel/FriendlyByteBuf |
| Data Storage | DataComponents | NBT-based |
| Recipe API | RecipeInput/RecipeOutput | Container/FinishedRecipe |
| Required Libraries | owo-lib (Fabric) | No equivalent |

## Building

The build system works - dependencies resolve and the Forge setup completes:

```bash
./gradlew :forge:compileJava
# Results in 4315+ errors due to source code incompatibilities
```

## Original Project

This is a backport of [Rearth's Oritech](https://github.com/Rearth/Oritech) mod.

## License

See the original project's LICENSE.md
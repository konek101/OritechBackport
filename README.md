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
  <strong>⚠️ WORK IN PROGRESS - Requires source code rewrites ⚠️</strong>
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

### Build Configuration with Sinytra Connector

This backport uses **Sinytra Connector** to allow Fabric libraries (like owo-lib) to run on Forge. The build configuration includes:
- Sinytra Connector 1.0.0-beta.43
- Forgified Fabric API 0.92.2
- owo-lib 0.11.2+1.20
- endec 0.1.5 (serialization library)
- **Java 21** (required for endec compatibility)

### ⚠️ Remaining Blockers

Despite adding Java 21, owo-lib, and endec, **3083 compilation errors** remain due to:

1. **owo-lib 0.12+ serialization APIs** - The source code uses `EndecRecipeSerializer`, `CodecUtils`, `MinecraftEndecs` from `io.wispforest.owo.serialization.*` which were added in owo-lib 0.12.0 (for MC 1.20.3+). The 1.20.1 compatible owo-lib (0.11.x) doesn't have these.
2. **MC 1.21 APIs** - StreamCodec, RecipeInput, RecipeOutput, RegistryFriendlyByteBuf don't exist in MC 1.20.1

### Backport Status

#### ✅ Build System (Complete)
- ForgeGradle configuration for MC 1.20.1 / Forge 47.4.10
- Sinytra Connector + Forgified Fabric API integration
- owo-lib 0.11.2+1.20 via Connector
- endec 0.1.5 serialization library
- Gradle properties and wrapper configured
- **Java 21 toolchain setup** (required for endec)
- mods.toml in Forge format
- All data files converted from NeoForge to Forge format
- Basic Forge mod entrypoint created

#### ❌ Source Code (Requires API Adaptation)
The 454 Java source files have **3083 compilation errors** due to:

| Issue | Description | Solution Required |
|-------|-------------|-------------------|
| endec serialization | Used in recipe/config serialization | Rewrite to use Codec/FriendlyByteBuf |
| StreamCodec | MC 1.21 networking API | Rewrite to use FriendlyByteBuf |
| RecipeInput/Output | MC 1.21 recipe API | Rewrite to use Container/FinishedRecipe |
| DataComponents | MC 1.21 item data system | Rewrite to use NBT-based storage |
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
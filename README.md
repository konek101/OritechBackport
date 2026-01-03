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
  <strong>⚠️ WORK IN PROGRESS - This backport is not yet functional ⚠️</strong>
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

### Backport Status

The build system has been configured for Forge 1.20.1, but significant source code changes are still required:

#### ✅ Completed
- Build configuration updated for ForgeGradle
- Gradle properties configured for MC 1.20.1
- Java version updated from 21 to 17
- mods.toml updated for Forge format
- Mixin configurations updated for Java 17
- NeoForge platform code removed
- Basic Forge mod entrypoint created

#### ❌ Still Required
- **API Changes**: Many Minecraft APIs changed between 1.20.1 and 1.21
  - `ResourceLocation.fromNamespaceAndPath()` → `new ResourceLocation(namespace, path)`
  - Component system changes (1.21 uses DataComponents, 1.20.1 uses NBT)
  - Registry API differences
- **Architectury API**: Common code uses Architectury 13.x which is for 1.21; needs Architectury 9.x for 1.20.1
- **owo-lib**: The mod heavily depends on owo-lib which has different versions for each MC version
- **GeckoLib**: Version needs updating for 1.20.1 compatibility
- **Platform Layer**: The Forge platform implementation needs to be written to replace NeoForge-specific code
- **Energy/Fluid APIs**: Need to use Forge capabilities system instead of NeoForge's capability registration

### Key Differences Between NeoForge 1.21 and Forge 1.20.1

| Feature | NeoForge 1.21 | Forge 1.20.1 |
|---------|---------------|--------------|
| Java Version | 21 | 17 |
| Mod Loader | NeoForge | MinecraftForge |
| Event Bus | `net.neoforged.bus.api.*` | `net.minecraftforge.eventbus.api.*` |
| Capabilities | `RegisterCapabilitiesEvent` | `AttachCapabilitiesEvent` |
| Networking | Payload system | SimpleChannel |
| Data Components | Component system | NBT-based |
| Registries | DeferredRegister/NeoForge | DeferredRegister/Forge |

## Building

```bash
./gradlew :forge:build
```

## Original Project

This is a backport of [Rearth's Oritech](https://github.com/Rearth/Oritech) mod.

## License

See the original project's LICENSE.md
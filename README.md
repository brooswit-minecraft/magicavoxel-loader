# MagicaVoxel Loader

Reusable NeoForge 21.1.250+ library/mod for Minecraft 1.21.1 that loads packaged MagicaVoxel `.vox` assets and converts their voxels to Minecraft-native 0..16 item-model elements at game setup.

## Consumer use

Declare the GitHub Packages Maven repository and dependency (a GitHub token with package-read access is required for the private package), then include it as a required NeoForge mod dependency. Do not copy its parser into consumer mods.

```groovy
repositories {
    maven {
        url = uri('https://maven.pkg.github.com/brooswit-minecraft/magicavoxel-loader')
        credentials {
            username = providers.gradleProperty('gpr.user').get()
            password = providers.gradleProperty('gpr.key').get()
        }
    }
}
dependencies {
    implementation 'io.github.brooswitminecraft:magicavoxel-loader:0.1.2'
}
```

CI publishes the build to Maven Local and resolves this exact coordinate from `consumer-stub` before accepting a change. Tagged releases publish the same versioned coordinate to GitHub Packages and attach the JAR to the GitHub Release.

Place an asset at `src/main/resources/assets/<your-modid>/vox/<name>.vox`. Load it at common setup with `VoxAssetLoader.load(...)`, then pass the resulting `VoxelModel` to `MinecraftItemModelConverter.convert(...)`; the output is Minecraft's native item-model element coordinate space and is suitable for the item-model bake/render integration.

## Asset standard

- MagicaVoxel **150** `.vox` files only; the first `SIZE`/`XYZI` model is loaded.
- Each axis must be **1–16** cells, and the model must contain at most **1,024** voxels. These bounds guarantee 1/16-grid-compatible Minecraft coordinates.
- Palette index `0` is invalid; indices `1–255` are preserved as `#palette_<index>` material keys for the consumer's item texture/material mapping.
- Replace an asset by editing its `.vox` file at the same resource path and rebuilding the consuming mod. No export/converter step is required. The included `sample.vox` is parsed automatically at this mod's game load.

## In-game rendering

The library includes `magicavoxelloader:voxel_display`, a runtime-rendered demonstration item. In a development or consumer world, run `/give @s magicavoxelloader:voxel_display`; it uses the custom item renderer to draw each voxel from the packaged `sample.vox` through Minecraft's native block-model renderer. Replacing that `.vox` resource changes the rendered voxel arrangement on the next game start—no manual export is involved.

## Verification

CI runs `./gradlew test jar`, including packaged-asset loading, coordinate conversion, and invalid dimension/format behavior. The packaged jar contains `assets/magicavoxelloader/vox/sample.vox`; the corresponding native item model is available after common setup through `MagicaVoxelLoader.sampleItemModel()`.

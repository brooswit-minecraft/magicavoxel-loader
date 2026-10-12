# MagicaVoxel Loader

Reusable NeoForge 21.1.250+ library/mod for Minecraft 1.21.1 that loads packaged MagicaVoxel `.vox` assets and converts their voxels to Minecraft-native 0..16 item-model elements at game setup.

## Consumer use

Declare `io.github.brooswitminecraft:magicavoxel-loader:<release-version>` from the repository's published Maven/release artifact, then include it as a required NeoForge mod dependency. Do not copy its parser into consumer mods.

Place an asset at `src/main/resources/assets/<your-modid>/vox/<name>.vox`. Load it at common setup with `VoxAssetLoader.load(...)`, then pass the resulting `VoxelModel` to `MinecraftItemModelConverter.convert(...)`; the output is Minecraft's native item-model element coordinate space and is suitable for the item-model bake/render integration.

## Asset standard

- MagicaVoxel **150** `.vox` files only; the first `SIZE`/`XYZI` model is loaded.
- Each axis must be **1–16** cells, and the model must contain at most **1,024** voxels. These bounds guarantee 1/16-grid-compatible Minecraft coordinates.
- Palette index `0` is invalid; indices `1–255` are preserved as `#palette_<index>` material keys for the consumer's item texture/material mapping.
- Replace an asset by editing its `.vox` file at the same resource path and rebuilding the consuming mod. No export/converter step is required. The included `sample.vox` is parsed automatically at this mod's game load.

## Verification

CI runs `./gradlew test jar`, including packaged-asset loading, coordinate conversion, and invalid dimension/format behavior. The packaged jar contains `assets/magicavoxelloader/vox/sample.vox`; the corresponding native item model is available after common setup through `MagicaVoxelLoader.sampleItemModel()`.

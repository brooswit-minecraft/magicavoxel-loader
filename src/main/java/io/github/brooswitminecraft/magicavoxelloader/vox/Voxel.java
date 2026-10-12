package io.github.brooswitminecraft.magicavoxelloader.vox;

/** One indexed MagicaVoxel voxel. Coordinates are constrained by {@link VoxAssetLoader}. */
public record Voxel(int x, int y, int z, int paletteIndex) { }

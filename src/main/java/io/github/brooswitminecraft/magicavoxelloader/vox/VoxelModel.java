package io.github.brooswitminecraft.magicavoxelloader.vox;

import java.util.List;

public record VoxelModel(int width, int height, int depth, List<Voxel> voxels) { }

package io.github.brooswitminecraft.magicavoxelloader.vox;

import java.util.List;

/** Converts a supported voxel grid into Minecraft's 0..16 element coordinate space. */
public final class MinecraftItemModelConverter {
    private MinecraftItemModelConverter() { }
    public static MinecraftItemModel convert(VoxelModel source) {
        double sx = 16.0 / source.width(), sy = 16.0 / source.height(), sz = 16.0 / source.depth();
        List<MinecraftItemModel.Element> elements = source.voxels().stream().map(v -> new MinecraftItemModel.Element(
            bounds(v.x() * sx, v.y() * sy, v.z() * sz),
            bounds((v.x()+1) * sx, (v.y()+1) * sy, (v.z()+1) * sz),
            "#palette_" + v.paletteIndex())).toList();
        return new MinecraftItemModel(elements);
    }
    private static int[] bounds(double x, double y, double z) { return new int[] {(int)Math.round(x), (int)Math.round(y), (int)Math.round(z)}; }
}

package io.github.brooswitminecraft.magicavoxelloader.vox;

import java.util.List;

/** Minecraft's native JSON model representation, ready for the item-model bake pipeline. */
public record MinecraftItemModel(List<Element> elements) {
    public record Element(int[] from, int[] to, String texture) { }
}

package io.github.brooswitminecraft.magicavoxelloader.vox;

import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class VoxAssetLoaderTest {
    @Test void readsPackagedSupportedAssetAndConvertsToMinecraftCoordinates() throws Exception {
        try (var stream = getClass().getResourceAsStream("/assets/magicavoxelloader/vox/sample.vox")) {
            var source = VoxAssetLoader.load(stream);
            assertEquals(2, source.width()); assertEquals(1, source.voxels().size());
            var nativeModel = MinecraftItemModelConverter.convert(source);
            assertArrayEquals(new int[] {0, 0, 0}, nativeModel.elements().getFirst().from());
            assertArrayEquals(new int[] {8, 8, 8}, nativeModel.elements().getFirst().to());
        }
    }
    @Test void rejectsDimensionsOutsideThePublishedConstraint() throws Exception {
        byte[] valid = getClass().getResourceAsStream("/assets/magicavoxelloader/vox/sample.vox").readAllBytes();
        // SIZE width begins after the VOX header, MAIN header, and SIZE header.
        valid[32] = 17;
        assertThrows(VoxFormatException.class, () -> VoxAssetLoader.load(new ByteArrayInputStream(valid)));
    }
    @Test void rejectsNonVoxInput() { assertThrows(VoxFormatException.class, () -> VoxAssetLoader.load(new ByteArrayInputStream(new byte[8]))); }
}

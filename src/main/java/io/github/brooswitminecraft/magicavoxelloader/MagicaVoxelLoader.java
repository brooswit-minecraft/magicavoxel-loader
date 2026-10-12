package io.github.brooswitminecraft.magicavoxelloader;

import io.github.brooswitminecraft.magicavoxelloader.vox.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.IOException;
import java.io.InputStream;

/** Loads the packaged model during mod initialization; consumers can use the converted native model. */
@Mod(MagicaVoxelLoader.MOD_ID)
public final class MagicaVoxelLoader {
    public static final String MOD_ID = "magicavoxelloader";
    private static volatile MinecraftItemModel sampleItemModel;
    public MagicaVoxelLoader(IEventBus modBus) { modBus.addListener(this::loadPackagedVox); }
    private void loadPackagedVox(FMLCommonSetupEvent ignored) {
        try (InputStream stream = getClass().getResourceAsStream("/assets/" + MOD_ID + "/vox/sample.vox")) {
            if (stream == null) throw new IllegalStateException("Packaged sample.vox is missing");
            sampleItemModel = MinecraftItemModelConverter.convert(VoxAssetLoader.load(stream));
        } catch (IOException e) { throw new IllegalStateException("Unable to load packaged MagicaVoxel asset", e); }
    }
    public static MinecraftItemModel sampleItemModel() {
        if (sampleItemModel == null) throw new IllegalStateException("MagicaVoxel model requested before common setup");
        return sampleItemModel;
    }
}

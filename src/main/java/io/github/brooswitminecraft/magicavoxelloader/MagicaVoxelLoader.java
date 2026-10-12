package io.github.brooswitminecraft.magicavoxelloader;

import io.github.brooswitminecraft.magicavoxelloader.vox.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.Item;

import java.io.IOException;
import java.io.InputStream;

/** Loads the packaged model during mod initialization; consumers can use the converted native model. */
@Mod(MagicaVoxelLoader.MOD_ID)
public final class MagicaVoxelLoader {
    public static final String MOD_ID = "magicavoxelloader";
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    /** `/give @s magicavoxelloader:voxel_display` renders the packaged sample through the runtime renderer. */
    public static final DeferredItem<Item> VOXEL_DISPLAY = ITEMS.register("voxel_display", () -> new Item(new Item.Properties()));
    private static volatile MinecraftItemModel sampleItemModel;
    private static volatile VoxelModel sampleVoxelModel;
    public MagicaVoxelLoader(IEventBus modBus) { ITEMS.register(modBus); modBus.addListener(this::loadPackagedVox); }
    private void loadPackagedVox(FMLCommonSetupEvent ignored) {
        try (InputStream stream = getClass().getResourceAsStream("/assets/" + MOD_ID + "/vox/sample.vox")) {
            if (stream == null) throw new IllegalStateException("Packaged sample.vox is missing");
            sampleVoxelModel = VoxAssetLoader.load(stream);
            sampleItemModel = MinecraftItemModelConverter.convert(sampleVoxelModel);
        } catch (IOException e) { throw new IllegalStateException("Unable to load packaged MagicaVoxel asset", e); }
    }
    public static MinecraftItemModel sampleItemModel() {
        if (sampleItemModel == null) throw new IllegalStateException("MagicaVoxel model requested before common setup");
        return sampleItemModel;
    }
    public static VoxelModel sampleVoxelModel() {
        if (sampleVoxelModel == null) throw new IllegalStateException("MagicaVoxel model requested before common setup");
        return sampleVoxelModel;
    }
}

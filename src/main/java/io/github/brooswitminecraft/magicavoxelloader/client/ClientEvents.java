package io.github.brooswitminecraft.magicavoxelloader.client;

import io.github.brooswitminecraft.magicavoxelloader.MagicaVoxelLoader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

/** Client-only registration keeps the runtime .vox renderer out of dedicated-server classloading. */
@EventBusSubscriber(modid = MagicaVoxelLoader.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEvents {
    private ClientEvents() { }
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        VoxelItemRenderer renderer = new VoxelItemRenderer();
        event.registerItem(new IClientItemExtensions() {
            @Override public VoxelItemRenderer getCustomRenderer() { return renderer; }
        }, MagicaVoxelLoader.VOXEL_DISPLAY.get());
    }
}

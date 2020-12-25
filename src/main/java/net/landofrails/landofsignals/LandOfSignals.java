package net.landofrails.landofsignals;

import cam72cam.mod.ModCore;
import cam72cam.mod.ModEvent;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.render.BlockRender;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.render.block.*;
import net.landofrails.landofsignals.render.item.ObjItemRender;
import net.landofrails.landofsignals.tile.*;
import net.landofrails.landofsignals.utils.Static;

@net.minecraftforge.fml.common.Mod(modid = LandOfSignals.MODID, name = "LandOfSignals", version = "0.0.1", dependencies = "required-after:universalmodcore@[1.0,1.1)", acceptedMinecraftVersions = "[1.12,1.13)")
public class LandOfSignals extends ModCore.Mod {
    public static final String MODID = "landofsignals";

    static {
        try {
            ModCore.register(new net.landofrails.landofsignals.LandOfSignals());
        } catch (Exception e) {
            throw new RuntimeException("Could not load mod " + MODID, e);
        }
    }

    @Override
    public String modID() {
        return MODID;
    }

    @Override
    public void commonEvent(ModEvent event) {

        switch (event) {
            case START:
                ModCore.Mod.info("Start learning process...");
                break;
            case CONSTRUCT:
                LOSBlocks.register();
                LOSItems.register();
        }

    }

    @Override
    public void clientEvent(ModEvent event) {
        switch (event) {
            case CONSTRUCT:
                //Block
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_SO_12, TileSignalSO12Render::render, TileSignalSO12.class);
                BlockRender.register(LOSBlocks.BLOCK_SIGNAL_LEVER, TileSignalLeverRender::render, TileSignalLever.class);

                BlockRender.register(LOSBlocks.BLOCK_GROUND_VORSIGNAL, TileGroundRender::render, TileGround.class);
                BlockRender.register(LOSBlocks.BLOCK_MID_VORSIGNAL_MAST, TileMidRender::render, TileMid.class);
                BlockRender.register(LOSBlocks.BLOCK_TOP_VORSIGNAL_KOPF, TileTopRender::render, TileTop.class);

                //Items
                ItemRender.register(LOSItems.ITEM_SIGNALSO12, ObjItemRender.getModelFor(new Identifier(LandOfSignals.MODID, "models/block/so12/signalso12.obj"), new Vec3d(0.5, 0, 0.5), 2));
                ItemRender.register(LOSItems.ITEM_SIGNAL_LEVER, ObjItemRender.getModelFor(new Identifier(LandOfSignals.MODID, "models/block/signalslever/signalslever.obj"), new Vec3d(0.5, 0.6, 0.5), 1));

                ItemRender.register(LOSItems.ITEM_GROUND_VORSIGNAL, ObjItemRender.getModelFor(Static.listGroundModels.get("BLOCK_GROUND_VORSIGNAL")._1(), new Vec3d(0.5, -0.9, 0.5), 0.63f));
                ItemRender.register(LOSItems.ITEM_MID_VORSIGNAL_MAST, ObjItemRender.getModelFor(Static.listMidModels.get("BLOCK_MID_VORSIGNAL_MAST")._1(), new Vec3d(0.5, -0.9, 0.5), 0.63f));
                ItemRender.register(LOSItems.ITEM_TOP_VORSIGNAL_KOPF, ObjItemRender.getModelFor(Static.listTopModels.get("BLOCK_TOP_VORSIGNAL_KOPF")._1(), new Vec3d(0.5, -0.9, 0.5), 0.63f));
                break;
        }
    }

    @Override
    public void serverEvent(ModEvent event) {

    }
}

package net.landofrails.stellwand.content.blocks;

import cam72cam.mod.render.BlockRender;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.blocks.others.*;
import net.landofrails.stellwand.content.entities.rendering.*;
import net.landofrails.stellwand.content.entities.storage.*;

public class CustomBlocks {

    private CustomBlocks() {

    }

    public static final BlockFiller BLOCKFILLER = new BlockFiller();
    public static final BlockSender BLOCKSENDER = new BlockSender();
    public static final BlockSignal BLOCKSIGNAL = new BlockSignal();
    public static final BlockMultisignal BLOCKMULTISIGNAL = new BlockMultisignal();
    public static final BlockButtonReceiver BLOCKRECEIVER = new BlockButtonReceiver();

    public static void init() {
        // WICHTIG: Triggert das Laden der Blöcke.
    }

    public static void registerBlockRenderers() {
        Stellwand.debug("Registering blocks");
        BlockRender.register(BLOCKFILLER, BlockFillerRenderEntity::render, BlockFillerStorageEntity.class);
        BlockRender.register(BLOCKSIGNAL, BlockSignalRenderEntity::render,
                BlockSignalStorageEntity.class);
        BlockRender.register(BLOCKSENDER, BlockSenderRenderEntity::render,
                BlockSenderStorageEntity.class);
        BlockRender.register(BLOCKMULTISIGNAL, BlockMultisignalRenderEntity::render, BlockMultisignalStorageEntity.class);
        BlockRender.register(BLOCKRECEIVER, BlockButtonReceiverRenderEntity::render, BlockButtonReceiverStorageEntity.class);

    }

}

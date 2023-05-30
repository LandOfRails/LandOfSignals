package net.landofrails.landofsignals.packet;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LOSGuis;

public class GuiItemManipulatorToClient extends Packet {

    @TagField
    BlockEntity block;

    public GuiItemManipulatorToClient(){
    }

    public GuiItemManipulatorToClient(BlockEntity block){
        this.block = block;
    }

    @Override
    protected void handle() {
        LOSGuis.MANIPULATOR.open(getPlayer(), block.getPos());
    }

}

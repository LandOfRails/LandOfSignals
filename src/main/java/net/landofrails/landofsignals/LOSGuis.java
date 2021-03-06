package net.landofrails.landofsignals;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.gui.*;
import net.landofrails.landofsignals.tile.TileSignalBox;

public class LOSGuis {

    private LOSGuis() {

    }

    public static final GuiRegistry.GUI TICKET_MACHINE_DB = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "TICKET_MACHINE_DB"), GuiTicketMachine::new);
    public static final GuiRegistry.BlockGUI SIGNAL_BOX = GuiRegistry.registerBlock(TileSignalBox.class, GuiSignalPartBox::new);
    public static final GuiRegistry.BlockGUI SIGNAL_ANIMATED_BOX = GuiRegistry.registerBlock(TileSignalBox.class, GuiSignalPartAnimatedBox::new);
    public static final GuiRegistry.GUI SIGNAL_SELECTOR = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "SIGNAL_SELECTOR"), GuiSelectSignal::new);
    public static final GuiRegistry.BlockGUI MANIPULATOR = GuiRegistry.registerBlock(BlockEntity.class, GuiManipualtor::new);

    public static void register() {
        // loads static classes and ctrs
    }

}

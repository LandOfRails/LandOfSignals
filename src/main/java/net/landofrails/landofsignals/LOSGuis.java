package net.landofrails.landofsignals;

import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.gui.GuiSignalBox;
import net.landofrails.landofsignals.gui.GuiTicketMachine;
import net.landofrails.landofsignals.tile.TileSignalBox;

public class LOSGuis {

    public static final GuiRegistry.GUI TICKET_MACHINE_DB = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "TICKET_MACHINE_DB"), GuiTicketMachine::new);
    public static final GuiRegistry.BlockGUI SIGNAL_BOX = GuiRegistry.registerBlock(TileSignalBox.class, GuiSignalBox::new);

    public static void register() {
        // loads static classes and ctrs
    }

}

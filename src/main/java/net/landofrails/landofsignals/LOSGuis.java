package net.landofrails.landofsignals;

import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.gui.GuiTicketMachine;

public class LOSGuis {

    public static final GuiRegistry.GUI TICKET_MACHINE_DB = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "TICKET_MACHINE_DB"), GuiTicketMachine::new);

    public static void register() {
        // loads static classes and ctrs
    }

}

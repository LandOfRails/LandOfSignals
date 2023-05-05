package net.landofrails.landofsignals;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.config.ConfigGui;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.configs.LandOfSignalsConfig;
import net.landofrails.landofsignals.gui.*;

public class LOSGuis {

    private LOSGuis() {

    }

    public static final GuiRegistry.GUI TICKET_MACHINE_DB = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "TICKET_MACHINE_DB"), GuiTicketMachine::new);
    // INFO: Replace GuiRegistry.BlockGUI with GuiRegistry.GUI to be able to open both on the same block.
    public static final GuiRegistry.GUI SIGNAL_BOX_COMPLEX_SIGNAL = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "SIGNAL_BOX_COMPLEX_SIGNAL"), GuiSignalBoxComplexSignal::new);
    public static final GuiRegistry.GUI SIGNAL_BOX_SIGNAL_PART = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "SIGNAL_BOX_SIGNAL_PART"), GuiSignalBoxSignalPart::new);
    public static final GuiRegistry.BlockGUI SIGNAL_ANIMATED_BOX = null;
    public static final GuiRegistry.BlockGUI MANIPULATOR = GuiRegistry.registerBlock(BlockEntity.class, GuiManipualtor::new);
    public static final GuiRegistry.GUI SIGN_TEXTEDITOR = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "SIGN_TEXTEDTIOR"), GuiSignTextEditor::new);
    public static final GuiRegistry.GUI SIGNAL_PRIORITIZATION = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "SIGNAL_PRIORITIZATION"), GuiSignalPrioritization::new);

    public static final GuiRegistry.GUI CONFIG = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "config"), () -> new ConfigGui(LandOfSignalsConfig.class));

    public static void register() {
        // loads static classes and ctrs
    }

}

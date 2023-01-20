package net.landofrails.landofsignals;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.creator.gui.*;
import net.landofrails.landofsignals.gui.*;
import net.landofrails.landofsignals.tile.TileSignalBox;

public class LOSGuis {

    private LOSGuis() {

    }

    public static final GuiRegistry.GUI TICKET_MACHINE_DB = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "TICKET_MACHINE_DB"), GuiTicketMachine::new);
    // INFO: Replace GuiRegistry.BlockGUI with GuiRegistry.GUI to be able to open both on the same block.
    public static final GuiRegistry.BlockGUI SIGNAL_BOX = GuiRegistry.registerBlock(TileSignalBox.class, GuiSignalPartBox::new);
    public static final GuiRegistry.BlockGUI SIGNAL_ANIMATED_BOX = null;
    public static final GuiRegistry.GUI LEGACYMODE_SELECTOR = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "LEGACYMODE_SELECTER"), GuiLegacyModeSelection::new);
    public static final GuiRegistry.BlockGUI MANIPULATOR = GuiRegistry.registerBlock(BlockEntity.class, GuiManipualtor::new);
    public static final GuiRegistry.GUI SIGN_TEXTEDITOR = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "SIGN_TEXTEDTIOR"), GuiSignTextEditor::new);

    public static final GuiRegistry.GUI CREATOR_SELECT_CONTENTPACK = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "CREATOR_SELECT_CONTENTPACK"), GuiSelectContentpack::new);
    public static final GuiRegistry.GUI CREATOR_SELECT_TYPE = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "CREATOR_SELECT_TYPE"), GuiSelectType::new);
    public static final GuiRegistry.GUI CREATOR_ENTRY_OVERVIEW = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "CREATOR_ENTRY_OVERVIEW"), GuiEntryOverview::new);
    public static final GuiRegistry.GUI CREATOR_NAME_ID = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "CREATOR_NAME_ID"), GuiNameId::new);
    public static final GuiRegistry.GUI CREATOR_GROUP_OVERVIEW = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "CREATOR_GROUP_OVERVIEW"), GuiGroupOverview::new);
    public static final GuiRegistry.GUI CREATOR_STATE_OVERVIEW = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "CREATOR_STATE_OVERVIEW"), GuiStateOverview::new);
    public static final GuiRegistry.GUI CREATOR_NEWSTATE = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "CREATOR_NEWSTATE"), GuiNewState::new);
    public static final GuiRegistry.GUI CREATOR_MODIFY_STATE = GuiRegistry.register(new Identifier(LandOfSignals.MODID, "CREATOR_MODIFY_STATE"), GuiModifyState::new);

    public static void register() {
        // loads static classes and ctrs
    }

}

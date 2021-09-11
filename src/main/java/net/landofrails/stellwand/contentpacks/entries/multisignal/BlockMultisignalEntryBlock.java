package net.landofrails.stellwand.contentpacks.entries.multisignal;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;

import java.util.LinkedList;
import java.util.Map;

public class BlockMultisignalEntryBlock extends ContentPackEntryBlock {

    /**
     * @formatter:off
     * {
     *     "modesList": {
     *         "Left Signal": {
     *             "Green": "greenLeftCube",
     *             "Red": "redLeftCube"
     *         },
     *         "Right Signal": {
     *             "Green": "greenRightCube",
     *             "Red": "redRightCube"
     *         }
     *     }
     * }
     * @formatter:on
     */
    private Map<String, Map<String, String>> modesList;
    // List of modeGroups that once initialized should stay this way
    private transient LinkedList<String> modeGroups;

    public BlockMultisignalEntryBlock() {

    }

    public BlockMultisignalEntryBlock(float[] rotation, float[] translation, Map<String, Map<String, String>> modesList) {
        super(rotation, translation);
        this.modesList = modesList;
    }

    public Map<String, Map<String, String>> getModesList() {
        return modesList;
    }

    public LinkedList<String> getModeGroups() {
        if (modeGroups == null)
            modeGroups = new LinkedList<>(modesList.keySet());
        return modeGroups;
    }

}

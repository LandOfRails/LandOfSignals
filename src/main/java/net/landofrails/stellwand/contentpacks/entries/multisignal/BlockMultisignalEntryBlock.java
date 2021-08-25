package net.landofrails.stellwand.contentpacks.entries.multisignal;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;

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

    public BlockMultisignalEntryBlock() {

    }

    public BlockMultisignalEntryBlock(float[] rotation, float[] translation, Map<String, Map<String, String>> modesList) {
        super(rotation, translation);
        this.modesList = modesList;
    }

    public Map<String, Map<String, String>> getModesList() {
        return modesList;
    }

}

package net.landofrails.stellwand.contentpacks.entries.multisignal;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;

import java.util.List;
import java.util.Map;

public class BlockMultisignalEntryBlock extends ContentPackEntryBlock {

    private List<Map<String, String>> modeList;

    public BlockMultisignalEntryBlock() {

    }

    public BlockMultisignalEntryBlock(float[] rotation, float[] translation, List<Map<String, String>> modeList) {
        super(rotation, translation);
        this.modeList = modeList;
    }

    public List<Map<String, String>> getModeList() {
        return modeList;
    }

}

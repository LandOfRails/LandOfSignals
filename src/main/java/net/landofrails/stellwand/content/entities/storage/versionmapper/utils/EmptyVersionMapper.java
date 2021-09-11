package net.landofrails.stellwand.content.entities.storage.versionmapper.utils;

import cam72cam.mod.serialization.TagCompound;
import net.landofrails.stellwand.content.entities.storage.versionmapper.VersionMapper;

public class EmptyVersionMapper extends VersionMapper {

    private int newVersion;

    public EmptyVersionMapper(int newVersion) {
        this.newVersion = newVersion;
    }

    @Override
    public int checkTagCompound(TagCompound nbt) {
        nbt.setInteger("version", newVersion);
        return newVersion;
    }
}
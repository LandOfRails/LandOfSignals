package net.landofrails.stellwand.content.entities.storage.versionmapper.mapper;

import cam72cam.mod.serialization.TagCompound;
import net.landofrails.stellwand.content.entities.storage.versionmapper.VersionMapper;

public class SenderMapperV1toV2 extends VersionMapper {

    @Override
    public int checkTagCompound(TagCompound nbt) {
        // Constants
        final int newVersion = 2;
        //

        String signalGroup = null;
        nbt.setString("signalGroup", signalGroup);

        nbt.setInteger("version", newVersion);
        return newVersion;
    }
}

package net.landofrails.stellwand.content.entities.storage.versionmapper;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.versionmapper.mapper.SenderMapperV1toV2;
import net.landofrails.stellwand.content.entities.storage.versionmapper.utils.VMContainer;

import java.util.LinkedList;
import java.util.List;

public abstract class VersionMapper {

    private static final List<VMContainer<?, ?>> versionMapperList = new LinkedList<>();

    /**
     * @param nbt (Current data)
     * @return new version
     */
    public abstract int checkTagCompound(TagCompound nbt);

    @SuppressWarnings("java:S1192")
    public static void checkMap(Class<? extends BlockEntity> blockEntity, TagCompound nbt) {
        if (nbt == null)
            return;

        if (versionMapperList.isEmpty()) {
            // Info: Sort from lowest to highest version
            versionMapperList.add(VMContainer.create(BlockSenderStorageEntity.class, new SenderMapperV1toV2(), 1));
        }

        int version = nbt.hasKey("version") ? nbt.getInteger("version") : 1;
        for (VMContainer<? extends BlockEntity, ? extends VersionMapper> vmContainer : versionMapperList) {
            if (vmContainer.isResponsible(blockEntity, version)) {
                version = vmContainer.getVersionMapper().checkTagCompound(nbt);
            }
        }
    }

}

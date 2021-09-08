package net.landofrails.stellwand.content.entities.storage.versionmapper.utils;

import cam72cam.mod.block.BlockEntity;
import net.landofrails.stellwand.content.entities.storage.versionmapper.VersionMapper;

public class VMContainer<A extends BlockEntity, B extends VersionMapper> {

    private Class<A> blockEntityClass;
    private B versionMapper;
    private int fromVersion;

    private VMContainer(Class<A> blockEntityClass, B versionMapper, int fromVersion) {
        this.blockEntityClass = blockEntityClass;
        this.versionMapper = versionMapper;
        this.fromVersion = fromVersion;
    }

    public static <A extends BlockEntity, B extends VersionMapper> VMContainer<A, B> create(Class<A> blockEntityClass, B versionMapper, int fromVersion) {
        return new VMContainer<>(blockEntityClass, versionMapper, fromVersion);
    }

    public B getVersionMapper() {
        return versionMapper;
    }

    public boolean isResponsible(Class<? extends BlockEntity> blockEntityClass, int fromVersion) {
        return this.blockEntityClass.equals(blockEntityClass) && this.fromVersion == fromVersion;
    }

}

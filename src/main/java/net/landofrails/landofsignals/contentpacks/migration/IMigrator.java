package net.landofrails.landofsignals.contentpacks.migration;

import cam72cam.mod.serialization.TagCompound;

public interface IMigrator {

    public boolean shouldBeMigrated(TagCompound tagCompound);

    public void migrate(TagCompound nbt);

}

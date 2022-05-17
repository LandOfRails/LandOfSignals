package net.landofrails.landofsignals.contentpacks.migration;

import cam72cam.mod.serialization.TagCompound;
import net.landofrails.landofsignals.configs.LegacyMode;

public class TileSignalPartMigratorV1 implements IMigrator {

    private static TileSignalPartMigratorV1 tileSignalPartMigratorV1;

    private TileSignalPartMigratorV1() {

    }

    public static TileSignalPartMigratorV1 getInstance() {
        if (tileSignalPartMigratorV1 == null)
            tileSignalPartMigratorV1 = new TileSignalPartMigratorV1();
        return tileSignalPartMigratorV1;
    }

    @Override
    public boolean shouldBeMigrated(TagCompound nbt) {
        return nbt.hasKey("version") && nbt.getInteger("version") == 1;
    }

    @Override
    public void migrate(TagCompound nbt) {
        nbt.setInteger("version", 2);
        nbt.setEnum("legacyMode", LegacyMode.ON);

        // FIXME Can we fix it? Yes, we- wait.. I'm actually unsure.
        // TODO Recover this and transform to current implementation, use sender (0,0,0)
        // nbt.remove("texturePath");
    }
}

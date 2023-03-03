package net.landofrails.landofsignals.contentpacks.migration;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.configs.LegacyMode;
import net.landofrails.landofsignals.serialization.MapVec3iStringStringMapper;
import net.landofrails.landofsignals.utils.Static;

import java.util.*;
import java.util.function.Predicate;

public class TileSignalPartMigratorV1 implements IMigrator {

    private static final String VERSION_KEY = "version";

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
        return nbt.hasKey(VERSION_KEY) && nbt.getInteger(VERSION_KEY) == 1;
    }

    @Override
    public void migrate(TagCompound nbt) {

        String id = nbt.getString("id");
        boolean hasId = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().containsKey(id);

        if (!hasId) {
            id = Static.MISSING;
            nbt.setString("id", id);
        }

        if (LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(id)) {
            nbt.setEnum("legacyMode", LegacyMode.ON);

            String texturePath = nbt.getString("texturePath");
            Map.Entry<String, ContentPackSignalGroup> firstGroup = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(id).getSignals().entrySet().iterator().next();
            Predicate<Map<String, ContentPackModel[]>> containsTexture = map -> map.values().stream().flatMap(Arrays::stream).map(ContentPackModel::getTextures).anyMatch(tex -> Objects.equals(tex, texturePath));
            Optional<String> stateId = firstGroup.getValue().getStates().entrySet().stream().filter(stateEntry -> containsTexture.test(stateEntry.getValue().getModels())).map(Map.Entry::getKey).findFirst();
            if (stateId.isPresent()) {
                Map.Entry<String, String> groupStateEntry = new AbstractMap.SimpleEntry<>(firstGroup.getKey(), stateId.get());
                Map<Vec3i, Map.Entry<String, String>> senderSignalGroupStates = Collections.singletonMap(Vec3i.ZERO, groupStateEntry);
                MapVec3iStringStringMapper.groupIdGroupStateMapToNbt(nbt, "senderSignalGroupStates", senderSignalGroupStates);
            } else {
                LandOfSignals.warn("Couldn't migrate signal %s with texturePath %s! State couldn't be migrated.", id, texturePath);
            }

        }
        nbt.setInteger(VERSION_KEY, 2);
    }
}

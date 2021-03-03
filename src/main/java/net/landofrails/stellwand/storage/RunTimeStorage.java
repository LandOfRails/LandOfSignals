package net.landofrails.stellwand.storage;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;

public class RunTimeStorage {

	private static HashMap<UUID, BlockSignalStorageEntity> signals;

	public static void register(@Nonnull UUID uuid,
			BlockSignalStorageEntity signal) {
		signals.put(uuid, signal);
	}

	@Nullable
	public static BlockSignalStorageEntity get(@Nonnull UUID uuid) {
		return signals.get(uuid);
	}

}

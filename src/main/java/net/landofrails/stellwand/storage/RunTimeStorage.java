package net.landofrails.stellwand.storage;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cam72cam.mod.math.Vec3i;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;

public class RunTimeStorage {

	private RunTimeStorage() {

	}

	private static Map<Vec3i, BlockSenderStorageEntity> senders = new HashMap<>();
	private static Map<Vec3i, BlockSignalStorageEntity> signals = new HashMap<>();

	public static void register(@Nonnull Vec3i senderId,
			@Nonnull BlockSenderStorageEntity sender) {
		senders.put(senderId, sender);
	}

	public static void register(@Nonnull Vec3i signalId,
			@Nonnull BlockSignalStorageEntity signal) {
		signals.put(signalId, signal);
	}

	@Nullable
	public static BlockSenderStorageEntity getSender(@Nonnull Vec3i senderId) {
		return senders.get(senderId);
	}

	@Nullable
	public static BlockSignalStorageEntity getSignal(@Nonnull Vec3i uuid) {
		return signals.get(uuid);
	}

	public static void removeSender(@Nonnull Vec3i senderId) {
		senders.remove(senderId);
	}

	public static void removeSignal(@Nonnull Vec3i signalId) {
		signals.remove(signalId);
	}

}

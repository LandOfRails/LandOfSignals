package net.landofrails.stellwand.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;

public class RunTimeStorage {

	private RunTimeStorage() {

	}

	private static Map<UUID, BlockSenderStorageEntity> senders = new HashMap<>();
	private static Map<UUID, BlockSignalStorageEntity> signals = new HashMap<>();

	public static void register(@Nonnull UUID senderId,
			@Nonnull BlockSenderStorageEntity sender) {
		senders.put(senderId, sender);
	}

	public static void register(@Nonnull UUID signalId,
			@Nonnull BlockSignalStorageEntity signal) {
		signals.put(signalId, signal);
	}

	@Nullable
	public static BlockSenderStorageEntity getSender(@Nonnull UUID senderId) {
		return senders.get(senderId);
	}

	@Nullable
	public static BlockSignalStorageEntity getSignal(@Nonnull UUID uuid) {
		return signals.get(uuid);
	}

	public static void removeSender(@Nonnull UUID senderId) {
		senders.remove(senderId);
	}

	public static void removeSignal(@Nonnull UUID signalId) {
		signals.remove(signalId);
	}

}

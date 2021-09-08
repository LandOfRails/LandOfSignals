package net.landofrails.stellwand.utils.compact;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.world.World;
import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.entities.storage.BlockMultisignalStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.contentpacks.types.EntryType;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@SuppressWarnings({"java:S112", "java:S1117"})
public class SignalContainer<T extends BlockEntity> {

    private final T signal;

    private SignalContainer(T signal) {
        this.signal = signal;
        if (!is(BlockSignalStorageEntity.class) && !is(BlockMultisignalStorageEntity.class)) {
            String clazz = "null";
            if (signal != null)
                clazz = signal.getClass().getName();
            throw new RuntimeException(String.format("Illegal type for signal! Class: %s", clazz));
        }
    }

    public static <T extends BlockEntity> SignalContainer<T> of(T signal) {
        return new SignalContainer<>(signal);
    }

    public static SignalContainer<BlockEntity> of(World world, Vec3i signalPos) {
        if (world.isBlock(signalPos, CustomBlocks.BLOCKSIGNAL)) {
            return of(world.getBlockEntity(signalPos, BlockSignalStorageEntity.class));
        } else if (world.isBlock(signalPos, CustomBlocks.BLOCKMULTISIGNAL)) {
            return of(world.getBlockEntity(signalPos, BlockMultisignalStorageEntity.class));
        } else {
            throw new RuntimeException(String.format("Illegal type for signal! World: %s, Position: %s", world, signalPos.toString()));
        }
    }

    public static boolean isSignal(World world, Vec3i signalPos) {
        return world.isBlock(signalPos, CustomBlocks.BLOCKSIGNAL) || world.isBlock(signalPos, CustomBlocks.BLOCKMULTISIGNAL);
    }

    public boolean is(Class<?> clazz) {
        return clazz.equals(getSignalClass());
    }

    public T getRawInstance() {
        return signal;
    }

    public <S extends BlockEntity> S getAs(Class<S> clazz) {
        return clazz.cast(signal);
    }

    @SuppressWarnings("unchecked")
    public Class<T> getSignalClass() {
        try {
            return (Class<T>) signal.getClass();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasSenderModesFrom(Vec3i senderPos) {
        if (is(BlockSignalStorageEntity.class)) {
            BlockSignalStorageEntity signal = getAs(BlockSignalStorageEntity.class);
            return signal.senderModes.containsKey(senderPos);
        } else {
            BlockMultisignalStorageEntity signal = getAs(BlockMultisignalStorageEntity.class);
            return signal.senderModeslist.containsKey(senderPos);
        }
    }


    public void removeSenderModesFrom(Vec3i senderPos) {
        if (is(BlockSignalStorageEntity.class)) {
            BlockSignalStorageEntity signal = getAs(BlockSignalStorageEntity.class);
            signal.senderModes.remove(senderPos);
        } else {
            BlockMultisignalStorageEntity signal = getAs(BlockMultisignalStorageEntity.class);
            signal.senderModeslist.remove(senderPos);
        }
    }

    public void updateSignalModes() {
        if (is(BlockSignalStorageEntity.class)) {
            BlockSignalStorageEntity signal = getAs(BlockSignalStorageEntity.class);
            signal.updateSignalMode();
        } else {
            BlockMultisignalStorageEntity signal = getAs(BlockMultisignalStorageEntity.class);
            signal.updateSignalModes();
        }
    }

    public Map<Vec3i, Map<String, String>> getSenderModes() {
        Map<Vec3i, Map<String, String>> senderModes = new HashMap<>();
        if (is(BlockSignalStorageEntity.class)) {
            BlockSignalStorageEntity signal = getAs(BlockSignalStorageEntity.class);
            signal.senderModes.forEach((senderPos, signalName) -> senderModes.put(senderPos, Collections.singletonMap(null, signalName)));
        } else {
            BlockMultisignalStorageEntity signal = getAs(BlockMultisignalStorageEntity.class);
            senderModes.putAll(signal.senderModeslist);
        }
        return senderModes;
    }

    public void setSenderModes(Map<Vec3i, Map<String, String>> modes) {
        if (is(BlockSignalStorageEntity.class)) {
            BlockSignalStorageEntity signal = getAs(BlockSignalStorageEntity.class);
            Map<Vec3i, String> senderModeMap = new HashMap<>();
            modes.forEach((senderPos, signalGroups) -> senderModeMap.put(senderPos, signalGroups.get(null)));
            signal.senderModes = senderModeMap;
        } else {
            BlockMultisignalStorageEntity signal = getAs(BlockMultisignalStorageEntity.class);
            signal.senderModeslist = modes;
        }
    }

    public EntryType getEntryType() {
        if (is(BlockSignalStorageEntity.class)) {
            return EntryType.BLOCKSIGNAL;
        } else if (is(BlockMultisignalStorageEntity.class)) {
            return EntryType.BLOCKMULTISIGNAL;
        }
        return null;
    }

    @SuppressWarnings("java:S1192")
    public TagCompound toTagCompound() {
        TagCompound tag = new TagCompound();
        tag.setEnum("type", getEntryType());
        tag.setTile("tileEntity", getAs(getSignalClass()));
        tag.setBoolean("isClient", getRawInstance().getWorld().isClient);
        return tag;
    }

    public static SignalContainer<BlockEntity> of(TagCompound tag) {
        EntryType type = tag.getEnum("type", EntryType.class);
        if (type.equals(EntryType.BLOCKSIGNAL)) {
            BlockSignalStorageEntity entity = tag.getTile("tileEntity", tag.getBoolean("isClient"));
            return of(entity);
        } else if (type.equals(EntryType.BLOCKMULTISIGNAL)) {
            BlockMultisignalStorageEntity entity = tag.getTile("tileEntity", tag.getBoolean("isClient"));
            return of(entity);
        }
        return null;
    }

    public void addSenderModesFrom(Vec3i pos, String groupId, String senderMode) {
        if (is(BlockSignalStorageEntity.class)) {
            BlockSignalStorageEntity signal = getAs(BlockSignalStorageEntity.class);
            signal.senderModes.put(pos, senderMode);
        } else {
            BlockMultisignalStorageEntity signal = getAs(BlockMultisignalStorageEntity.class);
            signal.senderModeslist.put(pos, Collections.singletonMap(groupId, senderMode));
        }
    }

    public Map<String, Map<String, String>> getPossibleModes() {
        if (is(BlockSignalStorageEntity.class)) {
            BlockSignalStorageEntity signal = getAs(BlockSignalStorageEntity.class);
            return Collections.singletonMap(null, signal.getPossibleModes());
        } else {
            BlockMultisignalStorageEntity signal = getAs(BlockMultisignalStorageEntity.class);
            return signal.getPossibleModes();
        }
    }

    public String getContentPackBlockId() {
        if (is(BlockSignalStorageEntity.class)) {
            BlockSignalStorageEntity signal = getAs(BlockSignalStorageEntity.class);
            return signal.contentPackBlockId;
        } else {
            BlockMultisignalStorageEntity signal = getAs(BlockMultisignalStorageEntity.class);
            return signal.contentPackBlockId;
        }
    }

    public boolean isCompatible(SignalContainer<?> otherSignal) {
        if (this.signal == null || otherSignal == null)
            return false;

        if (!signal.getClass().equals(otherSignal.getRawInstance().getClass()))
            return false;

        return getContentPackBlockId().equals(otherSignal.getContentPackBlockId());
    }

    public Vec3i getPos() {
        if (is(BlockSignalStorageEntity.class)) {
            BlockSignalStorageEntity signal = getAs(BlockSignalStorageEntity.class);
            return signal.getPos();
        } else {
            BlockMultisignalStorageEntity signal = getAs(BlockMultisignalStorageEntity.class);
            return signal.getPos();
        }
    }

    public CustomItem getCustomItem() {
        if (is(BlockSignalStorageEntity.class)) {
            return CustomItems.ITEMBLOCKSIGNAL;
        } else {
            return CustomItems.ITEMBLOCKMULTISIGNAL;
        }
    }

    @SuppressWarnings("java:S1319")
    public LinkedList<String> getModeGroups() {
        if (is(BlockSignalStorageEntity.class)) {
            LinkedList<String> modeGroups = new LinkedList<>();
            modeGroups.add(null);
            return modeGroups;
        } else {
            BlockMultisignalStorageEntity signal = getAs(BlockMultisignalStorageEntity.class);
            return signal.getModeGroups();
        }
    }
}

package net.landofrails.stellwand.content.items.connector;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.world.World;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.utils.compact.SignalContainer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public abstract class AItemConnector {

    private static final Map<Class<? extends AItemConnector>, BiPredicate<World, Vec3i>> CONNECTORS = new HashMap<>();

    private static final String KEY_ISCLIENT = "isClient";
    private static final String KEY_WORLD = "world";
    private static final String KEY_POS = "pos";
    private static final String KEY_CLASSNAME = "className";

    /**
     * Used by connectors to register class and bipredicate
     *
     * @param connector     Connector's class
     * @param isConnectable Method of identifying the correct connector
     * @param <T>           Class extending AItemConnector.class
     */
    protected static <T extends AItemConnector> void registerConnector(Class<T> connector, BiPredicate<World, Vec3i> isConnectable) {
        CONNECTORS.put(connector, isConnectable);
    }

    /**
     * Called by commonEvent
     */
    public static void registerConnectors() {
        ItemConnectorSignal.register();
        ItemConnectorSender.register();
    }

    /**
     * Returns true if itemstack contains a connector
     *
     * @param itemStack The itemstack
     * @return True if it contains a connector
     */
    public static boolean hasConnectorData(ItemStack itemStack) {
        if (itemStack.is(CustomItems.ITEMCONNECTOR2) || itemStack.is(CustomItems.ITEMCONNECTOR3)) {
            TagCompound tag = itemStack.getTagCompound();
            return tag.hasKey(KEY_WORLD) && tag.hasKey(KEY_POS) && tag.hasKey(KEY_CLASSNAME);
        }
        return false;
    }

    /**
     * Returns true if a connector is suited for the position
     *
     * @param world World
     * @param pos   Position
     * @return True if a suitable connector exists
     */
    public static boolean suitableConnectorExists(World world, Vec3i pos) {
        assert !CONNECTORS.isEmpty();
        return CONNECTORS.values().stream().anyMatch(c -> c.test(world, pos));
    }

    /**
     * Returns new instance of a connector if a suitable one exists
     *
     * @param world World
     * @param pos   Position
     * @return Suitable connector, may be null
     */
    public static AItemConnector getConnector(World world, Vec3i pos) {
        Class<? extends AItemConnector> clazz = getImplementor(world, pos);

        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Returns instance of a connector if one exists
     *
     * @param itemStack ItemStack
     * @return Suitable connector, may be null
     */
    public static AItemConnector getConnector(ItemStack itemStack) {
        Class<? extends AItemConnector> clazz = getImplementor(itemStack);

        if (clazz != null) {
            try {
                TagCompound tag = itemStack.getTagCompound();
                return clazz.getDeclaredConstructor(TagCompound.class).newInstance(tag);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Returns class of a connector if a suitable one exists
     *
     * @param world World
     * @param pos   Position
     * @return Class of the suitable connector, may be null
     */
    public static Class<? extends AItemConnector> getImplementor(World world, Vec3i pos) {
        return CONNECTORS.entrySet().stream().filter(es -> es.getValue().test(world, pos)).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    /**
     * Return class of connector if a suitable one exists
     *
     * @param itemStack ItemStack
     * @return Class of the connector in the itemstack, may be null
     */
    public static Class<? extends AItemConnector> getImplementor(ItemStack itemStack) {
        if (itemStack != null) {
            TagCompound tag = itemStack.getTagCompound();
            if (tag.hasKey(KEY_CLASSNAME)) {
                try {
                    Class<?> aClass = Class.forName(tag.getString(KEY_CLASSNAME));
                    return aClass.asSubclass(AItemConnector.class);
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Returns a new SignalContainer if a signal exists at the give position
     *
     * @param world World
     * @param pos   Position
     * @return The SignalContainer for this position, may be null
     */
    protected static SignalContainer<?> getSignal(World world, Vec3i pos) {
        world.keepLoaded(pos);
        boolean isSignal = SignalContainer.isSignal(world, pos);
        if (!isSignal)
            return null;
        return SignalContainer.of(world, pos);
    }

    /**
     * Returns a BlockSenderStorageEntity if a signal exists at the give position
     *
     * @param world World
     * @param pos   Position
     * @return Blocksender
     */
    protected static BlockSenderStorageEntity getSender(World world, Vec3i pos) {
        world.keepLoaded(pos);
        return world.getBlockEntity(pos, BlockSenderStorageEntity.class);
    }

    // Connector

    protected AItemConnector() {

    }

    protected AItemConnector(TagCompound tag) {
        if (tag != null && !tag.isEmpty()) {
            Boolean isClient = tag.getBoolean(KEY_ISCLIENT);
            this.startWorld = tag.getWorld(KEY_WORLD, isClient);
            this.startPos = tag.getVec3i(KEY_POS);
        }
    }

    private World startWorld = null;
    private Vec3i startPos = null;

    public World getStartWorld() {
        return startWorld;
    }

    public void setStartWorld(World startWorld) {
        this.startWorld = startWorld;
    }

    public Vec3i getStartPos() {
        return startPos;
    }

    public void setStartPos(Vec3i startPos) {
        this.startPos = startPos;
    }

    public TagCompound toTag() {
        return worldPosTag(startWorld, startPos).setString(KEY_CLASSNAME, getImplementingClass().getName());
    }

    protected static TagCompound worldPosTag(World world, Vec3i pos) {
        TagCompound tag = new TagCompound();
        tag.setBoolean(KEY_ISCLIENT, world.isClient);
        tag.setWorld(KEY_WORLD, world);
        tag.setVec3i(KEY_POS, pos);
        return tag;
    }

    public abstract boolean shouldOverrideConnector(World world, Vec3i pos);

    protected abstract boolean connect(World world, Vec3i pos, Player player, Player.Hand hand);

    protected abstract boolean canConnect(World world, Vec3i pos);

    protected abstract Class<? extends AItemConnector> getImplementingClass();

}

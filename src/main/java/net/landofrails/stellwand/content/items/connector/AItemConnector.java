package net.landofrails.stellwand.content.items.connector;

import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.world.World;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public abstract class AItemConnector {

    private static final Map<Class<? extends AItemConnector>, BiPredicate<World, Vec3i>> CONNECTORS = new HashMap<>();

    private static final long serialVersionUID = -4963471115414960689L;
    private static final String KEY_ISCLIENT = "isClient";
    private static final String KEY_WORLD = "world";
    private static final String KEY_POS = "pos";

    public static <T extends AItemConnector> void registerConnector(Class<T> connector, BiPredicate<World, Vec3i> isConnectable) {
        CONNECTORS.put(connector, isConnectable);
    }

    public static boolean suitableConnectorExists(World world, Vec3i pos) {
        return CONNECTORS.values().stream().anyMatch(c -> c.test(world, pos));
    }

    public static AItemConnector getConnector(World world, Vec3i pos) {
        Class<? extends AItemConnector> clazz = getImplementor(world, pos);

        if (clazz != null) {
            try {
                TagCompound tag = worldPosTag(world, pos);
                return clazz.getDeclaredConstructor(TagCompound.class).newInstance(tag);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Class<? extends AItemConnector> getImplementor(World world, Vec3i pos) {
        return CONNECTORS.entrySet().stream().filter(es -> es.getValue().test(world, pos)).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    // Connector

    protected AItemConnector() {

    }

    protected AItemConnector(TagCompound tag) {
        if (tag != null && !tag.isEmpty()) {
            Boolean isClient = tag.getBoolean(KEY_ISCLIENT);
            this.world = tag.getWorld(KEY_WORLD, isClient);
            this.pos = tag.getVec3i(KEY_POS);
        }
    }

    private World world;
    private Vec3i pos;

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Vec3i getPos() {
        return pos;
    }

    public void setPos(Vec3i pos) {
        this.pos = pos;
    }

    public TagCompound toTag() {
        return worldPosTag(world, pos);
    }

    protected static TagCompound worldPosTag(World world, Vec3i pos) {
        TagCompound tag = new TagCompound();
        tag.setBoolean(KEY_ISCLIENT, world.isClient);
        tag.setWorld(KEY_WORLD, world);
        tag.setVec3i(KEY_POS, pos);
        return tag;
    }

    protected abstract boolean connect(World world, Vec3i pos, Player player, Player.Hand hand);

}

package net.landofrails.stellwand.utils;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.world.World;
import net.landofrails.stellwand.content.entities.storage.BlockFillerStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockMultisignalStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;

import java.util.*;

public class StellwandUtils {

    private StellwandUtils() {

    }

    public static Set<Class<?>> getAllExtendedOrImplementedTypesRecursively(Class<?> clazz) {
        List<Class<?>> res = new ArrayList<>();

        do {
            res.add(clazz);

            // First, add all the interfaces implemented by this class
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                res.addAll(Arrays.asList(interfaces));

                for (Class<?> interfaze : interfaces) {
                    res.addAll(getAllExtendedOrImplementedTypesRecursively(interfaze));
                }
            }

            // Add the super class
            Class<?> superClass = clazz.getSuperclass();

            // Interfaces does not have java,lang.Object as superclass, they
            // have null, so break the cycle and return
            if (superClass == null) {
                break;
            }

            // Now inspect the superclass
            clazz = superClass;

            if ("java.lang.Object".equals(clazz.getCanonicalName()))
                res.add(clazz);

        } while (!"java.lang.Object".equals(clazz.getCanonicalName()));

        return new HashSet<>(res);
    }

    public static void printSuperclasses(ContentPackEntryBlock cpeb) {
        ModCore.error("Superclasses:");
        if (cpeb != null)
            StellwandUtils.getAllExtendedOrImplementedTypesRecursively(cpeb.getClass()).forEach(c -> ModCore.error("\t" + c.getName()));
    }

    public static boolean isStellwandBlock(World world, Vec3i pos) {
        if (isStellwandBlock(world, pos, BlockFillerStorageEntity.class)) {
            return true;
        } else if (isStellwandBlock(world, pos, BlockMultisignalStorageEntity.class)) {
            return true;
        } else if (isStellwandBlock(world, pos, BlockSenderStorageEntity.class)) {
            return true;
        } else {
            return isStellwandBlock(world, pos, BlockSignalStorageEntity.class);
        }
    }

    public static boolean isStellwandBlock(World world, Vec3i pos, Class<? extends BlockEntity> bec) {
        return world.getBlockEntity(pos, bec) != null;
    }
}

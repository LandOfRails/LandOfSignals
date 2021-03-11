package net.landofrails.landofsignals.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.math.Vec3d;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.items.ItemSignalPart;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.Static;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockSignalPart extends BlockTypeEntity {

    private int rot;
    private final String name;
    private final String path;
    private final Vec3d translation;
    private final Vec3d itemTranslation;
    private final Vec3d scaling;
    private final List<String> states;
    private final ItemSignalPart item;

    public BlockSignalPart(String name, @Nullable String customName, String path, Vec3d translation, Vec3d scaling) {
        this(name, customName, path, translation, translation, scaling, new ArrayList<String>() {{
            add(null);
        }});
    }

    public BlockSignalPart(String name, @Nullable String customName, String path, Vec3d translation, Vec3d itemTranslaton, Vec3d scaling) {
        this(name, customName, path, translation, itemTranslaton, scaling, new ArrayList<String>() {{
            add(null);
        }});
    }

    public BlockSignalPart(String name, @Nullable String customName, String path, Vec3d translation, Vec3d scaling, List<String> states) {
        this(name, customName, path, translation, translation, scaling, states);
    }

    public BlockSignalPart(String name, @Nullable String customName, String path, Vec3d translation, Vec3d itemTranslaton, Vec3d scaling, List<String> states) {
        super(LandOfSignals.MODID, name);
        Static.blockSignalPartList.put(name, this);
        item = new ItemSignalPart(name.replace("block", "item"), customName, this);
        this.name = name;
        this.path = path;
        this.translation = translation;
        this.scaling = scaling;
        this.states = states;
        this.itemTranslation = itemTranslaton;
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileSignalPart(rot, name);
    }

    public void setRot(int rot) {
        this.rot = rot;
    }

    public String getPath() {
        return path;
    }

    public Vec3d getTranslation() {
        return translation;
    }

    public Vec3d getScaling() {
        return scaling;
    }

    public List<String> getStates() {
        return states;
    }

    public ItemSignalPart getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public Vec3d getItemTranslation() {
        return itemTranslation;
    }
}

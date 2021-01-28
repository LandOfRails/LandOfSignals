package net.landofrails.landofsignals.blocks;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.items.ItemSignalPart;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.Static;

import java.util.ArrayList;
import java.util.List;

public class BlockSignalPart extends BlockTypeEntity {

    private int rot;
    private Vec3i pos;
    private final String name;
    private final String path;
    private final Vec3d translation;
    private final Vec3d itemTranslation;
    private final Vec3d scaling;
    private final List<String> states;
    private final ItemSignalPart item;
    private TileSignalPart tile;

    public BlockSignalPart(String name, String path, Vec3d translation, Vec3d scaling) {
        this(name, path, translation, translation, scaling, new ArrayList<String>() {{
            add(null);
        }});
    }

    public BlockSignalPart(String name, String path, Vec3d translation, Vec3d itemTranslaton, Vec3d scaling) {
        this(name, path, translation, itemTranslaton, scaling, new ArrayList<String>() {{
            add(null);
        }});
    }

    public BlockSignalPart(String name, String path, Vec3d translation, Vec3d scaling, List<String> states) {
        this(name, path, translation, translation, scaling, states);
    }

    public BlockSignalPart(String name, String path, Vec3d translation, Vec3d itemTranslaton, Vec3d scaling, List<String> states) {
        super(LandOfSignals.MODID, name);
        Static.blockSignalPartList.put(name, this);
        item = new ItemSignalPart(name.replace("block", "item"), this);
        this.name = name;
        this.path = path;
        this.translation = translation;
        this.scaling = scaling;
        this.states = states;
        this.itemTranslation = itemTranslaton;
        if (states.size() > 1) tile.setChanging();
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return tile = new TileSignalPart(rot, name, pos);
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

    public void setPos(Vec3i pos) {
        this.pos = pos;
    }

    public Vec3d getItemTranslation() {
        return itemTranslation;
    }
}

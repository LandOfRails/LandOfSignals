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

public class BlockSignalPart extends BlockTypeEntity {

    private int rot;
    private Vec3i pos;
    private String name;
    private String path;
    private Vec3d translation;
    private Vec3d scaling;
    private ArrayList<String> states;
    private ItemSignalPart item;

    public BlockSignalPart(String name, String path, Vec3d translation, Vec3d scaling, ArrayList<String> states) {
        super(LandOfSignals.MODID, name);
        Static.blockSignalPartList.put(name, this);
        item = new ItemSignalPart(name.replace("block", "item"), this);
        this.name = name;
        this.path = path;
        this.translation = translation;
        this.scaling = scaling;
        this.states = states;
    }

    @Override
    protected BlockEntity constructBlockEntity() {
        return new TileSignalPart(rot, name, pos);
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

    public ArrayList<String> getStates() {
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
}

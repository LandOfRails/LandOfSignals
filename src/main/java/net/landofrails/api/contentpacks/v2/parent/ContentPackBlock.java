package net.landofrails.api.contentpacks.v2.parent;

import cam72cam.mod.math.Vec3d;

import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ContentPackBlock {

    private float[] translation;
    private float[] rotation;
    private float[] scaling;

    public ContentPackBlock(float[] translation, float[] rotation, float[] scaling) {
        this.translation = translation;
        this.rotation = rotation;
        this.scaling = scaling;
    }

    public float[] getTranslation() {
        return translation;
    }

    public void setTranslation(float[] translation) {
        this.translation = translation;
    }

    public float[] getRotation() {
        return rotation;
    }

    public void setRotation(float[] rotation) {
        this.rotation = rotation;
    }

    public float[] getScaling() {
        return scaling;
    }

    public void setScaling(float[] scaling) {
        this.scaling = scaling;
    }

    public void validate(Consumer<String> invalid) {
        StringJoiner stringJoiner = new StringJoiner(",", "[", "]");
        if (translation == null) {
            translation = new float[]{.5f, .5f, .5f};
        } else if (translation.length != 3) {
            stringJoiner.add("translation should contain 3 values");
        }
        if (rotation == null) {
            rotation = new float[]{0, 0, 0};
        } else if (rotation.length != 3) {
            stringJoiner.add("rotation should contain 3 values");
        }
        if (scaling == null) {
            scaling = new float[]{.7f, .7f, .7f};
        } else if (scaling.length != 3) {
            stringJoiner.add("scaling should contain 3 values");
        }
        if (stringJoiner.length() > 2) {
            invalid.accept("block: " + stringJoiner.toString());
        }
        clampArray(rotation, -360.0f, 360.0f);
        clampArray(scaling, .1f, 25f);
    }

    public Vec3d getAsVec3d(Supplier<float[]> method) {
        float[] values = method.get();
        return new Vec3d(values[0], values[1], values[2]);
    }

    private void clampArray(float[] floatArray, float min, float max) {
        for (int index = 0; index < floatArray.length; index++)
            floatArray[index] = Math.max(Math.min(floatArray[index], max), min);
    }

}
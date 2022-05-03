package net.landofrails.api.contentpacks.v2.parent;

import cam72cam.mod.math.Vec3d;

import java.util.function.Supplier;

public class ContentPackItem {

    private float[] translation;
    private float[] rotation;
    private float[] scaling;

    public ContentPackItem(float[] translation, float[] rotation, float[] scaling) {
        this.translation = translation;
        this.rotation = rotation;
        this.scaling = scaling;
    }

    public void validate() {
        if (translation == null) {
            translation = new float[]{.5f, .5f, .5f};
        }
        if (rotation == null) {
            rotation = new float[]{0, 0, 0};
        }
        if (scaling == null) {
            scaling = new float[]{.7f, .7f, .7f};
        }
        clampArray(rotation, -360.0f, 360.0f);
        clampArray(scaling, .1f, 25f);
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

    public Vec3d getAsVec3d(Supplier<float[]> method) {
        float[] values = method.get();
        return new Vec3d(values[0], values[1], values[2]);
    }

    private void clampArray(float[] floatArray, float min, float max) {
        for (int index = 0; index < floatArray.length; index++)
            floatArray[index] = Math.max(Math.min(floatArray[index], max), min);
    }


}
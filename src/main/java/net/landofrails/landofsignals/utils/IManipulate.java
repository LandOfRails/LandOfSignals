package net.landofrails.landofsignals.utils;

import cam72cam.mod.math.Vec3d;

public interface IManipulate {

    /**
     * Provide new Vec3d for offset
     */
    void setOffset(Vec3d vec);

    Vec3d getOffset();

    void setRotation(int rotation);

    int getRotation();

}

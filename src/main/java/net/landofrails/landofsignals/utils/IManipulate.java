package net.landofrails.landofsignals.utils;

import cam72cam.mod.math.Vec3d;

public interface IManipulate {

    void setOffset(Vec3d vec);

    Vec3d getOffset();

    void setRotation(int rotation);

    int getRotation();

    void setScaling(Vec3d scaling);

    Vec3d getScaling();

}

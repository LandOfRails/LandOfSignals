package net.landofrails.stellwand.utils;

import cam72cam.mod.math.Vec3d;

public interface ICustomRenderer {

	public Vec3d getTranslate();

	public float getScale();

	public Vec3d getRotation();

	public String getPath();
}

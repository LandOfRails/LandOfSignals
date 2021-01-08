package net.landofrails.stellwand.utils;

import cam72cam.mod.math.Vec3d;

public interface ICustomRenderer {

	public Vec3d getTranslate(BlockItemType type);

	public float getScale(BlockItemType type);

	public Vec3d getRotation(BlockItemType type);

	public String getPath(BlockItemType type);
}

package net.landofrails.stellwand.contentpacks.entries.parent;

public class ContentPackEntryItem {

	private float[] rotation;
	private float[] translation;
	private float scale;
	private String model;
	private String mode;

	public ContentPackEntryItem(float[] rotation, float[] translation, float scale, String model, String mode) {
		this.rotation = rotation;
		this.translation = translation;
		this.scale = scale;
		this.model = model;
		this.mode = mode;
	}

	public float[] getRotation() {
		return rotation;
	}

	public float[] getTranslation() {
		return translation;
	}

	public float getScale() {
		return scale;
	}

	public String getModel() {
		return model;
	}

	public String getMode() {
		return mode;
	}

}

package net.landofrails.stellwand.contentpacks.entries.parent;

public abstract class ContentPackEntryBlock {

	private float[] rotation;
	private float[] translation;

	public ContentPackEntryBlock() {

	}

	@SuppressWarnings("java:S1319")
	public ContentPackEntryBlock(float[] rotation, float[] translation) {
		this.rotation = rotation;
		this.translation = translation;
	}

	public float[] getRotation() {
		return rotation;
	}

	public float[] getTranslation() {
		return translation;
	}

	public <T extends ContentPackEntryBlock> T getBlock(Class<T> clazz) {
		return clazz.cast(this);
	}

}

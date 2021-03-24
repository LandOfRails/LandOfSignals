package net.landofrails.stellwand.contentpacks.entries.parent;

public class ContentPackEntryBlock {

	private float[] rotation;
	private float[] translation;

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

}

package net.landofrails.landofsignals.utils.contentpacks;

public class ContentPackSignObject {

    private String model;

    private float[] translation;
    private float[] itemTranslation;
    private float[] scaling;

    private float[] itemScaling;

    private String[] renderGroups;
    private String texture;

    public ContentPackSignObject(String model, float[] translation, float[] itemTranslation, float[] scaling) {
        this(model, translation, itemTranslation, scaling, scaling, new String[0]);
    }

    public ContentPackSignObject(String model, float[] translation, float[] itemTranslation, float[] scaling, float[] itemScaling) {
        this(model, translation, itemTranslation, scaling, itemScaling, new String[0]);
    }

    public ContentPackSignObject(String model, float[] translation, float[] itemTranslation, float[] scaling, String[] renderGroups) {
        this(model, translation, itemTranslation, scaling, scaling, renderGroups);
    }

    public ContentPackSignObject(String model, float[] translation, float[] itemTranslation, float[] scaling, float[] itemScaling, String[] renderGroups) {
        this(model, translation, itemTranslation, scaling, itemScaling, renderGroups, null);
    }

    public ContentPackSignObject(String model, float[] translation, float[] itemTranslation, float[] scaling, float[] itemScaling, String[] renderGroups, String texture) {
        this.model = model;
        this.translation = translation;
        this.itemTranslation = itemTranslation;
        this.scaling = scaling;
        this.itemScaling = itemScaling;
        this.renderGroups = renderGroups;
        this.texture = texture;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float[] getTranslation() {
        return translation;
    }

    public void setTranslation(float[] translation) {
        this.translation = translation;
    }

    public float[] getItemTranslation() {
        return itemTranslation;
    }

    public void setItemTranslation(float[] itemTranslation) {
        this.itemTranslation = itemTranslation;
    }

    public float[] getScaling() {
        return scaling;
    }

    public void setScaling(float[] scaling) {
        this.scaling = scaling;
    }

    public float[] getItemScaling() {
        if (itemScaling == null) {
            if (scaling != null) {
                itemScaling = scaling;
            } else {
                // Emergency value
                itemScaling = new float[]{1, 1, 1};
            }
        }
        return itemScaling;
    }

    public void setItemScaling(float[] itemScaling) {
        this.itemScaling = itemScaling;
    }

    public String[] getRenderGroups() {
        return renderGroups;
    }

    public void setRenderGroups(String[] renderGroups) {
        this.renderGroups = renderGroups;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}

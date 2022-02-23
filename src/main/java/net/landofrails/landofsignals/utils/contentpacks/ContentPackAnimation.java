package net.landofrails.landofsignals.utils.contentpacks;

import java.util.List;

public class ContentPackAnimation {

    private List<String> groups;
    private float[] move;
    private float[] rotate;
    private String state;
    private float duration;
    private float startAfter;

    public ContentPackAnimation(final List<String> groups, final float[] move, final float[] rotate, final String state, final float duration, final float startAfter) {
        this.groups = groups;
        this.move = move;
        this.rotate = rotate;
        this.state = state;
        this.duration = duration;
        this.startAfter = startAfter;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(final List<String> groups) {
        this.groups = groups;
    }

    public float[] getMove() {
        return move;
    }

    public void setMove(final float[] move) {
        this.move = move;
    }

    public float[] getRotate() {
        return rotate;
    }

    public void setRotate(final float[] rotate) {
        this.rotate = rotate;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(final float duration) {
        this.duration = duration;
    }

    public float getStartAfter() {
        return startAfter;
    }

    public void setStartAfter(final float startAfter) {
        this.startAfter = startAfter;
    }
}

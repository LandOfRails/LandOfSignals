package net.landofrails.landofsignals.utils.contentpacks;

import java.util.List;

public class ContentPackAnimation {

    private List<String> groups;
    private float[] move;
    private float[] rotate;
    private String state;
    private float duration;
    private float startAfter;

    public ContentPackAnimation(List<String> groups, float[] move, float[] rotate, String state, float duration, float startAfter) {
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

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public float[] getMove() {
        return move;
    }

    public void setMove(float[] move) {
        this.move = move;
    }

    public float[] getRotate() {
        return rotate;
    }

    public void setRotate(float[] rotate) {
        this.rotate = rotate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getStartAfter() {
        return startAfter;
    }

    public void setStartAfter(float startAfter) {
        this.startAfter = startAfter;
    }
}

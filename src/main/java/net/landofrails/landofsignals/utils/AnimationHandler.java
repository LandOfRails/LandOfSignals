package net.landofrails.landofsignals.utils;

import net.landofrails.landofsignals.utils.contentpacks.ContentPackAnimation;

import java.util.List;
import java.util.Map;

public class AnimationHandler {

    private Map<String, List<ContentPackAnimation>> animations;

    public AnimationHandler(Map<String, List<ContentPackAnimation>> animations) {
        this.animations = animations;
    }
}

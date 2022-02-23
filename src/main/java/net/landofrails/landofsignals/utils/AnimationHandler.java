package net.landofrails.landofsignals.utils;

import net.landofrails.landofsignals.utils.contentpacks.ContentPackAnimation;

import java.util.List;
import java.util.Map;

public class AnimationHandler {

    private final Map<String, List<ContentPackAnimation>> animations;

    public AnimationHandler(final Map<String, List<ContentPackAnimation>> animations) {
        this.animations = animations;
    }
}

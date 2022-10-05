package net.landofrails.landofsignals.utils;

import net.landofrails.api.contentpacks.v1.ContentPackAnimation;

import java.util.List;
import java.util.Map;

public class AnimationHandler {

    @SuppressWarnings("unused")
    private final Map<String, List<ContentPackAnimation>> animations;

    public AnimationHandler(final Map<String, List<ContentPackAnimation>> animations) {
        this.animations = animations;
    }
}

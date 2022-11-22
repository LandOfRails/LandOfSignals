package net.landofrails.api.contentpacks.v2.parent;

import cam72cam.mod.render.ItemRender;

public enum ContentPackItemRenderType {

    DEFAULT,
    THIRD_PERSON_LEFT_HAND,
    THIRD_PERSON_RIGHT_HAND,
    FIRST_PERSON_LEFT_HAND,
    FIRST_PERSON_RIGHT_HAND,
    HEAD,
    GUI,
    ENTITY,
    FRAME;

    public static ContentPackItemRenderType map(ItemRender.ItemRenderType itemRenderType) {
        return ContentPackItemRenderType.valueOf(itemRenderType.name());
    }

}

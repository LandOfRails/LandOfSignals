package net.landofrails.landofsignals.render.item;

import java.io.Serial;

public class ItemRenderException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -9103800315967242824L;

    public ItemRenderException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

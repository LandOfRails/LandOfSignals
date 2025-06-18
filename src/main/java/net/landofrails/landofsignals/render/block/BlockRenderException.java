package net.landofrails.landofsignals.render.block;

public class BlockRenderException extends RuntimeException {
    private static final long serialVersionUID = 884378652673553882L;

    public BlockRenderException(String message) {
        super(message);
    }

    public BlockRenderException(String message, Throwable throwable) {
        super(message, throwable);
    }

}

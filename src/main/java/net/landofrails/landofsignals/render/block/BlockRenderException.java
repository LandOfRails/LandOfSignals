package net.landofrails.landofsignals.render.block;

public class BlockRenderException extends RuntimeException {
    private static final long serialVersionUID = 884378652673553882L;

    public BlockRenderException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BlockRenderException(String message, String... args) {
        super(String.format(message, (Object[]) args));
    }
}

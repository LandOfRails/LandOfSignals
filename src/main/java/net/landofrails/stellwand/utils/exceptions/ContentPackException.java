package net.landofrails.stellwand.utils.exceptions;

public class ContentPackException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 324008445989906666L;

	public ContentPackException(String text) {
		super(text);
	}

	public ContentPackException(String text, Throwable throwable) {
		super(text, throwable);
	}

}

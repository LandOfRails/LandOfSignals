package net.landofrails.stellwand.content.messages;

import cam72cam.mod.text.TextUtil;
import net.landofrails.stellwand.Stellwand;

public enum Message {

	// @formatter:off
	
	// Sender
	MESSAGE_NO_SIGNALS_CONNECTED("sender.nosignalsconnected"),
	MESSAGE_MODES_SET("sender.modesset");

	// @formatter:on

	private final String value;

	Message(String value) {
		this.value = value;
	}

	public String getRaw() {
		return "message." + Stellwand.DOMAIN + ":" + value;
	}

	@Override
	public String toString() {
		return TextUtil.translate(getRaw());
	}

	public String toString(Object... objects) {
		return TextUtil.translate(getRaw(), objects);
	}

}

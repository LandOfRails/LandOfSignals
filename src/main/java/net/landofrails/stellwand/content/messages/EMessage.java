package net.landofrails.stellwand.content.messages;

import cam72cam.mod.text.TextUtil;
import net.landofrails.stellwand.Stellwand;

public enum EMessage {

	// @formatter:off
	
	// Sender
	MESSAGE_NO_SIGNALS_CONNECTED("sender.nosignalsconnected"),
	MESSAGE_MODES_SET("sender.modesset"), 
	MESSAGE_NO_SIGNAL_FOUND("sender.signalnotfound"),
	MESSAGE_ERROR1("sender.error1"),
	
	// Connector
	MESSAGE_SIGNAL_DISCONNECTED("connector.signaldisconnected"),
	MESSAGE_SIGNAL_CONNECTED("connector.signalconnected"), 
	MESSAGE_SIGNALS_MUST_BE_EQUAL("connector.sendersignalsmustbeequal"),
	MESSAGE_NEW_SIGNAL_SELECTED("connector.newsignalselected"),
	MESSAGE_NEW_SENDER_SELECTED("connector.newsenderselected");
	// @formatter:on

	private final String value;

	EMessage(String value) {
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

	public static EMessage fromRaw(String raw) {
		for (EMessage eMessage : values())
			if (eMessage.getRaw().equals(raw))
				return eMessage;
		return null;
	}

}

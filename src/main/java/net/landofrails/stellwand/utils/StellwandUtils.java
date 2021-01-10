package net.landofrails.stellwand.utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

import net.landofrails.stellwand.content.blocks.others.BlockSignal;

public class StellwandUtils {

	private StellwandUtils() {

	}

	public static Optional<File> getModFolder() {
		try {
			URL url = BlockSignal.class.getProtectionDomain().getCodeSource().getLocation();
			String uri = new URI(url.toString().replace(" ", "%20")).getSchemeSpecificPart();
			File file = new File(uri.split("!")[0]);
			file = file.getParentFile();
			return Optional.ofNullable(file);
		} catch (NullPointerException | URISyntaxException e) {
			return Optional.empty();
		}
	}

}

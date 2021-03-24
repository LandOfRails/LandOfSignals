package net.landofrails.stellwand.config;

import cam72cam.mod.config.ConfigFile.Comment;
import cam72cam.mod.config.ConfigFile.File;
import cam72cam.mod.config.ConfigFile.Name;

@Comment("Configuration File for stellwand")
@Name("stellwand")
@File("stellwand.cfg")
public class StellwandConfig {

	private StellwandConfig() {

	}

	public static void init() {
		// Derzeit nichts zum initialisieren.
	}

	@Name("debugging")
	public static class Debugging {

		private Debugging() {

		}

		@Comment("Enables debug outputs, just for testing")
		public static boolean debugOutput = false;

	}

}

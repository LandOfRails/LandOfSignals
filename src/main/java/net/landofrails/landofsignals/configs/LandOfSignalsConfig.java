package net.landofrails.landofsignals.configs;

import cam72cam.mod.config.ConfigFile;

@ConfigFile.Comment("Configuration File for LandOfSignals")
@ConfigFile.Name("general")
@ConfigFile.File("landofsignals.cfg")
@SuppressWarnings({"java:S1118", "java:S1444"})
public class LandOfSignalsConfig {

    @ConfigFile.Comment("Should LegacyMode be used for old v1 contentpacks? (ON, OFF, PROMPT)")
    public static LegacyMode legacyMode = LegacyMode.PROMPT;

}

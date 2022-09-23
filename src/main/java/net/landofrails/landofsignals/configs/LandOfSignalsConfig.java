package net.landofrails.landofsignals.configs;

import cam72cam.mod.config.ConfigFile;
import net.landofrails.landofsignals.LandOfSignals;

@ConfigFile.Comment("Configuration File for LandOfSignals")
@ConfigFile.Name("general")
@ConfigFile.File("landofsignals.cfg")
public class LandOfSignalsConfig {

    @ConfigFile.Comment("DO NOT CHANGE! This is saved for compatability reasons.")
    public static String currentVersion = LandOfSignals.VERSION;

    @ConfigFile.Comment("Should LegacyMode be used for old v1 contentpacks? (ON, OFF, PROMPT)")
    public static LegacyMode legacyMode = LegacyMode.PROMPT;

}

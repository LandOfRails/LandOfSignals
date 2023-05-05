package net.landofrails.stellwand.config;

import cam72cam.mod.config.ConfigFile.Comment;
import cam72cam.mod.config.ConfigFile.File;
import cam72cam.mod.config.ConfigFile.Name;

@Comment("Configuration File for stellwand")
@Name("stellwand")
@File("stellwand.cfg")
@SuppressWarnings({"java:S1444", "java:S1118"})
public class StellwandConfig {

    public static void init() {
        // Currently nothing to initialize
    }

    @Comment("Disables the stellwand part. If you want to play on a server that has this activated, set it to \"false\". | Default: true")
    public static boolean disableStellwand = true;

    @Comment("Disables all stellwand recipes | Default: false")
    public static boolean disableRecipes = false;

    @Name("debugging")
    @Comment("Debugging options")
    public static class Debugging {

        @Comment("Enables debug outputs | Default: false")
        public static boolean debugOutput = false;

    }

}

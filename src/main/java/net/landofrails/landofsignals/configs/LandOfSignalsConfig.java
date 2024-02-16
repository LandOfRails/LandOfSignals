package net.landofrails.landofsignals.configs;

import cam72cam.mod.config.ConfigFile;

import java.util.HashMap;
import java.util.Map;

@ConfigFile.Comment("Configuration File for LandOfSignals")
@ConfigFile.Name("general")
@ConfigFile.File("landofsignals.cfg")
@SuppressWarnings({"java:S1118", "java:S1444"})
public class LandOfSignalsConfig {

    @ConfigFile.Comment("Enables preloading, slower startup, less lag spikes. If you want to use it, set it to \"true\". | Default: false")
    public static boolean preloadModels = false;

    @ConfigFile.Name("experimental")
    @ConfigFile.Comment("Experimental features - activate at own risk")
    public static class Experimental {

        @ConfigFile.Comment("Rescale items to slot size - needs \"preloadModels\" to be activated | Default: false")
        public static boolean rescaleItems = false;

    }

    public static Map<String, Object> values(){
        Map<String, Object> values = new HashMap<>();
        values.put("preloadModels", preloadModels);
        values.put("experimental.rescaleItems", Experimental.rescaleItems);
        return values;
    }

}

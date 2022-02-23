package net.landofrails.landofsignals;

import cam72cam.mod.ModCore;

@net.minecraftforge.fml.common.Mod(modid = Mod.MODID, name = "LandOfSignals", version = "0.0.4", dependencies = "required-after:universalmodcore@[1.1,1.2,1.3)", acceptedMinecraftVersions = "[1.12,1.13)")
public class Mod {
    public static final String MODID = "landofsignals";

    static {
        try {
            ModCore.register(new net.landofrails.landofsignals.LandOfSignals());
        } catch (final Exception e) {
            throw new RuntimeException("Could not load mod " + MODID, e);
        }
    }
}

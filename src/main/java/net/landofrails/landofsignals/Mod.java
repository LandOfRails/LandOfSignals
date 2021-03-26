package net.landofrails.landofsignals;

import cam72cam.mod.ModCore;

@net.minecraftforge.fml.common.Mod(modid = Mod.MODID, name = "LandOfSignals", version = LandOfSignals.VERSION, dependencies = "required-after:universalmodcore@[1.0,1.1)", acceptedMinecraftVersions = "[1.12,1.13)")
public class Mod {
    public static final String MODID = "landofsignals";

	public Mod() {
		// Has to be public
	}

    static {
        try {
            ModCore.register(new net.landofrails.landofsignals.LandOfSignals());
        } catch (Exception e) {
            throw new RuntimeException("Could not load mod " + MODID, e);
        }
    }
}

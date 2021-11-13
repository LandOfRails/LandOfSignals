package net.landofrails.stellwand.content.items.connector;

import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.world.World;
import net.landofrails.stellwand.utils.compact.SignalContainer;

public class ItemConnectorSignal extends AItemConnector {
    private static final long serialVersionUID = -7946475502287227609L;

    static {
        AItemConnector.registerConnector(ItemConnectorSignal.class, ItemConnectorSignal::isConnectable);
    }

    public ItemConnectorSignal() {
        super();
    }

    public ItemConnectorSignal(TagCompound tag) {
        super(tag);
    }

    private static Boolean isConnectable(World world, Vec3i pos) {
        return SignalContainer.isSignal(world, pos);
    }

    @Override
    protected boolean connect(World world, Vec3i pos, Player player, Player.Hand hand) {
        return false;
    }

}

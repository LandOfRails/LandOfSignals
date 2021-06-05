package net.landofrails.stellwand.content.guis;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.ModCore;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.render.OpenGL;
import net.landofrails.landofsignals.gui.GuiText;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.network.ChangeSenderModes;

public class SelectSenderModes implements IScreen {

	// List of modes
	private Map<String, String> modes;

	// Position for packet
	private Vec3i pos;

	// Display
	private final ItemStack itemPowerOff;
	private final ItemStack itemPowerOn;

	@SuppressWarnings({"java:S112", "java:S1192"})
	public SelectSenderModes(BlockSenderStorageEntity entity) {

		if (entity.getSignal() != null) {

			BlockSignalStorageEntity signalEntity = entity.getSignal();
			if (signalEntity != null) {
				modes = signalEntity.getPossibleModes();

				// @formatter:off
				// Item Power off
				itemPowerOff = new ItemStack(CustomItems.ITEMBLOCKSIGNAL, 1);
				itemPowerOff.getTagCompound().setString("itemId", signalEntity.contentPackBlockId);
				String customModePowerOff = entity.modePowerOff != null ? entity.modePowerOff : modes.values().iterator().next();
				itemPowerOff.getTagCompound().setString("customMode", customModePowerOff);
				// Item Power on
				itemPowerOn = new ItemStack(CustomItems.ITEMBLOCKSIGNAL, 1);
				itemPowerOn.getTagCompound().setString("itemId", signalEntity.contentPackBlockId);
				String customModePowerOn = entity.modePowerOn != null ? entity.modePowerOn : modes.values().iterator().next();
				itemPowerOn.getTagCompound().setString("customMode",customModePowerOn);
				// @formatter:on
				// Position
				pos = entity.getPos();

				return;
			} else {
				ModCore.error("Couldnt get signal: %s", "Signal doesn't exist (anymore)");
			}
		} else {
			ModCore.error("Sender has no signals!");
		}
		throw new RuntimeException("Entity does not contain signals.");

	}

	@Override
	public void init(IScreenBuilder screen) {
		new Button(screen, -100, 40,
				"<-- " + GuiText.LABEL_NOREDSTONE.toString()) {
			@Override
			public void onClick(Player.Hand hand) {
				String mode = itemPowerOff.getTagCompound()
						.getString("customMode");
				itemPowerOff.getTagCompound().setString("customMode",
						nextMode(mode));
			}
		};
		new Button(screen, -100, 80,
				GuiText.LABEL_REDSTONE.toString() + " -->") {
			@Override
			public void onClick(Player.Hand hand) {
				String mode = itemPowerOn.getTagCompound()
						.getString("customMode");
				itemPowerOn.getTagCompound().setString("customMode",
						nextMode(mode));
			}
		};
	}

	@Override
	public void onEnterKey(IScreenBuilder builder) {
		builder.close();
	}

	@Override
	public void onClose() {
		String modePowerOff = itemPowerOff.getTagCompound()
				.getString("customMode");
		String modePowerOn = itemPowerOn.getTagCompound()
				.getString("customMode");
		ChangeSenderModes packet = new ChangeSenderModes(pos, modePowerOff,
				modePowerOn);
		packet.sendToServer();
	}

	@Override
	public void draw(IScreenBuilder builder) {
		int scale = 8;
		try (OpenGL.With ignored = OpenGL.matrix()) {
			GL11.glTranslated(
					(double) GUIHelpers.getScreenWidth() / 2
							+ (double) builder.getWidth() / 4,
					(double) builder.getHeight() / 4, 0);
			GL11.glScaled(scale, scale, 1);
			GUIHelpers.drawItem(itemPowerOn, 0, 0);
		}
		try (OpenGL.With ignored = OpenGL.matrix()) {
			GL11.glTranslated(
					((double) GUIHelpers.getScreenWidth() / 2
							- (double) builder.getWidth() / 4) - 120,
					(double) builder.getHeight() / 4, 0);
			GL11.glScaled(scale, scale, 1);
			GUIHelpers.drawItem(itemPowerOff, 0, 0);
		}
	}

	private String nextMode(String mode) {
		boolean useNext = false;
		for (String m : modes.values()) {
			if (m.equalsIgnoreCase(mode))
				useNext = true;
			else if (useNext)
				return m;
		}
		return modes.values().iterator().next();
	}

}

package net.landofrails.stellwand.content.entities.storage;

import java.util.HashMap;
import java.util.Map;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.content.entities.function.BlockSignalFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSignalRenderEntity;
import net.landofrails.stellwand.storage.RunTimeStorage;

public class BlockSignalStorageEntity extends BlockSignalFunctionEntity {

	// Global final variables
	public static final String MISSING = "missing";

	// TagFields
	@TagField
	public String contentPackBlockId = MISSING;
	@TagField
	public float rot = 0;
	@TagField
	private String mode;

	// Modes
	public Map<Vec3i, String> modes = new HashMap<>();
	
	// Subclasses
	public BlockSignalRenderEntity renderEntity;

	public BlockSignalStorageEntity() {
		renderEntity = new BlockSignalRenderEntity(this);
	}

	@Override
	public void load(TagCompound nbt) throws SerializationException {
		super.load(nbt);
		RunTimeStorage.register(getPos(), this);
	}

	public void setContentBlockId(String id) {
		contentPackBlockId = id;
	}

	public String getContentBlockId() {
		return contentPackBlockId;
	}
	
	public String getMode() {

		if (this.mode == null) {
			Map<String, String> m = BlockSignalRenderEntity.modes.get(this.contentPackBlockId);
			if (m != null && !m.isEmpty()) {
				this.mode = m.values().iterator().next();
			}
		}
		return this.mode;

	}

	public void setMode(String mode) {

		String side = getWorld().isServer ? "Server" : "Client";
		ModCore.info("Side: " + side);
		ModCore.info("Changed mode: " + getPos().toString());
		ModCore.info("Mode: " + mode);

		this.mode = mode;
	}

}

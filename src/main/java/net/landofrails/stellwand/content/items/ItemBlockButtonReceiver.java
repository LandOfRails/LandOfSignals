package net.landofrails.stellwand.content.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.ClickResult;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.ItemRender;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.util.Facing;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.messages.EMessage;
import net.landofrails.stellwand.content.network.ServerMessagePacket;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryItem;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ItemBlockButtonReceiver extends CustomItem {

    private static final String ITEMID = "itemId";

    // VARIABLES
    public static final String MISSING = "missing";
    private static Map<String, OBJModel> models = new HashMap<>();
    private static Map<String, float[]> rotations = new HashMap<>();
    private static Map<String, float[]> translations = new HashMap<>();
    private static Map<String, Float> scales = new HashMap<>();
    private static Map<String, String> modes = new HashMap<>();

    public ItemBlockButtonReceiver() {
        super(LandOfSignals.MODID, "stellwand.itemblockbuttonreceiver");
    }

    // Only for Clientside
    public static void init() {
        if (models.isEmpty()) {
            try {
                Identifier id = new Identifier(Stellwand.DOMAIN, "models/block/others/blocknotfound/blocknotfound.obj");
                OBJModel model = new OBJModel(id, 0);
                models.put(MISSING, model);
                // Renderers in render function
                rotations.put(MISSING, new float[]{15, 195, 0});
                translations.put(MISSING, new float[]{0.5f, 0.5f, 0.5f});
                scales.put(MISSING, 0.7f);
                modes.put(MISSING, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ContentPack
            for (Entry<ContentPackEntry, String> entry : Content.getBlockButtonReceivers().entrySet()) {
                try {
                    ContentPackEntry cpe = entry.getKey();
                    String packId = entry.getValue();
                    String itemName = cpe.getBlockId(packId);
                    ContentPackEntryItem item = cpe.getItem();
                    Identifier id = new Identifier("stellwand", item.getModel());
                    OBJModel model = new OBJModel(id, 0);

                    models.put(itemName, model);
                    // Renderers in render function
                    rotations.put(itemName, item.getRotation());
                    translations.put(itemName, item.getTranslation());
                    scales.put(itemName, item.getScale());
                    modes.put(itemName, item.getMode());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(CustomTabs.STELLWAND_TAB);
    }

    @Override
    public List<ItemStack> getItemVariants(CreativeTab creativeTab) {
        List<ItemStack> items = new ArrayList<>();

        if (creativeTab != null && !creativeTab.equals(CustomTabs.STELLWAND_TAB))
            return items;

        if (getFirstVarient() != null)
            items.add(getFirstVarient());

        return items;
    }

    public ItemStack getFirstVarient() {
        ItemStack is = null;
        Iterator<Entry<ContentPackEntry, String>> it = Content.getBlockButtonReceivers().entrySet().iterator();
        if (it.hasNext()) {
            Entry<ContentPackEntry, String> entry = it.next();

            ContentPackEntry cpe = entry.getKey();
            is = new ItemStack(CustomItems.ITEMBLOCKBUTTONRECEIVER, 1);
            TagCompound tag = is.getTagCompound();
            tag.setString(ITEMID, cpe.getBlockId(entry.getValue()));
            is.setTagCompound(tag);

        }
        return is;
    }

    @Override
    public void onClickAir(Player player, World world, Player.Hand hand) {
        if (world.isServer) {
            ServerMessagePacket.send(player, EMessage.MESSAGE_DEPRECATED);
        }
    }

    public static ItemRender.IItemModel getModelFor() {

        return (world, stack) -> new StandardModel().addCustom((state, partialTicks) -> {

            ItemBlockButtonReceiver.init();

            TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString(ITEMID);
            if (itemId == null || !models.containsKey(itemId)) {
                itemId = MISSING;
            }

            float[] translate = translations.get(itemId);
            float[] rotation = rotations.get(itemId);
            // Enables the gui to display different modes
            String customMode = stack.getTagCompound().getString("customMode");
            String mode = customMode != null ? customMode : modes.get(itemId);
            float scale = scales.get(itemId);
            OBJModel model = models.get(itemId);

            state.translate(translate[0], translate[1], translate[2]);
            state.rotate(rotation[0], 1, 0, 0);
            state.rotate(rotation[1], 0, 1, 0);
            state.rotate(rotation[2], 0, 0, 1);
            state.scale(scale, scale, scale);

            try (OBJRender.Binding vbo = model.binder().bind(state)) {

                if (mode == null) {

                    vbo.draw();

                } else {

                    ArrayList<String> modes = model.groups().stream().filter(s -> s.startsWith(mode))
                            .collect(Collectors.toCollection(ArrayList::new));

                    if (!modes.isEmpty()) {
                        ArrayList<String> generals = model.groups().stream().filter(s -> s.startsWith("general"))
                                .collect(Collectors.toCollection(ArrayList::new));
                        modes.addAll(generals);
                        vbo.draw(modes);
                    } else {
                        vbo.draw(model.groups());
                    }

                }
            }
        });
    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Player.Hand hand, Facing facing, Vec3d inBlockPos) {
        if (world.isServer) {
            ServerMessagePacket.send(player, EMessage.MESSAGE_DEPRECATED);
        }
        return ClickResult.ACCEPTED;
    }

    private boolean isStandingInBlock(Vec3i vec3i) {
        return vec3i.x == 0 && vec3i.z == 0 && (vec3i.y == 0 || vec3i.y == -1);
    }

    @Override
    public List<String> getTooltip(ItemStack itemStack) {
        String lore = "";
        TagCompound tag = itemStack.getTagCompound();
        if (tag.hasKey(ITEMID)) {
            String itemId = tag.getString(ITEMID);
            lore = Content.getNameForId(itemId);
        }
        return Collections.singletonList(lore);
    }
}

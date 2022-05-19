package net.landofrails.stellwand.content.items;

import cam72cam.mod.block.BlockTypeEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.Player.Hand;
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
import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.entities.storage.BlockFillerStorageEntity;
import net.landofrails.stellwand.content.guis.SelectItem;
import net.landofrails.stellwand.content.network.ChangeHandHeldItem;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryItem;
import net.landofrails.stellwand.contentpacks.types.EntryType;

import java.util.*;
import java.util.Map.Entry;

public class ItemBlockFiller extends CustomItem {

    // Variables
    public static final String MISSING = "missing";
    private static final String ITEMID = "itemId";
    private static Map<String, OBJModel> models = new HashMap<>();
    private static Map<String, float[]> rotations = new HashMap<>();
    private static Map<String, float[]> translations = new HashMap<>();
    private static Map<String, Float> scales = new HashMap<>();
    //

    // CustomItem
    public ItemBlockFiller() {
        super(LandOfSignals.MODID, "stellwand.itemblockfiller");
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
        Iterator<Entry<ContentPackEntry, String>> it = Content.getBlockFillers().entrySet().iterator();
        if (it.hasNext()) {
            Entry<ContentPackEntry, String> entry = it.next();

            ContentPackEntry cpe = entry.getKey();
            is = new ItemStack(CustomItems.ITEMBLOCKFILLER, 1);
            TagCompound tag = is.getTagCompound();
            tag.setString(ITEMID, cpe.getBlockId(entry.getValue()));
            is.setTagCompound(tag);

        }
        return is;
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
    //

    // Init class
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ContentPack
            for (Entry<ContentPackEntry, String> entry : Content.getBlockFillers().entrySet()) {
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
                    scales.put(itemName, 0.7f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClickAir(Player player, World world, Hand hand) {

        if (world.isServer)
            return;

        int sizeInHand = player.getHeldItem(hand).getCount();
        SelectItem si = new SelectItem();
        si.open(player, EntryType.BLOCKFILLER, new ItemStack(CustomItems.ITEMBLOCKFILLER, 1), item -> {
            if (item != null) {
                player.setHeldItem(hand, item);
                item.setCount(sizeInHand);
                ChangeHandHeldItem packet = new ChangeHandHeldItem(player, item, hand);
                packet.sendToServer();
            }
        });

    }

    @Override
    public ClickResult onClickBlock(Player player, World world, Vec3i pos, Hand hand, Facing facing, Vec3d inBlockPos) {
        Vec3i target = world.isReplaceable(pos) ? pos : pos.offset(facing);

        if (isStandingInBlock(player.getBlockPosition().subtract(target)))
            return ClickResult.REJECTED;

        if (world.isAir(target) || world.isReplaceable(target)) {

            BlockTypeEntity block = CustomBlocks.BLOCKFILLER;

            world.setBlock(target, block);
            if (!player.isCreative()) {
                ItemStack is = player.getHeldItem(hand);
                is.shrink(1);
                player.setHeldItem(hand, is);
            }
            BlockFillerStorageEntity blockEntity = world.getBlockEntity(target, BlockFillerStorageEntity.class);
            // Set ContentPackBlockId
            ItemStack item = player.getHeldItem(hand);
            TagCompound tag = item.getTagCompound();
            if (blockEntity != null) {
                if (tag != null && !tag.isEmpty())
                    blockEntity.setContentBlockId(tag.hasKey(ITEMID) ? tag.getString(ITEMID) : MISSING);
                else
                    blockEntity.setContentBlockId(MISSING);
                blockEntity.renderEntity.setRotation(player.getRotationYawHead());
            }
            //

            return ClickResult.ACCEPTED;
        }

        return ClickResult.REJECTED;
    }

    private boolean isStandingInBlock(Vec3i vec3i) {
        return vec3i.x == 0 && vec3i.z == 0 && (vec3i.y == 0 || vec3i.y == -1);
    }

    // Rendering
    @SuppressWarnings({"java:S3776", "java:S112"})
    public static ItemRender.IItemModel getModelFor() {

        return (world, stack) -> new StandardModel().addCustom((state, partialTicks) -> {

            ItemBlockFiller.init();

            TagCompound tag = stack.getTagCompound();
            String itemId = tag.getString(ITEMID);
            if (itemId == null || !models.containsKey(itemId)) {
                itemId = MISSING;
            }

            OBJModel model = models.get(itemId);
            float[] translate = translations.get(itemId);
            float[] rotation = rotations.get(itemId);
            float scale = scales.get(itemId);

            state.translate(translate[0], translate[1], translate[2]);
            state.rotate(rotation[0], 1, 0, 0);
            state.rotate(rotation[1], 0, 1, 0);
            state.rotate(rotation[2], 0, 0, 1);
            state.scale(scale, scale, scale);

            try (OBJRender.Binding vbo = model.binder().bind(state)) {

                vbo.draw();

            }
        });
    }

}

package net.landofrails.stellwand.content.recipes;

import cam72cam.mod.item.Fuzzy;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.item.Recipes;
import net.landofrails.stellwand.config.StellwandConfig;
import net.landofrails.stellwand.content.items.CustomItems;

@SuppressWarnings("java:S3008")
public class CustomRecipes {

    public static Fuzzy FUZZY_ITEMBLOCKFILLER;
    public static Fuzzy FUZZY_ITEMBLOCKSIGNAL;

    public static void register() {

        // Disable recipes?
        if (StellwandConfig.disableRecipes) {
            return;
        }

        // @formatter:off

		// Filler
		ItemStack itemStackBlockFiller = CustomItems.ITEMBLOCKFILLER.getFirstVarient();
		itemStackBlockFiller.setCount(4);
		Recipes.shapedRecipe(itemStackBlockFiller, 2, 
				Fuzzy.HARDENED_CLAY, Fuzzy.HARDENED_CLAY,
				Fuzzy.HARDENED_CLAY, Fuzzy.HARDENED_CLAY
		);
		FUZZY_ITEMBLOCKFILLER = Fuzzy.get("itemBlockFiller");
		FUZZY_ITEMBLOCKFILLER.add(CustomItems.ITEMBLOCKFILLER);

		// Sender
		ItemStack itemStackBlockSender = CustomItems.ITEMBLOCKSENDER.getFirstVarient();
		itemStackBlockSender.setCount(2);
		Recipes.shapedRecipe(itemStackBlockSender, 2, 
				FUZZY_ITEMBLOCKFILLER, Fuzzy.REDSTONE_TORCH,
				Fuzzy.REDSTONE_TORCH, FUZZY_ITEMBLOCKFILLER
		);

		// Signal
		ItemStack itemStackBlockSignal = CustomItems.ITEMBLOCKSIGNAL.getFirstVarient();
		itemStackBlockSignal.setCount(2);
		Recipes.shapedRecipe(itemStackBlockSignal, 2,
				FUZZY_ITEMBLOCKFILLER, Fuzzy.REDSTONE_DUST,
				Fuzzy.REDSTONE_DUST,  FUZZY_ITEMBLOCKFILLER
		);
		FUZZY_ITEMBLOCKSIGNAL = Fuzzy.get("itemBlockSignal");
		FUZZY_ITEMBLOCKSIGNAL.add(CustomItems.ITEMBLOCKSIGNAL);

		// Multisignal
		ItemStack itemStackBlockMultisignal = CustomItems.ITEMBLOCKMULTISIGNAL.getFirstVarient();
		itemStackBlockMultisignal.setCount(1);
		Recipes.shapedRecipe(itemStackBlockMultisignal, 1,
				FUZZY_ITEMBLOCKSIGNAL,
				FUZZY_ITEMBLOCKSIGNAL
		);

		// Connector
		Recipes.shapedRecipe(CustomItems.ITEMCONNECTOR1, 2,
				FUZZY_ITEMBLOCKFILLER, Fuzzy.REDSTONE_DUST
		);

		// Magnifying glass
		Recipes.shapedRecipe(CustomItems.ITEMMAGNIFYINGGLASS, 2, 
				FUZZY_ITEMBLOCKFILLER, Fuzzy.GLASS_PANE,
				Fuzzy.WOOD_STICK, FUZZY_ITEMBLOCKFILLER
		);
		
		// @formatter:on
    }

}

package net.landofrails.stellwand.content.recipes;

import cam72cam.mod.item.Fuzzy;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.item.Recipes;
import net.landofrails.stellwand.config.StellwandConfig;
import net.landofrails.stellwand.content.items.CustomItems;

@SuppressWarnings("java:S3008")
public class CustomRecipes {

	public static Fuzzy FUZZY_ITEMBLOCKFILLER;

	public static void register() {

		// Disable recipes?
		if (StellwandConfig.disableRecipes) {
			return;
		}

		// @formatter:off
		
		ItemStack itemStackBlockFiller = CustomItems.ITEMBLOCKFILLER.getFirstVarient();
		itemStackBlockFiller.setCount(4);
		Recipes.shapedRecipe(itemStackBlockFiller, 2, 
				Fuzzy.HARDENED_CLAY, Fuzzy.HARDENED_CLAY,
				Fuzzy.HARDENED_CLAY, Fuzzy.HARDENED_CLAY
		);
		FUZZY_ITEMBLOCKFILLER = Fuzzy.get("itemBlockFiller");
		FUZZY_ITEMBLOCKFILLER.add(CustomItems.ITEMBLOCKFILLER);
		
		ItemStack itemStackBlockSender = CustomItems.ITEMBLOCKSENDER.getFirstVarient();
		itemStackBlockSender.setCount(2);
		Recipes.shapedRecipe(itemStackBlockSender, 2, 
				FUZZY_ITEMBLOCKFILLER, Fuzzy.REDSTONE_TORCH,
				Fuzzy.REDSTONE_TORCH, FUZZY_ITEMBLOCKFILLER
		);
		
		ItemStack itemStackBlockSignal = CustomItems.ITEMBLOCKSIGNAL.getFirstVarient();
		itemStackBlockSignal.setCount(2);
		Recipes.shapedRecipe(itemStackBlockSignal, 2, 
				Fuzzy.REDSTONE_DUST, FUZZY_ITEMBLOCKFILLER,
				FUZZY_ITEMBLOCKFILLER, Fuzzy.REDSTONE_DUST
		);
		
		Recipes.shapedRecipe(CustomItems.ITEMCONNECTOR1, 2, 
				FUZZY_ITEMBLOCKFILLER, Fuzzy.REDSTONE_DUST,
				null, null
		);
		
		Recipes.shapedRecipe(CustomItems.ITEMMAGNIFYINGGLASS, 2, 
				Fuzzy.GLASS_PANE, FUZZY_ITEMBLOCKFILLER,
				FUZZY_ITEMBLOCKFILLER, Fuzzy.WOOD_STICK
		);
		
		// @formatter:on
	}

}

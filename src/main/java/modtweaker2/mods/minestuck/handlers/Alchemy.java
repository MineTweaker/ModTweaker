package modtweaker2.mods.minestuck.handlers;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.util.BaseDescriptionAddition;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.minestuck.Alchemy")
public class Alchemy {
	
	@ZenMethod
	public static void setCost(IItemStack iStack, String cost) {
		ItemStack stack = InputHelper.toStack(iStack);
		MineTweakerAPI.apply(new SetCost(stack.getItem(), stack.getItemDamage(), cost));
	}
	
	@ZenMethod
	public static void setOreDictCost(String name, String cost) {
		MineTweakerAPI.apply(new SetCost(name, OreDictionary.WILDCARD_VALUE, cost));
	}
	
	@ZenMethod
	public static void removeCost(IItemStack iStack) {
		ItemStack stack = InputHelper.toStack(iStack);
		MineTweakerAPI.apply(new SetCost(stack.getItem(), stack.getItemDamage(), null));
	}
	
	@ZenMethod
	public static void removeOreDictCost(String name) {
		MineTweakerAPI.apply(new SetCost(name, OreDictionary.WILDCARD_VALUE, null));
	}
	
	private static class SetCost extends BaseDescriptionAddition {
		
		private final List<Object> items;
		private final GristSet cost;
		private GristSet costOld;
		
		public SetCost(Object item, int metadata, String cost) {
			super("Grist Cost");
			items = Arrays.asList(item, metadata);
			this.cost = getGrist(cost);
		}
		
		@Override
		public void apply() {
			costOld = GristRegistry.getAllConversions().remove(items);
			if(cost != null)
				GristRegistry.getAllConversions().put(items, cost);
		}
		
		@Override
		public void undo() {
			if(costOld == null)
				GristRegistry.getAllConversions().remove(items);
			else GristRegistry.getAllConversions().put(items, costOld);
		}
		
		@Override
		public String getRecipeInfo()
		{
			if(items.get(0) instanceof String)
				return items.get(0).toString();
			else if(items.get(1).equals(OreDictionary.WILDCARD_VALUE))
				return StatCollector.translateToLocal(((Item) items.get(0)).getUnlocalizedName());
			ItemStack stack = new ItemStack((Item) items.get(0), 1, (Integer) items.get(1));
			return stack.getDisplayName();
		}
		
		private static GristSet getGrist(String str)
		{
			if(str == null)
				return null;
			
			GristSet grist = new GristSet();
			String[] values = str.split(",");
			for (String value : values) {
				if (value.startsWith(" ")) value = value.replaceFirst(" ", "");
				String[] gristAmount = value.split("\\s+");
				if (gristAmount.length == 2)
					grist.addGrist(GristType.getTypeFromString(gristAmount[0]), Integer.parseInt(gristAmount[1]));
			}
			
			return grist;
		}
	}
}
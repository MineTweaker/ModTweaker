package modtweaker.mods.actuallyadditions.handlers;

import static modtweaker.helpers.InputHelper.toIItemStack;
import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.LensConversionRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.LogHelper;
import modtweaker.utils.BaseListAddition;
import modtweaker.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.actuallyadditions.AtomicReconstructor")
public class AtomicReconstructor {
	protected static final String name = "Actually Additions Atomic Reconstructor";

	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, int energyUse) {
		MineTweakerAPI.apply(new Add(new LensConversionRecipe(toStack(input), toStack(output), energyUse, ActuallyAdditionsAPI.lensDefaultConversion)));
	}

	private static class Add extends BaseListAddition<LensConversionRecipe> {
		public Add(LensConversionRecipe recipe) {
			super(AtomicReconstructor.name, ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES);
			
			this.recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(LensConversionRecipe recipe) {
			return LogHelper.getStackDescription(recipe.inputStack);
		}
	}

	@ZenMethod
	public static void remove(IIngredient output) {
		List<LensConversionRecipe> recipes = new LinkedList<LensConversionRecipe>();

		if (output == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		for (LensConversionRecipe recipe : ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES) {
			if (matches(output, toIItemStack(recipe.inputStack)))
				recipes.add(recipe);
		}

		if (!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", AtomicReconstructor.name,
					output.toString()));
		}

	}

	private static class Remove extends BaseListRemoval<LensConversionRecipe> {
		public Remove(List<LensConversionRecipe> recipes) {
			super(AtomicReconstructor.name, ActuallyAdditionsAPI.RECONSTRUCTOR_LENS_CONVERSION_RECIPES, recipes);
		}

		@Override
		protected String getRecipeInfo(LensConversionRecipe recipe) {
			return LogHelper.getStackDescription(recipe.inputStack);
		}
	}
}

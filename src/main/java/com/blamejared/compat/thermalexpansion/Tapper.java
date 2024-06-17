package com.blamejared.compat.thermalexpansion;

import cofh.thermalexpansion.util.managers.machine.FurnaceManager;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.thermalexpansion.Tapper")
@ModOnly("thermalexpansion")
@ZenRegister
public class Tapper {
    
    @ZenMethod
    public static void addRecipe(ILiquidStack output, IItemStack input, int energy) {
        ModTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(output), InputHelper.toStack(input)));
    }
    
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input)));
    }
    
    private static class Add extends BaseAction {
        
        private ItemStack input;
        private LiquidStack output;
        
        public Add(LiquidStack output, ItemStack input) {
            super("Tapper");
            this.output = output;
            this.input = input;
            this.energy = energy;
        }
        
        @Override
        public void apply() {
            TapperManager.addStandardMapping(input, output);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private ItemStack input;
        
        public Remove(ItemStack input) {
            super("Tapper");
            this.input = input;
        }
        
        @Override
        public void apply() {
            if(!TapperManager.recipeExists(input, false)) {
                CraftTweakerAPI.logError("No Furnace recipe exists for: " + input);
                return;
            }
            FurnaceManager.removeRecipe(input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(input);
        }
    }
}

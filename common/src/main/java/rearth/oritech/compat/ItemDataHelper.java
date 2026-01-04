package rearth.oritech.compat;

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Compatibility layer for MC 1.21's DataComponent system.
 * In MC 1.20.1, we use NBT tags instead.
 */
public final class ItemDataHelper {
    
    // NBT Keys
    public static final String IS_AOE_ACTIVE_KEY = "oritech:is_aoe_active";
    public static final String TARGET_POSITION_KEY = "oritech:target_position";
    public static final String STORED_FLUID_KEY = "oritech:stored_fluid";
    public static final String ADDON_DATA_KEY = "oritech:addon_data";
    public static final String ENERGY_KEY = "oritech:energy";
    
    // Boolean data
    public static boolean getBoolean(ItemStack stack, String key, boolean defaultValue) {
        if (stack.hasTag() && stack.getTag().contains(key)) {
            return stack.getTag().getBoolean(key);
        }
        return defaultValue;
    }
    
    public static void setBoolean(ItemStack stack, String key, boolean value) {
        stack.getOrCreateTag().putBoolean(key, value);
    }
    
    // Long data (for energy)
    public static long getLong(ItemStack stack, String key, long defaultValue) {
        if (stack.hasTag() && stack.getTag().contains(key)) {
            return stack.getTag().getLong(key);
        }
        return defaultValue;
    }
    
    public static void setLong(ItemStack stack, String key, long value) {
        stack.getOrCreateTag().putLong(key, value);
    }
    
    // BlockPos data
    @Nullable
    public static BlockPos getBlockPos(ItemStack stack, String key) {
        if (stack.hasTag() && stack.getTag().contains(key)) {
            return NbtUtils.readBlockPos(stack.getTag().getCompound(key));
        }
        return null;
    }
    
    public static void setBlockPos(ItemStack stack, String key, BlockPos pos) {
        stack.getOrCreateTag().put(key, NbtUtils.writeBlockPos(pos));
    }
    
    // FluidStack data
    public static FluidStack getFluidStack(ItemStack stack, String key) {
        if (stack.hasTag() && stack.getTag().contains(key)) {
            return FluidStack.read(stack.getTag().getCompound(key));
        }
        return FluidStack.empty();
    }
    
    public static void setFluidStack(ItemStack stack, String key, FluidStack fluid) {
        CompoundTag tag = new CompoundTag();
        fluid.write(tag);
        stack.getOrCreateTag().put(key, tag);
    }
    
    // CompoundTag data (for complex objects like AddonData)
    @Nullable
    public static CompoundTag getCompoundTag(ItemStack stack, String key) {
        if (stack.hasTag() && stack.getTag().contains(key)) {
            return stack.getTag().getCompound(key);
        }
        return null;
    }
    
    public static void setCompoundTag(ItemStack stack, String key, CompoundTag tag) {
        stack.getOrCreateTag().put(key, tag);
    }
    
    // AOE Active helpers
    public static boolean isAoeActive(ItemStack stack) {
        return getBoolean(stack, IS_AOE_ACTIVE_KEY, false);
    }
    
    public static void setAoeActive(ItemStack stack, boolean active) {
        setBoolean(stack, IS_AOE_ACTIVE_KEY, active);
    }
    
    // Target Position helpers
    @Nullable
    public static BlockPos getTargetPosition(ItemStack stack) {
        return getBlockPos(stack, TARGET_POSITION_KEY);
    }
    
    public static void setTargetPosition(ItemStack stack, BlockPos pos) {
        setBlockPos(stack, TARGET_POSITION_KEY, pos);
    }
    
    // Stored Fluid helpers
    public static FluidStack getStoredFluid(ItemStack stack) {
        return getFluidStack(stack, STORED_FLUID_KEY);
    }
    
    public static void setStoredFluid(ItemStack stack, FluidStack fluid) {
        setFluidStack(stack, STORED_FLUID_KEY, fluid);
    }
    
    // Energy helpers
    public static long getEnergy(ItemStack stack) {
        return getLong(stack, ENERGY_KEY, 0L);
    }
    
    public static void setEnergy(ItemStack stack, long energy) {
        setLong(stack, ENERGY_KEY, energy);
    }
    
    private ItemDataHelper() {}
}

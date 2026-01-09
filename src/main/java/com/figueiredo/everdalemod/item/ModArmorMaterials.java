package com.figueiredo.everdalemod.item;

import com.figueiredo.everdalemod.EverdaleMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    TIN("tin", 10, 7, new int[]{ 5, 7, 6, 4 }, SoundEvents.ARMOR_EQUIP_CHAIN,
            1f, 0f, () -> Ingredient.of(ModItems.TIN.get()));

    private final String name;
    private final int durabilityMultiplier;
    private final int enchantmentValue;
    private final int[] protectionAmounts;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float KnockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    private static final int[] BASE_DURABILITY = {11, 16, 16, 13};

    ModArmorMaterials(String name, int durabilityMultiplier, int enchantmentValue, int[] protectionAmounts, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.enchantmentValue = enchantmentValue;
        this.protectionAmounts = protectionAmounts;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.KnockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type pType) {
        return BASE_DURABILITY[pType.ordinal()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type pType) {
        return this.protectionAmounts[pType.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return EverdaleMod.MOD_ID + ':' + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.KnockbackResistance;
    }
}

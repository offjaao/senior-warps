package com.github.offjaao.warps.utils.inventory.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

/**
 * @author SaiintBrisson
 */

public class ItemBuilder {

    private ItemStack is;

    public ItemBuilder() {
    }

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder(int id) {
        this(Material.getMaterial(id));
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder(Material m, int amount) {
        is = new ItemStack(m, amount);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(is);
    }

    public ItemBuilder durability(int dur) {
        is.setDurability((short) dur);
        return this;
    }

    public ItemBuilder name(String name) {
        if (name == null)
            return this;
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setType(Material material) {
        is.setType(material);
        return this;
    }

    public ItemBuilder type(Material material) {
        is = new ItemStack(material, 1);
        return this;
    }

    public ItemBuilder type(int id) {
        return type(Material.getMaterial(id));
    }

    public ItemBuilder unsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder owner(String owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemBuilder enchant(Enchantment ench, int level) {
        ItemMeta im = is.getItemMeta();
        im.addEnchant(ench, level, true);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder infinityDurability() {
        is.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder lore(ArrayList<String> lore) {
        if (lore == null)
            return this;
        ItemMeta im = is.getItemMeta();
        List<String> out = im.getLore() == null ? new ArrayList<>() : im.getLore();
        for (String string : lore)
            out.add(ChatColor.translateAlternateColorCodes('&', string));
        im.setLore(out);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        if (lore == null)
            return this;
        ItemMeta im = is.getItemMeta();
        List<String> out = im.getLore() == null ? new ArrayList<>() : im.getLore();
        for (String string : lore)
            out.add(ChatColor.translateAlternateColorCodes('&', string));
        im.setLore(out);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder armorColor(Color cor) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            im.setColor(cor);
            is.setItemMeta(im);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        is.getItemMeta().setLore(lore);
        return this;
    }

    public List<String> getLore() {
        return is.getItemMeta().getLore();
    }

    public ItemBuilder woolColor(DyeColor color) {
        if (!is.getType().equals(Material.WOOL))
            return this;
        is.setDurability(color.getWoolData());
        return this;
    }

    public ItemBuilder amount(int amount) {
        if (amount > 64)
            amount = 64;
        is.setAmount(amount);
        return this;
    }

    public ItemBuilder removeAttributes() {
        ItemMeta meta = is.getItemMeta();
        meta.addItemFlags(ItemFlag.values());
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeLore() {
        ItemMeta im = is.getItemMeta();
        im.setLore(new ArrayList<>());
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setWoolColor(DyeColor color) {
        if (!is.getType().equals(Material.WOOL))
            return this;
        this.is.setDurability(color.getDyeData());
        return this;
    }

    public ItemStack build() {
        return is;
    }
}
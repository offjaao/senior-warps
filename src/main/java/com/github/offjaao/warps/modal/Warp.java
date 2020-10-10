package com.github.offjaao.warps.modal;

import com.github.offjaao.warps.enums.WarpCategory;
import com.github.offjaao.warps.utils.inventory.menu.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@Data
@AllArgsConstructor
public class Warp {

    private final String name, owner;
    private String password;
    private WarpCategory category;
    private final Location location;
    private ItemStack icon;

    public ItemBuilder iconToBuilder() {
        return new ItemBuilder(icon).clone();
    }

    public boolean hasPassword() {
        return password != null;
    }

    public boolean isPrivate() {
        return category == WarpCategory.PRIVATE;
    }
}

package com.github.offjaao.warps.minecraft.inventories;

import com.github.offjaao.warps.WarpsPlugin;
import com.github.offjaao.warps.enums.WarpCategory;
import com.github.offjaao.warps.modal.Warp;
import com.github.offjaao.warps.modal.cache.WarpCache;
import com.github.offjaao.warps.utils.inventory.Pagination;
import com.github.offjaao.warps.utils.inventory.menu.ItemBuilder;
import com.github.offjaao.warps.utils.inventory.menu.Menu;
import com.github.offjaao.warps.utils.inventory.menu.MenuItem;
import com.github.offjaao.warps.utils.inventory.menu.MenuNode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerWarpsInventory extends Menu<Player> {

    private final WarpCache warpCache = WarpsPlugin.getInstance().getWarpCache();
    public ServerWarpsInventory() {
        super(4, "Server warps");
    }

    @Override
    protected MenuNode render(Player player, MenuNode node) {
        List<MenuItem> items = new ArrayList<>();

        int i = 1;
        for (Map.Entry<String, Warp> entry : warpCache.getCache().asMap().entrySet()) {
            Warp warp = entry.getValue();
            if (warp.getCategory() == WarpCategory.SERVER) {
                items.add(
                        MenuItem.builder()
                                .item(warp.getIcon())
                                .eventConsumer(event -> {
                                    event.setCancelled(true);
                                    Player whoClicked = ((Player) event.getWhoClicked());
                                    whoClicked.teleport(warp.getLocation());
                                    whoClicked.sendMessage("§aTeleported to " + warp.getName() + ".");
                                })
                                .build()
                );
                i++;
            }
        }
        Pagination pagination = new Pagination(items, node.getTitle(), 4);
        pagination.addFixedItem(MenuItem.builder()
                .slot(31)
                .item(new ItemBuilder(Material.FEATHER).name("§cBack to menu").build())
                .eventConsumer(event -> {
                    Player whoClicked = (Player) event.getWhoClicked();
                    event.setCancelled(true);
                    new WarpMainInventory().prepare(whoClicked).render(whoClicked);
                })
                .build());
        return pagination.getInventory();
    }
}

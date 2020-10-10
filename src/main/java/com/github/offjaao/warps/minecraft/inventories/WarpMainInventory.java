package com.github.offjaao.warps.minecraft.inventories;

import com.github.offjaao.warps.WarpsPlugin;
import com.github.offjaao.warps.entity.UserWarp;
import com.github.offjaao.warps.entity.cache.UserCache;
import com.github.offjaao.warps.utils.inventory.menu.ItemBuilder;
import com.github.offjaao.warps.utils.inventory.menu.Menu;
import com.github.offjaao.warps.utils.inventory.menu.MenuItem;
import com.github.offjaao.warps.utils.inventory.menu.MenuNode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WarpMainInventory extends Menu<Player> {

    private final ServerWarpsInventory serverWarpsInventory = new ServerWarpsInventory();
    private final PrivateWarpsInventory privateWarpsInventory = new PrivateWarpsInventory();
    private final PublicWarpsInventory publicWarpsInventory = new PublicWarpsInventory();
    private final UserCache userCache = WarpsPlugin.getInstance().getUserCache();

    public WarpMainInventory() {
        super(3, "Warps");
    }

    protected MenuNode render(Player player, MenuNode node) {
        UserWarp userWarp = userCache.get(player.getUniqueId());

        if (userWarp != null) {
            node.appendItem(MenuItem.builder()
                    .slot(11)
                    .item(new ItemBuilder(Material.DARK_OAK_DOOR_ITEM)
                            .name("§aYour private warps")
                            .lore("§7All your private warps.")
                            .build()
                    )
                    .eventConsumer(event -> {
                        event.setCancelled(true);
                        Player whoClicked = (Player) event.getWhoClicked();
                        privateWarpsInventory.prepare(userWarp).render(whoClicked);
                    })
                    .build());

            node.appendItem(MenuItem.builder()
                    .slot(13)
                    .item(new ItemBuilder(Material.GRASS)
                            .name("§aYour public warps")
                            .lore("§7All your public warps.")
                            .build()
                    )
                    .eventConsumer(event -> {
                        event.setCancelled(true);
                        Player whoClicked = (Player) event.getWhoClicked();
                        publicWarpsInventory.prepare(userWarp).render(whoClicked);
                    })
                    .build());

            node.appendItem(MenuItem.builder()
                    .slot(15)
                    .item(new ItemBuilder(Material.COMPASS)
                            .name("§aServer Warps")
                            .lore("§7All server warps.")
                            .build()
                    )
                    .eventConsumer(event -> {
                        event.setCancelled(true);
                        Player whoClicked = (Player) event.getWhoClicked();
                        serverWarpsInventory.prepare(whoClicked).render(whoClicked);
                    })
                    .build());
        }
        return node;
    }


}

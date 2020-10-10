package com.github.offjaao.warps.minecraft.inventories;

import com.github.offjaao.warps.WarpsPlugin;
import com.github.offjaao.warps.entity.UserWarp;
import com.github.offjaao.warps.enums.WarpCategory;
import com.github.offjaao.warps.manager.WarpManager;
import com.github.offjaao.warps.modal.Warp;
import com.github.offjaao.warps.utils.ChatAsker;
import com.github.offjaao.warps.utils.inventory.Pagination;
import com.github.offjaao.warps.utils.inventory.menu.ItemBuilder;
import com.github.offjaao.warps.utils.inventory.menu.Menu;
import com.github.offjaao.warps.utils.inventory.menu.MenuItem;
import com.github.offjaao.warps.utils.inventory.menu.MenuNode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PublicWarpsInventory extends Menu<UserWarp> {

    private final EditWarpInventory editWarpInventory = new EditWarpInventory();
    private final WarpManager warpManager = WarpsPlugin.getInstance().getWarpManager();

    public PublicWarpsInventory() {
        super(4, "Public warps");
    }

    @Override
    protected MenuNode render(UserWarp userWarp, MenuNode node) {
        List<MenuItem> items = new ArrayList<>();
        for (Warp warp : userWarp.getWarpsByCategory(WarpCategory.PUBLIC)) {
            items.add(
                    MenuItem.builder()
                            .item(warp.getIcon())
                            .eventConsumer(event -> {
                                event.setCancelled(true);
                                Player whoClicked = ((Player) event.getWhoClicked());
                                switch (event.getClick()) {
                                    case LEFT:
                                        if (!warp.hasPassword()) {
                                            whoClicked.teleport(warp.getLocation());
                                            whoClicked.sendMessage("§aTeleported to " + warp.getName() + ".");
                                            return;
                                        }
                                        whoClicked.closeInventory();
                                        ChatAsker.builder()
                                                .messages("§eWhat is the password for this warp?")
                                                .onComplete((player, s) -> {
                                                    if (s.equals(warp.getPassword())) {
                                                        player.teleport(warp.getLocation());
                                                        whoClicked.sendMessage("§aTeleported to " + warp.getName() + ".");
                                                        return;
                                                    }
                                                    player.sendMessage("§cIncorrect password.");
                                                }).build().addPlayer(whoClicked);
                                        break;
                                    case SHIFT_RIGHT:
                                        if (!warp.getOwner().equalsIgnoreCase(whoClicked.getName())) {
                                            return;
                                        }
                                        editWarpInventory.prepare(warp).render(whoClicked);
                                        break;
                                }
                            })
                            .build()
            );
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

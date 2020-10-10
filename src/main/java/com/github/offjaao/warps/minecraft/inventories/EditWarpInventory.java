package com.github.offjaao.warps.minecraft.inventories;

import com.github.offjaao.warps.WarpsPlugin;
import com.github.offjaao.warps.entity.UserWarp;
import com.github.offjaao.warps.entity.cache.UserCache;
import com.github.offjaao.warps.enums.WarpCategory;
import com.github.offjaao.warps.manager.WarpManager;
import com.github.offjaao.warps.modal.Warp;
import com.github.offjaao.warps.utils.ChatAsker;
import com.github.offjaao.warps.utils.GlowUtils;
import com.github.offjaao.warps.utils.inventory.menu.ItemBuilder;
import com.github.offjaao.warps.utils.inventory.menu.Menu;
import com.github.offjaao.warps.utils.inventory.menu.MenuItem;
import com.github.offjaao.warps.utils.inventory.menu.MenuNode;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditWarpInventory extends Menu<Warp> {

    private final WarpManager warpManager = WarpsPlugin.getInstance().getWarpManager();
    private final UserCache userCache = WarpsPlugin.getInstance().getUserCache();

    public EditWarpInventory() {
        super(6, "Editing warp");
    }

    protected MenuNode render(Warp warp, MenuNode node) {
        node.appendItem(MenuItem.builder()
                .slot(4)
                .item(new ItemBuilder(Material.DARK_OAK_DOOR_ITEM)
                        .name("§7Warp: §f" + warp.getName())
                        .lore("§7Editing...")
                        .build()
                )
                .build()
        );

        node.appendItem(MenuItem.builder()
                .slot(20)
                .item(new ItemBuilder(Material.ANVIL)
                        .name("§eEdit name")
                        .lore("§7Click to edit a item name.")
                        .build()
                )
                .eventConsumer(event -> {
                    event.setCancelled(true);
                    Player whoClicked = (Player) event.getWhoClicked();
                    whoClicked.closeInventory();
                    ChatAsker.builder()
                            .messages("", "§aWhat will be the new name of this item?", "§7Type cancel to exit.", "")
                            .onComplete((player, s) -> {
                                if (s.equalsIgnoreCase("cancel")) {
                                    player.sendMessage("§cCanceled.");
                                    return;
                                }
                                ItemStack build = warp.iconToBuilder().name(s.replace("&", "§")).build();
                                warp.setIcon(build);
                                player.sendMessage("§aNew name has defined.");
                                warpManager.updateWarp(warp);
                            }).build().addPlayer(whoClicked);
                })
                .build()
        );

        node.appendItem(MenuItem.builder()
                .slot(22)
                .item(new ItemBuilder(Material.BOOK_AND_QUILL)
                        .name("§eEdit lore")
                        .lore("§7Click to edit a item lore.")
                        .build()
                )
                .eventConsumer(event -> {
                    Player whoClicked = ((Player) event.getWhoClicked());
                    whoClicked.closeInventory();
                    ChatAsker.builder()
                            .messages("", "§aWhat will be the new lore of this item?", "§7Type cancel to exit.", "")
                            .onComplete((player, s) -> {
                                if (s.equalsIgnoreCase("cancel")) {
                                    player.sendMessage("§cCanceled.");
                                    return;
                                }
                                String[] split = s.split(";");
                                ItemStack build = warp.iconToBuilder().removeLore().lore(split).build();
                                warp.setIcon(build);
                                player.sendMessage("§aNew lore has defined.");
                                warpManager.updateWarp(warp);
                            }).build().addPlayer(whoClicked);
                })
                .build()
        );

        node.appendItem(MenuItem.builder()
                .slot(24)
                .item(new ItemBuilder(Material.STONE)
                        .name("§eEdit material")
                        .lore("§7Click to edit a material icon.")
                        .build()
                )
                .eventConsumer(event -> {
                    Player whoClicked = ((Player) event.getWhoClicked());
                    whoClicked.closeInventory();
                    ChatAsker.builder()
                            .messages("", "§aWhat will be the new material id of this item?", "§7Type cancel to exit.", "")
                            .onComplete((player, s) -> {
                                if (s.equalsIgnoreCase("cancel")) {
                                    player.sendMessage("§cCanceled.");
                                    return;
                                }
                                int i = NumberUtils.toInt(s, 1);
                                Material material = Material.getMaterial(i);
                                if (material == null) {
                                    player.sendMessage("§cMaterial not founded.");
                                    return;
                                }
                                ItemBuilder type = warp.iconToBuilder().setType(material);
                                warp.setIcon(type.build());
                                player.sendMessage("§aNew icon has defined.");
                                warpManager.updateWarp(warp);
                            }).build().addPlayer(whoClicked);
                })
                .build()
        );

        node.appendItem(MenuItem.builder()
                .slot(30)
                .item(new ItemBuilder(Material.GLOWSTONE_DUST)
                        .name("§eEdit glow")
                        .lore("§7Change your icon is glowing.")
                        .build()
                )
                .eventConsumer(event -> {
                    event.setCancelled(true);
                    ItemStack icon = warp.getIcon();
                    if (!GlowUtils.hasGlow(icon)) {
                        GlowUtils.addGlow(icon);
                    } else {
                        GlowUtils.removeGlow(icon);
                    }
                    warp.setIcon(icon);
                    warpManager.updateWarp(warp);
                })
                .build()
        );

        node.appendItem(MenuItem.builder()
                .slot(32)
                .item(new ItemBuilder(Material.GOLD_NUGGET)
                        .name("§eEdit warp type")
                        .lore("§7Change your warp is public/private.", "§7Current: §f" + warp.getCategory().name())
                        .build()
                )
                .eventConsumer(event -> {
                    event.setCancelled(true);
                    if (warp.isPrivate()) {
                        warp.setCategory(WarpCategory.PUBLIC);
                    } else warp.setCategory(WarpCategory.PRIVATE);
                    warpManager.updateWarp(warp);
                    prepare(warp).render((Player) event.getWhoClicked());
                })
                .build()
        );

        node.appendItem(MenuItem.builder()
                .slot(48)
                .item(new ItemBuilder(Material.FEATHER).name("§cBack to menu").build())
                .eventConsumer(event -> {
                    Player whoClicked = (Player) event.getWhoClicked();
                    event.setCancelled(true);
                    new WarpMainInventory().prepare(whoClicked).render(whoClicked);
                })
                .build());

        node.appendItem(MenuItem.builder()
                .slot(49)
                .item(new ItemBuilder(Material.BARRIER).name("§cDelete warp").build())
                .eventConsumer(event -> {
                    Player whoClicked = (Player) event.getWhoClicked();
                    UserWarp userWarp = userCache.get(whoClicked.getUniqueId());
                    event.setCancelled(true);
                    whoClicked.sendMessage("§c'" + warp.getName() + "' warp deleted.");
                    whoClicked.closeInventory();
                    warpManager.deleteWarp(warp, userWarp);
                })
                .build());

        node.appendItem(MenuItem.builder()
                .slot(50)
                .item(new ItemBuilder(Material.CHEST).name("§eSet password")
                        .lore("§7Change or remove warp password.",
                                "§7Current password: §f" + (warp.getPassword() == null ? "§cNot defined" : warp.getPassword())).build())
                .eventConsumer(event -> {
                    Player whoClicked = (Player) event.getWhoClicked();
                    event.setCancelled(true);
                    whoClicked.closeInventory();
                    ChatAsker.builder()
                            .messages("",
                                    "§aEnter the new password for this warp!",
                                    "§7Type remove to remove the password and cancel to exit.",
                                    "")
                            .onComplete((player, s) -> {
                                if (s.equalsIgnoreCase("remove")) {
                                    warp.setPassword(null);
                                    player.sendMessage("§cCanceled.");
                                    return;
                                } else if (s.equalsIgnoreCase("cancel")) {
                                    player.sendMessage("§cCanceled");
                                    return;
                                }
                                warp.setPassword(s);
                                player.sendMessage("§aNew password has defined. §7(" + warp.getPassword() + ")");
                                warpManager.updateWarp(warp);
                            }).build().addPlayer(whoClicked);
                })
                .build());
        return node;
    }
}

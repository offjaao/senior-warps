package com.github.offjaao.warps.minecraft.commands;

import com.github.offjaao.warps.WarpsPlugin;
import com.github.offjaao.warps.database.Storage;
import com.github.offjaao.warps.entity.UserWarp;
import com.github.offjaao.warps.entity.cache.UserCache;
import com.github.offjaao.warps.enums.WarpCategory;
import com.github.offjaao.warps.manager.WarpManager;
import com.github.offjaao.warps.modal.Warp;
import com.github.offjaao.warps.modal.cache.WarpCache;
import com.github.offjaao.warps.utils.inventory.menu.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CreateWarpCommand implements CommandExecutor {

    private final WarpCache warpCache = WarpsPlugin.getInstance().getWarpCache();
    private final UserCache userCache = WarpsPlugin.getInstance().getUserCache();
    private final WarpManager warpManager = WarpsPlugin.getInstance().getWarpManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        UserWarp userWarp = userCache.get(player.getUniqueId());

        if (args.length != 2) {
            player.sendMessage("§cUsage: /createwarp <name> <type>");
            return false;
        }

        String name = args[0];
        String category = args[1];

        if (category.equalsIgnoreCase("SERVER") && !player.hasPermission("warp.create.global")) {
            player.sendMessage("§cYou not have permission to create server warps.");
            return false;
        }

        WarpCategory warpCategory = WarpCategory.valueOf(category.toUpperCase());
        Warp warpName = warpCache.get(name);

        if (warpName != null && warpName.getCategory() == warpCategory) {
            player.sendMessage("§cWarp '" + name + "' already created.");
            return false;
        }

        ItemStack itemStack = new ItemBuilder(Material.STONE)
                .name("§7" + name)
                .lore("§eClick to teleport!")
                .build();
        String ownerValue = warpCategory != WarpCategory.SERVER ? player.getName() : "Server";

        Warp warp = new Warp(
                name,
                ownerValue,
                null,
                warpCategory,
                player.getLocation(),
                itemStack
        );
        if (warp.getCategory() == WarpCategory.PUBLIC || warp.getCategory() == WarpCategory.PRIVATE) {
            userWarp.addWarp(warp);
        }
        warpManager.saveWarp(warp);
        return false;
    }
}

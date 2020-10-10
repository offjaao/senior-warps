package com.github.offjaao.warps.minecraft.commands;

import com.github.offjaao.warps.minecraft.inventories.WarpMainInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private final WarpMainInventory mainInventory = new WarpMainInventory();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        mainInventory.prepare(player).render(player);

        return false;
    }

}

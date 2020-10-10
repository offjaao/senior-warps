package com.github.offjaao.warps.minecraft.listeners;

import com.github.offjaao.warps.utils.inventory.menu.MenuHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof MenuHolder) {
            ((MenuHolder) event.getInventory()
                    .getHolder())
                    .getNode()
                    .processClick(event);
        }
    }


}

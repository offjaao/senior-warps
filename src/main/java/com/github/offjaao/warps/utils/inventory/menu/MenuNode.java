package com.github.offjaao.warps.utils.inventory.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * @author SaiintBrisson
 */
@Getter
public class MenuNode {

	@Setter
	private int size;
	@Setter
	private String title;

	private final MenuItem[] items;

	public MenuNode(int size, String title, MenuItem[] items) {
		this.size = size;
		this.title = title;

		if(items != null) {
			this.items = items;
		} else {
			this.items = new MenuItem[size];
		}
	}

	public MenuNode appendItem(MenuItem item) {

		items[item.getSlot()] = item;

		return this;
	}

	public void render(Player player) {
		MenuHolder holder = new MenuHolder(player.getUniqueId(), this);
		Inventory inventory = Bukkit.createInventory(holder, size, title);

		for (MenuItem item : items) {
			if (item == null)
				continue;

			inventory.setItem(item.getSlot(), item.getItem());
		}

		holder.setInventory(inventory);
		player.openInventory(inventory);
	}

	public boolean processClick(InventoryClickEvent event) {
		if (event.getRawSlot() < 0 || event.getRawSlot() > items.length) {
			event.setCancelled(true);
			return false;
		}

		if (items[event.getRawSlot()] == null || event.getCurrentItem() == null) {
			event.setCancelled(true);
			return false;
		}

		MenuItem item = items[event.getRawSlot()];

		if (item == null || item.getEventConsumer() == null) {
			event.setCancelled(true);
			return false;
		}

		item.getEventConsumer().accept(event);

		return true;
	}

}
package com.github.offjaao.warps.utils.inventory.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author SaiintBrisson
 */
@Getter
@Builder
public class MenuItem {

	@Setter
	private int slot;
	private ItemStack item;

	private Consumer<InventoryClickEvent> eventConsumer;

}
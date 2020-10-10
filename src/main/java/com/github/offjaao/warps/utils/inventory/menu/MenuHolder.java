package com.github.offjaao.warps.utils.inventory.menu;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;


/**
 * @author SaiintBrisson
 */
@Getter
@RequiredArgsConstructor
public class MenuHolder implements InventoryHolder {

	@Setter
	private Inventory inventory;

	@NonNull
	private final UUID holder;

	@NonNull
	private final MenuNode node;

}
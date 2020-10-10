package com.github.offjaao.warps.utils.inventory.menu;

import lombok.Getter;

/**
 * @author SaiintBrisson
 */

@Getter
public abstract class Menu<T> {

	private String title;

	private int size;

	private MenuItem[] items;

	public Menu(int size, String title) {

		this.size = size * 9;
		this.title = title;

		this.items = new MenuItem[this.size];
	}

	public Menu appendItem(MenuItem item) {

		items[item.getSlot()] = item;

		return this;
	}

	protected MenuNode render(T variable, MenuNode node) {return node;}

	public final MenuNode prepare(T variable) {
		MenuNode node = new MenuNode(size, title, items);
		return render(variable, node);
	}

}
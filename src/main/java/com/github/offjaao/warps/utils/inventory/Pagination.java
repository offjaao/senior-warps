package com.github.offjaao.warps.utils.inventory;

import com.github.offjaao.warps.utils.inventory.menu.ItemBuilder;
import com.github.offjaao.warps.utils.inventory.menu.MenuItem;
import com.github.offjaao.warps.utils.inventory.menu.MenuNode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * @author SaiintBrisson
 */

public class Pagination extends LinkedList<MenuItem> {

    private String title;
    private int rows;

    private int totalPages;
    private int pageSize;

    private int currentPage = 0;

    private List<MenuItem> fixedItems;

    public Pagination(Collection<? extends MenuItem> items, String title, int rows) {
        super(items);

        this.title = title;
        this.rows = rows;

        if (rows < 3)
            throw new IllegalStateException("Inventory rows must be 3 or greater");

        int sideBars = (rows - 2) * 2;
        this.pageSize = (rows * 9) - (18 + sideBars);

        this.totalPages = (int) Math.ceil((double) items.size() / (double) pageSize);

        this.fixedItems = new ArrayList<>();
    }

    public MenuNode getInventory() {
        currentPage = 0;

        return build();
    }

    public MenuNode nextInventory() {
        if (currentPage == totalPages)
            return null;

        if (currentPage + 1 < totalPages)
            currentPage++;

        return build();
    }

    public MenuNode previousInventory() {
        if (currentPage == 0)
            return null;

        currentPage--;

        return build();
    }

    private MenuNode build() {
        MenuNode builder = new MenuNode(rows * 9,
                title, null);
        fill(builder);


        for (MenuItem fixedItem : fixedItems) {
            builder.appendItem(fixedItem);
        }

        addFunctions(builder);

        return builder;
    }

    public void addFixedItem(MenuItem item) {
        fixedItems.add(item);
    }

    private void fill(MenuNode node) {
        int current = 10;

        for (int i = 0; i < pageSize; ) {
            if ((currentPage * pageSize) + i >= size())
                break;

            if (current >= (rows * 9) - 10) {
                break;
            }

            if (current % 9 == 0 || current % 9 == 8) {
                current++;
                continue;
            }

            MenuItem item;
            try {
                item = get((currentPage * pageSize) + i);
            } catch (Exception e) {
                current++;
                continue;
            }

            if (item == null) {
                current++;
                continue;
            }

            item.setSlot(current);
            node.appendItem(item);

            i++;
            current++;
        }
    }

    private void addFunctions(MenuNode builder) {
        if (currentPage + 1 < totalPages) {
            MenuItem item = MenuItem.builder()
                    .item(
                            new ItemBuilder(Material.ARROW)
                                    .name("§aNext ->")
                                    .build()
                    )
                    .eventConsumer(event -> {
                        event.setCancelled(true);

                        MenuNode nextInv = nextInventory();
                        if (nextInv != null)
                            nextInv.render((Player) event.getWhoClicked());
                    })
                    .slot((rows * 9) - 1)
                    .build();

            builder.appendItem(item);
        }

        if (currentPage > 0) {
            MenuItem item = MenuItem.builder()
                    .item(
                            new ItemBuilder(Material.ARROW)
                                    .name("§c<- Previous")
                                    .build()
                    )
                    .eventConsumer(event -> {
                        event.setCancelled(true);

                        MenuNode previousInv = previousInventory();
                        if (previousInv != null)
                            previousInv.render((Player) event.getWhoClicked());
                    })
                    .slot((rows * 9) - 9)
                    .build();

            builder.appendItem(item);
        }
    }

}
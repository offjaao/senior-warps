package com.github.offjaao.warps.minecraft.listeners;

import com.github.offjaao.warps.WarpsPlugin;
import com.github.offjaao.warps.entity.UserWarp;
import com.github.offjaao.warps.entity.cache.UserCache;
import com.github.offjaao.warps.enums.WarpCategory;
import com.github.offjaao.warps.manager.WarpManager;
import com.github.offjaao.warps.modal.Warp;
import com.github.offjaao.warps.modal.cache.WarpCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PlayerConnectionListener implements Listener {

    private final UserCache userCache = WarpsPlugin.getInstance().getUserCache();
    private final WarpCache warpCache = WarpsPlugin.getInstance().getWarpCache();
    private final WarpManager warpManager = WarpsPlugin.getInstance().getWarpManager();
    private final ExecutorService executorService = new ThreadPoolExecutor(
            2, 4, 15, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>()
    );

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserWarp userWarp = new UserWarp(player.getName(), player.getUniqueId());
        executorService.submit(() -> {
            List<Warp> warps = warpManager.loadWarps(player.getName(), WarpCategory.PRIVATE);
            if (warps.size() >= 1) {
                userWarp.setWarps(warps);
            } else
                userWarp.setWarps(new ArrayList<>());
            userCache.put(player.getUniqueId(), userWarp);
        });
        userWarp.validateWarpLimit();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uniqueId = event.getPlayer().getUniqueId();
        UserWarp userWarp = userCache.get(uniqueId);
        if (userWarp == null) return;

        executorService.submit(() -> {
            userCache.remove(uniqueId);
            userWarp.getWarpsByCategory(WarpCategory.PRIVATE).forEach(warp -> warpCache.remove(warp.getName()));
        });
    }


}

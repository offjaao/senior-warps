package com.github.offjaao.warps.manager;

import com.github.offjaao.warps.WarpsPlugin;
import com.github.offjaao.warps.database.Storage;
import com.github.offjaao.warps.entity.UserWarp;
import com.github.offjaao.warps.enums.WarpCategory;
import com.github.offjaao.warps.modal.Warp;
import com.github.offjaao.warps.modal.cache.WarpCache;
import java.util.List;

public class WarpManager {

    private final Storage storage = WarpsPlugin.getInstance().getStorage();
    private final WarpCache warpCache = WarpsPlugin.getInstance().getWarpCache();

    public void loadServerWarps() {
        storage.loadCategory(WarpCategory.SERVER).forEach(warp -> {
            warpCache.put(warp.getName(), warp);
        });
    }

    public void updateWarp(Warp warp) {
        storage.updateWarp(warp);
    }

    public List<Warp> loadWarps(String owner, WarpCategory category) {
        return storage.loadOwner(owner, category);
    }

    public void saveWarp(Warp warp) {
        warpCache.put(warp.getName(), warp);
        storage.saveWarp(warp);
    }

    public void deleteWarp(Warp warp, UserWarp userWarp) {
        userWarp.remove(warp);
        warpCache.remove(warp.getName());
        storage.deleteWarp(warp);
    }

}

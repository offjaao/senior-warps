package com.github.offjaao.warps.modal.cache;

import com.github.offjaao.warps.memory.cache.CacheAdapter;
import com.github.offjaao.warps.modal.Warp;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class WarpCache implements CacheAdapter<String, Warp> {

    private final Cache<String, Warp> warpCache =
            CacheBuilder.newBuilder()
                    .initialCapacity(200)
                    .concurrencyLevel(4)
                    .build();

    @Override
    public Cache<String, Warp> getCache() {
        return warpCache;
    }

    @Override
    public Warp get(String key) {
        return warpCache.getIfPresent(key);
    }

    @Override
    public void put(String key, Warp value) {
        warpCache.put(key, value);
    }

    @Override
    public void remove(String key) {
        warpCache.invalidate(key);
    }

    @Override
    public void clear() {
        warpCache.invalidateAll();
    }
}

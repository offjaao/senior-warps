package com.github.offjaao.warps.entity.cache;

import com.github.offjaao.warps.entity.UserWarp;
import com.github.offjaao.warps.memory.cache.CacheAdapter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;

public class UserCache implements CacheAdapter<UUID, UserWarp> {

    private final Cache<UUID, UserWarp> userCache =
            CacheBuilder.newBuilder()
                    .initialCapacity(200)
                    .concurrencyLevel(4)
                    .build();

    @Override
    public Cache<UUID, UserWarp> getCache() {
        return userCache;
    }

    @Override
    public UserWarp get(UUID key) {
        return userCache.getIfPresent(key);
    }

    @Override
    public void put(UUID key, UserWarp value) {
        userCache.put(key, value);
    }

    @Override
    public void remove(UUID key) {
        userCache.invalidate(key);
    }

    @Override
    public void clear() {
        userCache.invalidateAll();
    }
}

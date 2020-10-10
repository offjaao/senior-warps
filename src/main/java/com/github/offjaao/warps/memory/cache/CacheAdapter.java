package com.github.offjaao.warps.memory.cache;

import com.google.common.cache.Cache;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface CacheAdapter<K, V> extends Iterable<V> {

    Cache<K, V> getCache();

    V get(K key);
    void put(K key, V value);
    void remove(K key);

    void clear();

    @NotNull
    @Override
    default Iterator<V> iterator() {
        return getCache().asMap().values().iterator();
    }

}

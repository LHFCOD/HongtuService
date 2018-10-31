package com.hongtu.slice.component;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class GuanvaCacheGetter {
    LoadingCache<Object, Object> cache;
    private Logger LOGGER = LoggerFactory.getLogger(GuanvaCacheGetter.class);

    public GuanvaCacheGetter(Properties configuration) {
        Long guanvaCacheTime = 3600L;
        try {
            guanvaCacheTime = Long.valueOf(configuration.get("guanva.cacheTime").toString());
        } catch (Exception ex) {
            LOGGER.error("guanva.cacheTime failed : {}", Throwables.getStackTraceAsString(ex));
        }
        cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(guanvaCacheTime, TimeUnit.SECONDS)
                .build(createCacheLoader());
    }

    public static com.google.common.cache.CacheLoader<Object, Object> createCacheLoader() {
        return new com.google.common.cache.CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception {
                return new Object();
            }
        };
    }

    public Object get(Object key) {
        Object scoreResultList = cache.getIfPresent(key);
        return scoreResultList;
    }

    public void set(Object key, Object hits) {
        cache.put(key, hits);
    }
}


package com.travelrely.core.util;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import com.travelrely.core.nrs.Engine;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

public class BitmapCacheManager {

    public static final int MAX_SIZE = 50;

    public static final int SOFT_CACHE_SIZE = 15;

    LruCache<String, Bitmap> mLruCache;

    LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache;

    public static BitmapCacheManager instance;

    public static BitmapCacheManager getInstance() {
        if (instance == null) {
            instance = new BitmapCacheManager();
        }
        return instance;
    }

    private BitmapCacheManager() {

        int memClass = ((ActivityManager) Engine.getInstance().getContext()
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 4; // 硬引用缓存容量，为系统可用内存的1/4

        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue,
                    Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if (oldValue != null) {
                    mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
                }
            }

            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (value != null)
                    return value.getRowBytes() * value.getHeight();
                else
                    return 0;
            }
        };
        mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(SOFT_CACHE_SIZE) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(
                    java.util.Map.Entry<String, SoftReference<Bitmap>> eldest) {
                if (size() > SOFT_CACHE_SIZE) {
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * 从缓存中获取图片
     */
    public Bitmap getBitmapFromCache(String url) {
        Bitmap bitmap;
        // 先从硬引用缓存中获取
        synchronized (mLruCache) {
            bitmap = mLruCache.get(url);
            if (bitmap != null) {
                // 如果找到的话，把元素移到LinkedHashMap的最前面，从而保证在LRU算法中是最后被删除
                mLruCache.remove(url);
                mLruCache.put(url, bitmap);
                return Utils.headBitmap(bitmap);
            }
        }
        // 如果硬引用缓存中找不到，到软引用缓存中找
        synchronized (mSoftCache) {
            SoftReference<Bitmap> bitmapReference = mSoftCache.get(url);
            if (bitmapReference != null) {
                bitmap = bitmapReference.get();
                if (bitmap != null) {
                    // 将图片移回硬缓存
                    mLruCache.put(url, bitmap);
                    mSoftCache.remove(url);
                    Utils.headBitmap(bitmap);
                    return Utils.headBitmap(bitmap);
                } else {
                    mSoftCache.remove(url);
                }
            }
        }

        bitmap = BitmapFactory.decodeFile(url);
        if (bitmap != null) {
            addBitmapToCache(url, bitmap);
        }
        return Utils.headBitmap(bitmap);
    }

    /**
     * 添加图片到缓存
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (mLruCache) {
                mLruCache.put(url, bitmap);
            }
        }
    }

    public void clearCache() {
        mSoftCache.clear();
    }
}

package com.hgad.warehousemanager.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hasee on 2017/2/26.
 */
public class SingleThreadPool {

    private SingleThreadPool(){}

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static ExecutorService executorCacheService = Executors.newCachedThreadPool();

    public static ExecutorService getThread(){

        return executorService;
    }
    public static ExecutorService getPoolThread(){

        return executorCacheService;
    }
}

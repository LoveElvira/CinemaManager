package com.yyjlr.tickets;

import android.app.Activity;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.yyjlr.tickets.service.IDataService;
import com.yyjlr.tickets.service.MockupDataServiceImp;
import com.yyjlr.tickets.service.RealDataServiceImp;

/**
 * Created by Elvira on 2016/7/28.
 */
public class Application extends android.app.Application {
    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static Application sInstance;
    private Activity currentActivity;
    private static IDataService iDataService;

    static {
        if (Config.DEBUG) {
            iDataService = new MockupDataServiceImp();
        } else {
            iDataService = new RealDataServiceImp();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Fresco.initialize(this, createFrescoConfig());
    }

    private ImagePipelineConfig createFrescoConfig() {
        DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(getExternalCacheDir())
                .setBaseDirectoryName("fresco cache")
                .setMaxCacheSize(100 * 1024 * 1024)
                .setMaxCacheSizeOnLowDiskSpace(10 * 1024 * 1024)
                .setMaxCacheSizeOnVeryLowDiskSpace(5 * 1024 * 1024)
                .setVersion(1)
                .build();
        return ImagePipelineConfig.newBuilder(this)
                .setMainDiskCacheConfig(mainDiskCacheConfig)
                .build();
    }


    /**
     * @return Application singleton instance
     */
    public static synchronized Application getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public static IDataService getiDataService() {
        return iDataService;
    }

}

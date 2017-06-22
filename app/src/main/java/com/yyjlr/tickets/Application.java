package com.yyjlr.tickets;

import android.app.Activity;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.service.IDataService;
import com.yyjlr.tickets.service.MockupDataServiceImp;
import com.yyjlr.tickets.service.RealDataServiceImp;

import java.util.ArrayList;
import java.util.Stack;

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
    private ArrayList<AbstractActivity> activitys;          // 全局Activity集合

    {
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
        activitys = new ArrayList<AbstractActivity>();
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
     * 添加Activity到全局activity管理集合
     */
    public void addActivity(AbstractActivity activity) {
        String className = activity.getClass().getName();
        for (Activity at : activitys) {
            if (className.equals(at.getClass().getName())) {
                activitys.remove(at);
                break;
            }
        }
        activitys.add(activity);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activitys.size(); i < size; i++) {
            if (null != activitys.get(i)) {
                activitys.get(i).finish();
            }
        }
        activitys.clear();
    }

    /**
     * 结束指定Activity
     */
    public void finishActivity(Activity activity) {
        String className = activity.getClass().getName();
        for (Activity at : activitys) {
            if (className.equals(at.getClass().getName())) {
                activitys.remove(at);
                at.finish();
                break;
            }
        }
    }

    /**
     * @return Application singleton instance
     */
    public static synchronized Application getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        return this.currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public static IDataService getiDataService() {
        return iDataService;
    }

}

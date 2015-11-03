package com.icer.burgerkingcn.app;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * Created by icer on 2015/9/24.
 */
public class AppApplication extends Application {

    private static AppApplication appApplication;
    // NOTE: the content of this path will be deleted
    //       when the application is uninstalled (Android 2.2 and higher)
    protected File extStorageAppBasePath;
    protected File extStorageAppCachePath;
    protected File extStorageAppFilePath;

    @Override
    public void onCreate() {
        super.onCreate();
        initSingleton();
        initExtDir();
        initExtCacheDir();
        initExtFileDir();
    }

    @Override
    public File getCacheDir() {
        // NOTE: this method is used in Android 2.2 and higher

        if (extStorageAppCachePath != null) {
            // Use the external storage for the cache
            return extStorageAppCachePath;
        } else {
            // /data/data/your.package.name/cache
            return super.getCacheDir();
        }
    }

    @Override
    public File getFilesDir() {

        if (extStorageAppFilePath != null)
            return extStorageAppFilePath;
        else
            return super.getFilesDir();
    }

    private void initExtDir() {
        // Check if the external storage is writeable
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // Retrieve the base path for the application in the external storage
            File externalStorageDir = Environment.getExternalStorageDirectory();

            if (externalStorageDir != null) {
                // {SD_PATH}/Android/data/your.package.name
                extStorageAppBasePath = new File(externalStorageDir.getAbsolutePath() +
                        File.separator + "Android" + File.separator + "data" +
                        File.separator + getPackageName());
            }
        }
    }

    private void initExtFileDir() {
        // Check if the external storage is writeable
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            if (extStorageAppBasePath != null) {
                // {SD_PATH}/Android/data/your.package.name/file
                extStorageAppFilePath = new File(extStorageAppBasePath.getAbsolutePath() +
                        File.separator + "file");

                boolean isFilePathAvailable = true;

                if (!extStorageAppFilePath.exists()) {
                    // Create the file path on the external storage
                    isFilePathAvailable = extStorageAppFilePath.mkdirs();
                }

                if (!isFilePathAvailable) {
                    // Unable to create the file path
                    extStorageAppFilePath = null;
                }
            }
        }
    }

    private void initExtCacheDir() {
        // Check if the external storage is writeable
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            if (extStorageAppBasePath != null) {
                // {SD_PATH}/Android/data/your.package.name/cache
                extStorageAppCachePath = new File(extStorageAppBasePath.getAbsolutePath() +
                        File.separator + "cache");

                boolean isCachePathAvailable = true;

                if (!extStorageAppCachePath.exists()) {
                    // Create the cache path on the external storage
                    isCachePathAvailable = extStorageAppCachePath.mkdirs();
                }

                if (!isCachePathAvailable) {
                    // Unable to create the cache path
                    extStorageAppCachePath = null;
                }
            }
        }
    }

    private void initSingleton() {
        appApplication = this;
    }

    public static AppApplication getInstance() {
        return appApplication;
    }
}

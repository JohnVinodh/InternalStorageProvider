package com.kony.internalstorageprovider;

import android.app.Application;
import android.os.StrictMode;

import com.konylabs.android.KonyApplication;

/**
 * Created by KH2195 on 20-Sep-18.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
}



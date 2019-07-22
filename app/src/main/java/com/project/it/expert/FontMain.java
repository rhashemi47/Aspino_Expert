package com.project.it.expert;

import android.support.multidex.MultiDexApplication;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Rm on 03/04/2018.
 */

public class FontMain extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/vazir.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}

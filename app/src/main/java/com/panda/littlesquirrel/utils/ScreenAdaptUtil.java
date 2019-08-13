package com.panda.littlesquirrel.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * Created by jinjing on 2019/3/22.
 */

public class ScreenAdaptUtil {
    private static float sNoncompatDesity;
    private static float sNoncompatScaledDesity;

    public static void setCustomDesity(Activity activity, final Application paramApplication, int designPicture_dp)
    {
         DisplayMetrics appdisplayMetrics = paramApplication.getResources().getDisplayMetrics();
        if (sNoncompatDesity == 0.0F)
        {
            sNoncompatDesity = appdisplayMetrics.density;
            sNoncompatScaledDesity = appdisplayMetrics.scaledDensity;
            paramApplication.registerComponentCallbacks(new ComponentCallbacks()
            {
                @Override
                public void onConfigurationChanged(Configuration paramAnonymousConfiguration)
                {
                    if ((paramAnonymousConfiguration != null) && (paramAnonymousConfiguration.fontScale > 0.0F)){
                        //ScreenAdaptUtil.access$002(this.val$application.getResources().getDisplayMetrics().scaledDensity);
                        sNoncompatScaledDesity=paramApplication.getResources().getDisplayMetrics().scaledDensity;
                    }

                }

                @Override
                public void onLowMemory()
                {
                }
            });
        }
        final float targetDesity = (float) appdisplayMetrics.widthPixels / designPicture_dp;//designPicture_dp为设计图的宽度dp
        final float targetScaleDesity = targetDesity * (sNoncompatScaledDesity / sNoncompatDesity);
        final int targetDesityDpi = (int) (160 * targetDesity);

        appdisplayMetrics.density = targetDesity;
        appdisplayMetrics.scaledDensity = targetScaleDesity;
        appdisplayMetrics.densityDpi = targetDesityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDesity;
        activityDisplayMetrics.scaledDensity = targetScaleDesity;
        activityDisplayMetrics.densityDpi = targetDesityDpi;
    }
}

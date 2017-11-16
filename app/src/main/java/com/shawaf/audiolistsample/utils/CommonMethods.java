package com.shawaf.audiolistsample.utils;

import android.os.Build;

/**
 * Created by mohamedelshawaf on 9/20/17.
 */

public class CommonMethods {

    //Check the version of sdk is installed
    public static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

}

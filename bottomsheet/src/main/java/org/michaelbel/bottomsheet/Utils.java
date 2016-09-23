package org.michaelbel.bottomsheet;

import android.content.Context;
import android.support.annotation.NonNull;

import org.michaelbel.bottomsheet.R;

class Utils {

    private static Boolean isTablet = null;

    static int dp(@NonNull Context context, float value) {
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }

    public static boolean isTablet(@NonNull Context context) {
        if (isTablet == null) {
            isTablet = context.getResources().getBoolean(R.bool.tablet);
        }

        return isTablet;
    }
}
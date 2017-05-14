package tw.bingluen.heyyzu.tool;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;

public class ContextUtils {


    @IntDef({
            Context.MODE_PRIVATE, Context.MODE_APPEND,
            Context.MODE_ENABLE_WRITE_AHEAD_LOGGING, Context.MODE_NO_LOCALIZED_COLLATORS
    })
    public @interface SPMode{};


    public static SharedPreferences getSP(Fragment fragment, String key, @SPMode int mode) {
        int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            return getContext23(fragment).getSharedPreferences(key, mode);
        } else {
            return getContextLegacy(fragment).getSharedPreferences(key, mode);
        }
    }

    public static Context getContext(Fragment fragment) {
        int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            return getContext23(fragment);
        } else {
            return getContextLegacy(fragment);
        }
    }

    public static Drawable getDrawable(Fragment fragment, @DrawableRes int id) {
        int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            return ContextCompat.getDrawable(getContext23(fragment), id);
        } else {
            return ContextCompat.getDrawable(getContextLegacy(fragment), id);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static Context getContext23(Fragment fragment) {
        return fragment.getContext();
    }

    private static Context getContextLegacy(Fragment fragment) {
        return fragment.getActivity();
    }
}

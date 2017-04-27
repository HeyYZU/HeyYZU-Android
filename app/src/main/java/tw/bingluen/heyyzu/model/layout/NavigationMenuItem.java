package tw.bingluen.heyyzu.model.layout;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

public class NavigationMenuItem {
    private boolean enableLeftArrow;
    private boolean enableRightArrow;
    private String title;
    private int titleResId;
    private String key;


    public static NavigationMenuItem getInstance(
            @StringRes int titleResId,
            boolean enableLeftArrow, boolean enableRightArrow,
            @Nullable String key) {
        NavigationMenuItem item = new NavigationMenuItem();
        item.title = null;
        item.titleResId = titleResId;
        item.enableLeftArrow = enableLeftArrow;
        item.enableRightArrow = enableRightArrow;
        item.key = key;

        return item;
    }

    public static NavigationMenuItem getInstance(
            String title,
            boolean enableLeftArrow, boolean enableRightArrow,
            @Nullable String key) {
        NavigationMenuItem item = new NavigationMenuItem();
        item.title = title;
        item.titleResId = 0;
        item.enableLeftArrow = enableLeftArrow;
        item.enableRightArrow = enableRightArrow;
        item.key = key;

        return item;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public boolean isEnableLeftArrow() {
        return enableLeftArrow;
    }

    public boolean isEnableRightArrow() {
        return enableRightArrow;
    }


}

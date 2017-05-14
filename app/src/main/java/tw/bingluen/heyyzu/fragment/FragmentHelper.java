package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.content.Intent;

public interface FragmentHelper {
    void replaceContentFragment(Fragment fragment);
    void forceLogout();
    void openActivity(Intent i);
}

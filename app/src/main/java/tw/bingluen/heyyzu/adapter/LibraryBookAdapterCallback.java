package tw.bingluen.heyyzu.adapter;

import android.view.View;

import tw.bingluen.heyyzu.model.library.LibraryUsersBook;

/**
 * Created by zhuangbinglun on 2017/5/11.
 */

public interface LibraryBookAdapterCallback {
    void onItemClick(View v, int pos, LibraryUsersBook book);
}

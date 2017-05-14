package tw.bingluen.heyyzu.adapter;

import android.view.View;

import tw.bingluen.heyyzu.model.library.LibraryUsersBook;


public interface LibraryBookAdapterCallback {
    void onItemClick(View v, int pos, LibraryUsersBook book);
}

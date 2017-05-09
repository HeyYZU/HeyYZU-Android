package tw.bingluen.heyyzu.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.library.LibraryUsersBook;

public class LibraryBookAdapter extends RecyclerView.Adapter<LibraryBookAdapter.ViewHolder> {

    public static final int READING_BOOK = 0;
    public static final int RESERVING_BOOK = 1;
    public static final int FAVORITE_BOOK = 2;

    @IntDef({READING_BOOK, RESERVING_BOOK, FAVORITE_BOOK})
    public @interface BookListType{}

    private List<LibraryUsersBook> bookList;
    private LibraryBookAdapterCallback mCallback;
    private int layoutID;

    public LibraryBookAdapter(List<LibraryUsersBook> bookList, @BookListType int bookListType, LibraryBookAdapterCallback mCallback) {
        this.bookList = bookList;
        this.mCallback = mCallback;
        this.layoutID =
                bookListType == READING_BOOK ? R.layout.item_reading_book
                        : bookListType == RESERVING_BOOK ? R.layout.item_reserving_book
                        : R.layout.item_favorite_book;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(layoutID, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    interface LibraryBookAdapterCallback {
        void onItemClick(View v, int pos, LibraryUsersBook book);
    }
}

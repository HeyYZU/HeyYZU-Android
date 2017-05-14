package tw.bingluen.heyyzu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.library.LibraryUsersBook;

public class LibraryFavoriteBookAdapter extends RecyclerView.Adapter<LibraryFavoriteBookAdapter.ViewHolder> {

    private List<LibraryUsersBook> bookList;
    private LibraryBookAdapterCallback mCallback;
    private Context context;

    public LibraryFavoriteBookAdapter(List<LibraryUsersBook> bookList, LibraryBookAdapterCallback mCallback) {
        this.bookList = bookList;
        this.mCallback = mCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_favorite_book, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LibraryUsersBook book = bookList.get(position);
        holder.bookName.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bookName;
        private TextView bookAuthor;
        ViewHolder(View itemView) {
            super(itemView);
            bookName = (TextView) itemView.findViewById(R.id.txt_book_title);
            bookAuthor = (TextView) itemView.findViewById(R.id.txt_book_author);
        }
    }
}

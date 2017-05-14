package tw.bingluen.heyyzu.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.library.LibraryUsersBook;

public class LibraryReadingBookAdapter extends RecyclerView.Adapter<LibraryReadingBookAdapter.ViewHolder> {

    private List<LibraryUsersBook> bookList;
    private LibraryBookAdapterCallback mCallback;
    private Context context;

    public LibraryReadingBookAdapter(List<LibraryUsersBook> bookList, LibraryBookAdapterCallback mCallback) {
        this.bookList = bookList;
        this.mCallback = mCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_reading_book, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        final LibraryUsersBook book = bookList.get(position);
        holder.bookName.setText(book.getTitle());
        final long dueCountDown = book.getAttr().getDueDate() - (System.currentTimeMillis() / 1000);
        final int dayCountdown = (int) (dueCountDown / 60 / 60 / 24);

        if (dayCountdown < -3) {
            holder.bookDue.setText(String.format(
                    Locale.getDefault(),
                    context.getString(R.string.txt_library_book_due_over_with_fine),
                    dueCountDown / 60 / 60 / 24, book.getAttr().getFine()
            ));
            holder.bookDue.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else if (dayCountdown > 3) {
            holder.bookDue.setText(String.format(
                    Locale.getDefault(),
                    context.getString(R.string.txt_library_book_due_left),
                    dueCountDown / 60 / 60 / 24
            ));
        } else if (dayCountdown >= 0) {
            holder.bookDue.setText(String.format(
                    Locale.getDefault(),
                    context.getString(R.string.txt_library_book_due_left),
                    dueCountDown / 60 / 60 / 24
            ));
            holder.bookDue.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            holder.bookDue.setText(String.format(
                    Locale.getDefault(),
                    context.getString(R.string.txt_library_book_due_over),
                    dueCountDown / 60 / 60 / 24
            ));
            holder.bookDue.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(v, pos, book);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bookName;
        private TextView bookDue;
        ViewHolder(View itemView) {
            super(itemView);
            bookName = (TextView) itemView.findViewById(R.id.txt_book_title);
            bookDue = (TextView) itemView.findViewById(R.id.txt_book_dueDate);
        }
    }
}

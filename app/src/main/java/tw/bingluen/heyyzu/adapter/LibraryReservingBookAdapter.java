package tw.bingluen.heyyzu.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.library.LibraryUsersBook;

public class LibraryReservingBookAdapter extends RecyclerView.Adapter<LibraryReservingBookAdapter.ViewHolder> {

    private List<LibraryUsersBook> bookList;
    private LibraryBookAdapterCallback mCallback;
    private Context context;

    public LibraryReservingBookAdapter(List<LibraryUsersBook> bookList, LibraryBookAdapterCallback mCallback) {
        this.bookList = bookList;
        this.mCallback = mCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_reserving_book, parent, false);
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
        if (book.getAttr().getReservedBefore() != 0) {
            holder.bookOrder.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.bookOrder.setText(String.format(
                    Locale.getDefault(),
                    context.getString(R.string.txt_library_reserving_available_to_pick_up),
                    DateFormat.getDateFormat(context).format(book.getAttr().getReservedBefore() * 1000)
            ));
        } else {
            holder.bookOrder.setText(String.format(
                    Locale.getDefault(),
                    context.getString(R.string.txt_library_reserving_order),
                    book.getAttr().getOrder()
            ));
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
        private TextView bookOrder;
        ViewHolder(View itemView) {
            super(itemView);
            bookName = (TextView) itemView.findViewById(R.id.txt_book_title);
            bookOrder = (TextView) itemView.findViewById(R.id.txt_book_reserving_order);
        }
    }

}

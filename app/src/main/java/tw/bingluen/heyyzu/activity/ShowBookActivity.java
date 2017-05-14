package tw.bingluen.heyyzu.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.constant.SPKey;
import tw.bingluen.heyyzu.fragment.SimpleDialogFragment;
import tw.bingluen.heyyzu.model.library.LibraryBook;
import tw.bingluen.heyyzu.model.library.LibraryCollection;
import tw.bingluen.heyyzu.network.HeyYZUAPIClient;

public class ShowBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        loadingBook();
    }

    private void loadingBook() {
        String token = getSharedPreferences(SPKey.NAME, MODE_PRIVATE).getString(SPKey.USER_ACCESS_TOKEN, "");
        String bookId = getIntent().getStringExtra("bookId");
        Call<LibraryBook> callBook = HeyYZUAPIClient.get().libraryBook(bookId, token);
        callBook.enqueue(new Callback<LibraryBook>() {
            @Override
            public void onResponse(Call<LibraryBook> call, Response<LibraryBook> response) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    inflateBook(response.body());
                } else {
                    handleError(response.code());
                }
            }

            @Override
            public void onFailure(Call<LibraryBook> call, Throwable t) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                handleError(0);
            }
        });
    }

    private void inflateBook(LibraryBook book) {

        final TextView bookTitle, bookISBN, bookIndex, bookAuthor, bookPublisher, bookYear;
        final RecyclerView bookCollection;
        final ImageView bookCover;

        bookTitle = (TextView) findViewById(R.id.tv_book_title);
        bookISBN = (TextView) findViewById(R.id.tv_book_isbn);
        bookIndex = (TextView) findViewById(R.id.tv_book_index);
        bookAuthor = (TextView) findViewById(R.id.tv_book_author);
        bookPublisher  = (TextView) findViewById(R.id.tv_book_publisher);
        bookYear = (TextView) findViewById(R.id.tv_book_year);
        bookCollection = (RecyclerView) findViewById(R.id.recycler_view);
        bookCover = (ImageView) findViewById(R.id.img_book_cover);

        bookTitle.setText(book.getTitle());
        bookISBN.setText(String.format(Locale.getDefault(), "%1$d", book.getIsbn()));
        bookIndex.setText(book.getIndex());
        bookAuthor.setText(book.getAuthor());
        bookPublisher.setText(book.getPublisher());
        bookYear.setText(String.format(Locale.getDefault(), "%1$d", book.getYear()));

        if (book.getCover().length() > 0) {
            Picasso.with(this)
                    .load(book.getCover())
                    .fit()
                    .centerInside()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .error(R.drawable.ic_empty_box)
                    .into(bookCover);
        }

        bookCollection.setAdapter(new BookCollectionAdapter(book.getCollections()));
        bookCollection.setLayoutManager(new LinearLayoutManager(this));
        bookCollection.setItemAnimator(new DefaultItemAnimator());
        bookCollection.setNestedScrollingEnabled(false);

        findViewById(R.id.view_book).setVisibility(View.VISIBLE);
    }

    private void handleError(int responseCode) {
        final AppCompatActivity that = this;
        switch (responseCode) {
            case 400:
                SimpleDialogFragment.getInstance(SimpleDialogFragment.ERROR_WITH_ONE_BUTTON)
                        .setTitle(R.string.dialog_title_oops)
                        .setMessage(R.string.dialog_message_token_expired)
                        .setRightButtonText(R.string.btn_ok)
                        .setRightButtonCallback(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences sp = getSharedPreferences(SPKey.NAME, MODE_PRIVATE);
                                sp.edit()
                                        .remove(SPKey.ACCESS_TOKEN_EXPIRED)
                                        .remove(SPKey.USER_ACCESS_TOKEN)
                                        .apply();
                                Intent intent = new Intent(that, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                break;
            case 502:
                findViewById(R.id.view_server_error).setVisibility(View.VISIBLE);
                break;
            case 404:
                findViewById(R.id.view_search_none).setVisibility(View.VISIBLE);
                break;
            case 0:
            default:
                findViewById(R.id.view_connect_error).setVisibility(View.VISIBLE);
                break;
        }
    }

    class BookCollectionAdapter extends RecyclerView.Adapter<BookCollectionAdapter.ViewHolder> {

        private List<LibraryCollection> dataList;
        private Context context;

        public BookCollectionAdapter(List<LibraryCollection> dataList) {
            this.dataList = dataList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_book_collection, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final LibraryCollection collection = dataList.get(position);
            try {
                holder.collection.setText(String.format(Locale.getDefault(),
                        "%1$s / %2$s%3$s",
                        collection.getAttribute().getType(),
                        collection.getBranch(),
                        collection.getCollection())
                );
                if (collection.getReturnTime() > 0) {
                    holder.status.setText(R.string.txt_library_book_status_loaned_out);
                    holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    holder.collection.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                } else {
                    holder.status.setText(R.string.txt_library_book_status_on_shelf);
                    holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    holder.collection.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                }

                if (collection.getReservingCount() > 0) {
                    holder.reservingCounting.setText(String.format(
                            Locale.getDefault(),
                            getString(R.string.txt_library_book_reserving_count),
                            collection.getReservingCount()
                    ));
                }
            } catch (NullPointerException e) {
                holder.collection.setText(R.string.txt_library_book_status_cataloging);
                holder.status.setVisibility(View.GONE);
                holder.reservingCounting.setVisibility(View.GONE);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView collection, status, reservingCounting;
            public ViewHolder(View itemView) {
                super(itemView);
                collection = (TextView) itemView.findViewById(R.id.tv_book_collection);
                status = (TextView) itemView.findViewById(R.id.tv_book_status);
                reservingCounting = (TextView) itemView.findViewById(R.id.tv_book_reserving_count);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

package tw.bingluen.heyyzu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.library.LibrarySearchResult;

public class LibrarySearchResultAdapter extends RecyclerView.Adapter<LibrarySearchResultAdapter.ViewHolder> {

    private List<LibrarySearchResult> resultList;
    private LibrarySearchResultAdapterCallback mCallback;
    private Context context;
    private RecyclerView.ViewHolder viewHolder;

    public LibrarySearchResultAdapter(List<LibrarySearchResult> resultList, LibrarySearchResultAdapterCallback mCallback) {
        this.resultList = resultList;
        this.mCallback = mCallback;
    }

    public void setResultList(List<LibrarySearchResult> resultList) {
        this.resultList = resultList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_library_search_result, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        if (resultList == null) return 0;
        return resultList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        final LibrarySearchResult item = resultList.get(pos);
        holder.index.setText(item.getIndex());
        holder.author.setText(item.getAuthor());
        holder.title.setText(item.getTitle());
        if (item.getCover().length() > 0) {
            Picasso.with(context)
                    .load(item.getCover())
                    .fit()
                    .centerInside()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.ic_empty_box)
                    .into(holder.cover);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(v, pos, item);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView cover;
        private TextView title, author, index;

        public ViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.img_book_cover);
            title = (TextView) itemView.findViewById(R.id.tv_book_title);
            author = (TextView) itemView.findViewById(R.id.tv_book_author);
            index = (TextView) itemView.findViewById(R.id.tv_book_index);
        }
    }

    public interface LibrarySearchResultAdapterCallback {
        void onItemClick(View v, int pos, LibrarySearchResult item);
    }
}

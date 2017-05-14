package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.adapter.LibrarySearchResultAdapter;
import tw.bingluen.heyyzu.model.library.LibrarySearchResult;
import tw.bingluen.heyyzu.tool.ContextUtils;

public class LibrarySearchResultFragment extends Fragment {
    private LibrarySearchResultAdapter adapter;

    public static LibrarySearchResultFragment getInstance(
            List<LibrarySearchResult> resultList,
            LibrarySearchResultAdapter.LibrarySearchResultAdapterCallback mCallback) {
        LibrarySearchResultFragment fragment = new LibrarySearchResultFragment();
        fragment.adapter = new LibrarySearchResultAdapter(resultList, mCallback);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.recycler_view, container, false);
        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return root;
    }

    public void updateList(List<LibrarySearchResult> newList) {
        adapter.setResultList(newList);
        adapter.notifyDataSetChanged();
    }
}

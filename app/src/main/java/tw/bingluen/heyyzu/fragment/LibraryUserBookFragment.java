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

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.tool.ContextUtils;

public class LibraryUserBookFragment extends Fragment {

    private RecyclerView.Adapter adapter;

    public static LibraryUserBookFragment getInstance(RecyclerView.Adapter adapter) {
        LibraryUserBookFragment fragment = new LibraryUserBookFragment();
        fragment.adapter = adapter;
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recycler_view, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return root;
    }
}

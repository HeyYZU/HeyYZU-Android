package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;

import tw.bingluen.heyyzu.R;

public class LibraryFragment extends Fragment {

    private FloatingSearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_fragment_library);

        final View viewSearchView = root.findViewById(R.id.view_searchView);
        searchView = (FloatingSearchView) root.findViewById(R.id.searchView);

        final AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appBarLayout);
        viewSearchView.setElevation(appBarLayout.getElevation());
        searchView.setElevation(appBarLayout.getElevation());
        appBarLayout.setElevation(0);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        final AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appBarLayout);
        appBarLayout.setElevation(searchView.getElevation());
    }
}

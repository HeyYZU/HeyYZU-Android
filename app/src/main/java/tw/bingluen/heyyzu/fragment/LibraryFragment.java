package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.constant.SPKey;
import tw.bingluen.heyyzu.model.library.LibraryDashboard;
import tw.bingluen.heyyzu.network.HeyYZUAPIClient;
import tw.bingluen.heyyzu.tool.ContextUtils;

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

    private void loadingDashboard() {
        String token = ContextUtils.getSP(this, SPKey.NAME, Context.MODE_PRIVATE).getString(SPKey.USER_ACCESS_TOKEN, "");

        Call<LibraryDashboard> callDashboard = HeyYZUAPIClient.get().libraryDashboard(token);

        callDashboard.enqueue(new Callback<LibraryDashboard>() {
            @Override
            public void onResponse(Call<LibraryDashboard> call, Response<LibraryDashboard> response) {
                if (response.isSuccessful()) {
                    inflateDashboard(response.body());
                } else {

                }
            }

            @Override
            public void onFailure(Call<LibraryDashboard> call, Throwable t) {

            }
        });
    }

    private void inflateDashboard(LibraryDashboard data) {

    }
}

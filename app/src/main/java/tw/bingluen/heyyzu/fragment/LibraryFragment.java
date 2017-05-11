package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.adapter.LibraryBookAdapterCallback;
import tw.bingluen.heyyzu.adapter.LibraryFavoriteBookAdapter;
import tw.bingluen.heyyzu.adapter.LibraryReadingBookAdapter;
import tw.bingluen.heyyzu.adapter.LibraryReservingBookAdapter;
import tw.bingluen.heyyzu.constant.SPKey;
import tw.bingluen.heyyzu.model.library.LibraryDashboard;
import tw.bingluen.heyyzu.model.library.LibraryUsersBook;
import tw.bingluen.heyyzu.network.HeyYZUAPIClient;
import tw.bingluen.heyyzu.tool.ContextUtils;

public class LibraryFragment extends Fragment {

    private FloatingSearchView searchView;
    private LibraryDashboard dashboard;
    private CharSequence previousTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        previousTitle =  toolbar.getTitle();
        toolbar.setTitle(R.string.title_fragment_library);

        final View viewSearchView = root.findViewById(R.id.view_searchView);
        searchView = (FloatingSearchView) root.findViewById(R.id.searchView);

        final AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appBarLayout);
        viewSearchView.setElevation(appBarLayout.getElevation());
        searchView.setElevation(appBarLayout.getElevation());
        appBarLayout.setElevation(0);

        dashboard = new LibraryDashboard();

        loadingDashboard(root);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        final AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appBarLayout);
        appBarLayout.setElevation(searchView.getElevation());

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(previousTitle);
    }

    private void loadingDashboard(final View rootView) {
        String token = ContextUtils.getSP(this, SPKey.NAME, Context.MODE_PRIVATE).getString(SPKey.USER_ACCESS_TOKEN, "");

        Call<LibraryDashboard> callDashboard = HeyYZUAPIClient.get().libraryDashboard(token);

        callDashboard.enqueue(new Callback<LibraryDashboard>() {
            @Override
            public void onResponse(Call<LibraryDashboard> call, Response<LibraryDashboard> response) {
                if (response.isSuccessful()) {
                    Log.d("LibraryFragment", "Successful.");
                    inflateDashboard(response.body(), rootView);
                } else {
                    Log.d("LibraryFragment", "dashboard response error.");
                }
            }

            @Override
            public void onFailure(Call<LibraryDashboard> call, Throwable t) {
                Log.d("LibraryFragment", "Fail");
            }
        });
    }

    private void inflateDashboard(LibraryDashboard data, View root) {

        final RecyclerView readingView = (RecyclerView) root.findViewById(R.id.recycler_view_reading_book);
        final RecyclerView reservingView = (RecyclerView) root.findViewById(R.id.recycler_view_reserving_book);
        final RecyclerView favoriteView = (RecyclerView) root.findViewById(R.id.recycler_view_favorite_book);

        if (data.getReading().getTotal() > 0) {
            readingView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
            readingView.setItemAnimator(new DefaultItemAnimator());
            readingView.setAdapter(new LibraryReadingBookAdapter(data.getReading().getLeastFive(), new LibraryBookAdapterCallback() {
                @Override
                public void onItemClick(View v, int pos, LibraryUsersBook book) {

                }
            }));
            readingView.setVisibility(View.VISIBLE);
            root.findViewById(R.id.tv_reading_book_empty).setVisibility(View.GONE);
        }

        if (data.getReserving().getTotal() > 0) {
            reservingView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
            reservingView.setItemAnimator(new DefaultItemAnimator());
            reservingView.setAdapter(new LibraryReservingBookAdapter(data.getReserving().getLeastFive(), new LibraryBookAdapterCallback() {
                @Override
                public void onItemClick(View v, int pos, LibraryUsersBook book) {

                }
            }));
            reservingView.setVisibility(View.VISIBLE);
            root.findViewById(R.id.tv_reserving_book_empty).setVisibility(View.GONE);
        }

        if(data.getFavorite().getTotal() > 0) {
            favoriteView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
            favoriteView.setItemAnimator(new DefaultItemAnimator());
            favoriteView.setAdapter(new LibraryFavoriteBookAdapter(data.getFavorite().getLeastFive(), new LibraryBookAdapterCallback() {
                @Override
                public void onItemClick(View v, int pos, LibraryUsersBook book) {

                }
            }));
            favoriteView.setVisibility(View.VISIBLE);
            root.findViewById(R.id.tv_favorite_book_empty).setVisibility(View.GONE);
        }
    }
}

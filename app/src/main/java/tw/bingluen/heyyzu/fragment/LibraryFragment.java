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


    private View root;

    private FloatingSearchView searchView;
    private LibraryDashboard dashboard;
    private CharSequence previousTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_library, container, false);

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

        loadingDashboard();

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

    private void loadingDashboard() {
        String token = ContextUtils.getSP(this, SPKey.NAME, Context.MODE_PRIVATE).getString(SPKey.USER_ACCESS_TOKEN, "");

        Call<LibraryDashboard> callDashboard = HeyYZUAPIClient.get().libraryDashboard(token);

        callDashboard.enqueue(new Callback<LibraryDashboard>() {
            @Override
            public void onResponse(Call<LibraryDashboard> call, Response<LibraryDashboard> response) {
                root.findViewById(R.id.progressBar).setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    inflateDashboard(response.body());
                } else {
                    handleError(response.code());
                }
            }

            @Override
            public void onFailure(Call<LibraryDashboard> call, Throwable t) {
                root.findViewById(R.id.progressBar).setVisibility(View.GONE);
                handleError(0);
            }
        });
    }

    private void inflateDashboard(LibraryDashboard data) {

        final RecyclerView readingView = (RecyclerView) root.findViewById(R.id.recycler_view_reading_book);
        final RecyclerView reservingView = (RecyclerView) root.findViewById(R.id.recycler_view_reserving_book);
        final RecyclerView favoriteView = (RecyclerView) root.findViewById(R.id.recycler_view_favorite_book);

        root.findViewById(R.id.view_library_dashboard).setVisibility(View.VISIBLE);

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

    private void handleError(int responseCode) {
        switch (responseCode) {
            case 400:
                SimpleDialogFragment.getInstance(SimpleDialogFragment.ERROR_WITH_ONE_BUTTON)
                        .setTitle(R.string.dialog_title_oops)
                        .setMessage(R.string.dialog_message_token_expired)
                        .setRightButtonText(R.string.btn_ok)
                        .setRightButtonCallback(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentHelper helper = (FragmentHelper) getActivity();
                                helper.forceLogout();
                            }
                        });
                break;
            case 502:
                root.findViewById(R.id.view_server_error).setVisibility(View.VISIBLE);
                break;
            case 0:
            default:
                root.findViewById(R.id.view_connect_error).setVisibility(View.VISIBLE);
                break;
        }
    }
}

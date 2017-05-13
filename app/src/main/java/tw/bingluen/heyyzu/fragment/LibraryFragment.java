package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.adapter.LibraryBookAdapterCallback;
import tw.bingluen.heyyzu.adapter.LibraryFavoriteBookAdapter;
import tw.bingluen.heyyzu.adapter.LibraryReadingBookAdapter;
import tw.bingluen.heyyzu.adapter.LibraryReservingBookAdapter;
import tw.bingluen.heyyzu.adapter.LibrarySearchResultAdapter;
import tw.bingluen.heyyzu.constant.SPKey;
import tw.bingluen.heyyzu.model.library.LibraryDashboard;
import tw.bingluen.heyyzu.model.library.LibrarySearchResult;
import tw.bingluen.heyyzu.model.library.LibraryUsersBook;
import tw.bingluen.heyyzu.network.HeyYZUAPIClient;
import tw.bingluen.heyyzu.network.YZUAPIClient;
import tw.bingluen.heyyzu.tool.ContextUtils;

public class LibraryFragment extends Fragment {


    private View root;

    private FloatingSearchView searchView;
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

        loadingDashboard();

        setupSearchView();

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

    private void loadingSearchSuggestion(String keyword) {
        searchView.showProgress();

        Call<List<LibrarySearchResult>> callSearch = YZUAPIClient.get().search(keyword);

        callSearch.enqueue(new Callback<List<LibrarySearchResult>>() {
            @Override
            public void onResponse(Call<List<LibrarySearchResult>> call, Response<List<LibrarySearchResult>> response) {
                searchView.hideProgress();
                if (response.isSuccessful()) {
                    if (searchResultList == null) {
                        searchResultList = response.body();
                    } else {
                        searchResultList.clear();
                        searchResultList.addAll(response.body());
                    }
                    searchView.swapSuggestions(
                            searchResultList.subList(0, Math.min(searchResultList.size(), 5))
                    );
                } else {
                    searchView.clearSuggestions();
                }
            }

            @Override
            public void onFailure(Call<List<LibrarySearchResult>> call, Throwable t) {
                searchView.hideProgress();
                searchView.clearSuggestions();
            }
        });
    }

    private void inflateDashboard(LibraryDashboard data) {

        final RecyclerView readingView = (RecyclerView) root.findViewById(R.id.recycler_view_reading_book);
        final RecyclerView reservingView = (RecyclerView) root.findViewById(R.id.recycler_view_reserving_book);
        final RecyclerView favoriteView = (RecyclerView) root.findViewById(R.id.recycler_view_favorite_book);

        root.findViewById(R.id.content_library).setVisibility(View.VISIBLE);

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

    private List<LibrarySearchResult> searchResultList;
    private LibrarySearchResultFragment searchResultFragment;

    private void setupSearchView() {
        searchView = (FloatingSearchView) root.findViewById(R.id.searchView);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                String[] keywords = newQuery.split(" ");

                if (searchResultList == null || searchResultList.size() == 0) {
                    if (newQuery.length() < 4) return;
                    loadingSearchSuggestion(newQuery);
                } else if (newQuery.length() == 0) {
                    searchResultList.clear();
                    searchView.swapSuggestions(searchResultList);
                    return;
                } else if (keywords.length > oldQuery.split(" ").length && oldQuery.length() < newQuery.length() && newQuery.contains(oldQuery)) {
                    List<LibrarySearchResult> filtered = new ArrayList<>();
                    for (LibrarySearchResult item : searchResultList) {
                        if (containsKeyword(keywords, item.getTitle())) {
                            filtered.add(item);
                        }
                    }
                    searchView.swapSuggestions(filtered.subList(0, Math.min(filtered.size(), 5)));
                } else if (keywords.length < oldQuery.split(" ").length && oldQuery.length() > newQuery.length() && oldQuery.contains(newQuery)) {
                    List<LibrarySearchResult> filtered = new ArrayList<>();
                    for (LibrarySearchResult item : searchResultList) {
                        if (containsKeyword(keywords, item.getTitle())) {
                            filtered.add(item);
                        }
                    }
                    searchView.swapSuggestions(filtered.subList(0, Math.min(filtered.size(), 5)));
                } else {
                    if (newQuery.length() < 4) {
                        searchView.clearSuggestions();
                        return;
                    }
                    loadingSearchSuggestion(newQuery);
                }
            }
        });

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                if (searchResultFragment == null) {
                    searchResultFragment = LibrarySearchResultFragment.getInstance(searchResultList, new LibrarySearchResultAdapter.LibrarySearchResultAdapterCallback() {
                        @Override
                        public void onItemClick(View v, int pos, LibrarySearchResult item) {

                        }
                    });

                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    ft.addToBackStack("LibrarySearch")
                            .replace(R.id.content_library, searchResultFragment)
                            .commit();
                } else {
                    searchResultFragment.updateList(searchResultList);
                }
            }
        });
    }

    private boolean containsKeyword(String[] keywords, String bookTitle) {
        for (String keyword:keywords) {
            if (bookTitle.toLowerCase().contains(keyword.toLowerCase())) return true;
        }
        return false;
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

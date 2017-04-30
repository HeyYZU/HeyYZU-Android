package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.adapter.NavigationMenuItemAdapter;
import tw.bingluen.heyyzu.constant.SPKey;
import tw.bingluen.heyyzu.model.layout.NavigationMenuItem;
import tw.bingluen.heyyzu.model.user.Curriculum;
import tw.bingluen.heyyzu.network.HeyYZUAPIClient;
import tw.bingluen.heyyzu.tool.ContextUtils;

public class NavigationMenuFragment extends Fragment {

    public static final int PAGE_TYPE_MAIN = 0;
    public static final int PAGE_TYPE_COURSE = 1;

    @IntDef({PAGE_TYPE_MAIN, PAGE_TYPE_COURSE})
    public @interface PageType{}

    private int pageType;
    private NavigationMenuItemAdapter adapter;

    private List<NavigationMenuItem> menuItemList;

    private NavigationMenuItemAdapter.NavigationMenuCallback menuCallback;

    private NavigationCallback navigationCallback;

    public static NavigationMenuFragment getInstance(@PageType int pageType) {
        NavigationMenuFragment fragment = new NavigationMenuFragment();
        fragment.pageType = pageType;

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationCallback = (NavigationCallback) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        switch (pageType) {
            case PAGE_TYPE_MAIN:
                prepareMainMenu();
                break;
            case PAGE_TYPE_COURSE:
                prepareCourseMenu();
                break;
        }

        View root = inflater.inflate(R.layout.fragment_navigation_menu_page, container, false);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.nav_menu);
        adapter = new NavigationMenuItemAdapter(menuItemList, menuCallback);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return root;
    }

    private void prepareMainMenu() {
        menuItemList = new ArrayList<>();

        menuItemList.add(NavigationMenuItem.getInstance(R.string.navigation_menu_home, false, false, null));
        menuItemList.add(NavigationMenuItem.getInstance(R.string.navigation_menu_course, false, true, null));
        menuItemList.add(NavigationMenuItem.getInstance(R.string.navigation_menu_library, false, false, null));
        menuItemList.add(NavigationMenuItem.getInstance(R.string.navigation_menu_calendar, false, false, null));

        menuCallback = new NavigationMenuItemAdapter.NavigationMenuCallback() {
            @Override
            public void onItemClick(View v, int position, String key) {
                switch (position) {
                    case 0:
                        navigationCallback.switchContentFragment(NavigationCallback.HOME_FRAGMENT, null);
                        break;
                    case 1:
                        navigationCallback.switchNavigationPage(NavigationCallback.COURSE_NAVIGATION);
                        break;
                    case 2:
                        navigationCallback.switchContentFragment(NavigationCallback.LIBRARY_FRAGMENT, null);
                        break;
                    case 3:
                        navigationCallback.switchContentFragment(NavigationCallback.CALENDAR_FRAGMENT, null);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void prepareCourseMenu() {

        String token = ContextUtils.getSP(this, SPKey.NAME, Context.MODE_PRIVATE).getString(SPKey.USER_ACCESS_TOKEN, "");

        menuItemList = new ArrayList<>();

        menuItemList.add(NavigationMenuItem.getInstance(R.string.navigation_menu_back, true, false, null));

        if (token.length() > 0) {
            Call<List<Curriculum>> curriculumCall = HeyYZUAPIClient.get().userCurriculum(token);

            curriculumCall.enqueue(new Callback<List<Curriculum>>() {
                @Override
                public void onResponse(Call<List<Curriculum>> call, Response<List<Curriculum>> response) {
                    if (response.isSuccessful()) {
                        for (Curriculum curriculum : response.body()) {
                            menuItemList.add(NavigationMenuItem.getInstance(curriculum.getName(), false, false, curriculum.getLessonId()));
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        // Token 過期
                    }
                }

                @Override
                public void onFailure(Call<List<Curriculum>> call, Throwable t) {
                    // 網路炸了
                }
            });
        }

        menuCallback = new NavigationMenuItemAdapter.NavigationMenuCallback() {
            @Override
            public void onItemClick(View v, int position, String key) {
                Log.d("NavMenuItem", "Click position = " + position + " key = " + key);
                if (position > 0) {
                    navigationCallback.switchContentFragment(NavigationCallback.COURSE_FRAGMENT, key);
                } else {
                    navigationCallback.switchNavigationPage(NavigationCallback.MAIN_NAVIGATION);
                }
            }
        };
    }

    public interface NavigationCallback{
        int HOME_FRAGMENT = -1;
        int COURSE_FRAGMENT = 0;
        int LIBRARY_FRAGMENT = 1;
        int CALENDAR_FRAGMENT = 2;
        @IntDef({HOME_FRAGMENT, COURSE_FRAGMENT, LIBRARY_FRAGMENT, CALENDAR_FRAGMENT})
        @interface TargetFragment{}

        int MAIN_NAVIGATION = 0;
        int COURSE_NAVIGATION = 1;
        @IntDef({MAIN_NAVIGATION, COURSE_NAVIGATION})
        @interface TargetNavigation{}


        void switchContentFragment(@TargetFragment int targetFragment, @Nullable String key);
        void switchNavigationPage(@TargetNavigation int targetNav);
    }
}

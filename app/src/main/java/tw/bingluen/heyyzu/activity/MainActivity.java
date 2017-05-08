package tw.bingluen.heyyzu.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.constant.SPKey;
import tw.bingluen.heyyzu.fragment.ClassroomFragment;
import tw.bingluen.heyyzu.fragment.FragmentHelper;
import tw.bingluen.heyyzu.fragment.NavigationMenuFragment;
import tw.bingluen.heyyzu.fragment.SimpleDialogFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationMenuFragment.NavigationCallback,
        View.OnClickListener, FragmentHelper {

    private ViewPager navViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navViewPager = (ViewPager) findViewById(R.id.nav_viewPager);
        navViewPager.setAdapter(new NavPageAdapter(getFragmentManager()));
        navViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        ImageView logout = (ImageView) findViewById(R.id.img_logout);
        ImageView settings = (ImageView) findViewById(R.id.img_settings);
        logout.setOnClickListener(this);
        settings.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    /**
     * Interface - NavigationMenuFragment.NavigationCallback.switchContentFragment
     * To handle fragment replace switch request from navigation menu
     * @param targetFragment new fragment
     * @param key addition key to fragment
     */

    @Override
    public void switchContentFragment(@TargetFragment int targetFragment, @Nullable Object key) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        switch(targetFragment) {
            case HOME_FRAGMENT:
                while (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                    onBackPressed();
                }
                break;
            case COURSE_FRAGMENT:
                ft.addToBackStack("NavReplace")
                        .replace(R.id.content_main, ClassroomFragment.getInstance(key))
                        .commit()
                        ;
                break;
            case LIBRARY_FRAGMENT:
                break;
            case CALENDAR_FRAGMENT:
                break;
            default:
                return;
        }
    }

    /**
     * Interface - NavigationMenuFragment.NavigationCallback.switchNavigationPage
     * Switch navigation menu page
     * @param targetNav target menu page index
     */

    @Override
    public void switchNavigationPage(@TargetFragment int targetNav) {
        navViewPager.setCurrentItem(targetNav);
    }

    @Override
    public void onClick(View v) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch(v.getId()) {
            case R.id.img_logout:
                final SimpleDialogFragment dialog = SimpleDialogFragment.getInstance(SimpleDialogFragment.NORMAL_WITH_TWO_BUTTON);
                dialog.setTitle(R.string.dialog_title_logout)
                        .setLeftButtonText(R.string.btn_ok)
                        .setRightButtonText(R.string.btn_cancel)
                        .setMessage(R.string.dialog_message_logout)
                        .setLeftButtonCallback(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                doLogout();
                            }
                        })
                        .setRightButtonCallback(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        })
                        .show(getFragmentManager(), "Dialog");
                break;
            case R.id.img_settings:

                break;
            default:
                break;
        }
    }

    private void doLogout() {
        SharedPreferences sp = getSharedPreferences(SPKey.NAME, MODE_PRIVATE);
        sp.edit()
                .remove(SPKey.ACCESS_TOKEN_EXPIRED)
                .remove(SPKey.USER_ACCESS_TOKEN)
                .apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Interface - FragmentHelper.replaceContentFragment
     * To handle replace fragment request from child fragment.
     * @param fragment new fragment
     */

    @Override
    public void replaceContentFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack("FragmentReplace")
                .replace(R.id.content_main, fragment)
                .commit()
                ;
    }

    /**
     * Interface - FragmentHelper.
     * forceLogout user.
     */

    @Override
    public void forceLogout() {
        doLogout();
    }


    class NavPageAdapter extends FragmentPagerAdapter {

        private List<NavigationMenuFragment> fragmentList;

        public NavPageAdapter(FragmentManager fm) {
            super(fm);

            fragmentList = new ArrayList<>();

            fragmentList.add(NavigationMenuFragment.getInstance(NavigationMenuFragment.PAGE_TYPE_MAIN));
            fragmentList.add(NavigationMenuFragment.getInstance(NavigationMenuFragment.PAGE_TYPE_COURSE));
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}

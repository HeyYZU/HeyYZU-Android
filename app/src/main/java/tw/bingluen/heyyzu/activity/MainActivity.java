package tw.bingluen.heyyzu.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.fragment.NavigationMenuFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationMenuFragment.NavigationCallback {

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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navViewPager = (ViewPager) findViewById(R.id.nav_viewPager);
        navViewPager.setAdapter(new NavPageAdapter(getFragmentManager()));
        navViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void switchContentFragment(@TargetFragment int targetFragment, @Nullable String key) {
        switch(targetFragment) {
            case COURSE_FRAGMENT:
                break;
            case LIBRARY_FRAGMENT:
                break;
            case CALENDAR_FRAGMENT:
                break;
            default:
                return;
        }
    }

    @Override
    public void switchNavigationPage(@TargetFragment int targetNav) {
        navViewPager.setCurrentItem(targetNav);
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

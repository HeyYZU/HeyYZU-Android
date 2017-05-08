package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tw.bingluen.heyyzu.R;

public class ClassroomFragment extends Fragment {

    private String lessonID;
    private String lessonName;
    private CharSequence previousToolbarTitle;
    private TabLayout tabLayout;

    public static ClassroomFragment getInstance(Object keys) {
        ClassroomFragment fragment = new ClassroomFragment();
        fragment.lessonID = ((NavigationMenuFragment.CourseKeys) keys).courseId;
        fragment.lessonName = ((NavigationMenuFragment.CourseKeys) keys).courseName;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_course, container, false);
        final ViewPager viewPager = (ViewPager) root.findViewById(R.id.course_viewPager);
        tabLayout = (TabLayout) root.findViewById(R.id.course_tabLayout);

        viewPager.setAdapter(new CourseFragmentAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager, true);

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        final AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appBarLayout);

        previousToolbarTitle = toolbar.getTitle();
        toolbar.setTitle(this.lessonName);

        tabLayout.setElevation(appBarLayout.getElevation());
        appBarLayout.setElevation(0);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(previousToolbarTitle);

        final AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appBarLayout);
        appBarLayout.setElevation(tabLayout.getElevation());
    }

    class CourseFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public CourseFragmentAdapter(FragmentManager fm) {
            super(fm);

            fragmentList = new ArrayList<>();

            try {
                fragmentList.add(CourseFragment.getInstance(CourseFragment.ANNOUNCEMENT_FRAGMENT, lessonID));
                fragmentList.add(CourseFragment.getInstance(CourseFragment.HOMEWORK_FRAGMENT, lessonID));
                fragmentList.add(CourseFragment.getInstance(CourseFragment.MATERIAL_FRAGMENT, lessonID));
            } catch (Exception e) {
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return getString(R.string.fragment_page_title_announcement);
                case 1:
                    return getString(R.string.fragment_page_title_homework);
                case 2:
                    return getString(R.string.fragment_page_title_material);
                default:
                    return "";
            }
        }
    }
}

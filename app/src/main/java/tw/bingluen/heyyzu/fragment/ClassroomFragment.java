package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tw.bingluen.heyyzu.R;

public class ClassroomFragment extends Fragment {

    private String lessonID;
    private String lessonName;
    private CharSequence previousToolbarTitle;

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
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.course_tabLayout);

        viewPager.setAdapter(new CourseFragmentAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager, true);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);

        previousToolbarTitle = collapsingToolbarLayout.getTitle();
        collapsingToolbarLayout.setTitle(this.lessonName);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(previousToolbarTitle);
    }

    class CourseFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public CourseFragmentAdapter(FragmentManager fm) {
            super(fm);

            fragmentList = new ArrayList<>();

            try {
                fragmentList.add(CourseFragment.getInstance(CourseFragment.ANNOUNCEMENT_FRAGMENT, lessonID, lessonName));
                fragmentList.add(CourseFragment.getInstance(CourseFragment.HOMEWORK_FRAGMENT, lessonID, lessonName));
                fragmentList.add(CourseFragment.getInstance(CourseFragment.MATERIAL_FRAGMENT, lessonID, lessonName));
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

package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    public static ClassroomFragment getInstance(String lessonId) {
        ClassroomFragment fragment = new ClassroomFragment();
        fragment.lessonID = lessonId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_course, container, false);
        final ViewPager viewPager = (ViewPager) root.findViewById(R.id.course_viewPager);
        final TabLayout tabLayout = (TabLayout) root.findViewById(R.id.course_tabLayout);

        viewPager.setAdapter(new CourseFragmentAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager, true);
        return root;
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

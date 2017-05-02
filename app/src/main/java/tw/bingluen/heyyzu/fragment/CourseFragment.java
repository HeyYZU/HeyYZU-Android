package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.adapter.AnnouncementAdapter;
import tw.bingluen.heyyzu.adapter.HomeworkAdapter;
import tw.bingluen.heyyzu.adapter.MaterialAdapter;
import tw.bingluen.heyyzu.constant.SPKey;
import tw.bingluen.heyyzu.model.course.CourseAnnouncement;
import tw.bingluen.heyyzu.model.course.CourseHomework;
import tw.bingluen.heyyzu.model.course.CourseMaterial;
import tw.bingluen.heyyzu.network.HeyYZUAPIClient;
import tw.bingluen.heyyzu.tool.ContextUtils;

public class CourseFragment extends Fragment {
    public static final int ANNOUNCEMENT_FRAGMENT = 0;
    public static final int HOMEWORK_FRAGMENT = 1;
    public static final int MATERIAL_FRAGMENT = 2;

    @IntDef({ANNOUNCEMENT_FRAGMENT, HOMEWORK_FRAGMENT, MATERIAL_FRAGMENT})
    public @interface CourseFragmentType{}

    protected String lessonID;

    public static CourseFragment getInstance(@CourseFragmentType int fragmentType, @NonNull String lessonID) throws Exception {
        CourseFragment fragment;

        switch (fragmentType) {
            case ANNOUNCEMENT_FRAGMENT:
                fragment = new AnnouncementFragment();
                break;
            case HOMEWORK_FRAGMENT:
                fragment = new HomeworkFragment();
                break;
            case MATERIAL_FRAGMENT:
                fragment = new MaterialFragment();
                break;
            default:
                throw new Exception("CourseFragmentType is not expected.");
        }

        fragment.lessonID = lessonID;

        return fragment;
    }

    public static class MaterialFragment extends CourseFragment {
        private List<CourseMaterial> dataList;

        private MaterialAdapter adapter;

        private ProgressBar progressBar;

        private View exceptionView;

        private RecyclerView recyclerView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            dataList = new ArrayList<>();

            prepareData();

            View root = inflater.inflate(R.layout.recycle_view, container, false);
            recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
            progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
            exceptionView = root.findViewById(R.id.exception_view);

            adapter = new MaterialAdapter(dataList, new MaterialAdapter.MaterialCallback() {
                @Override
                public void showMaterial(View v, int position, CourseMaterial material) {
                    tw.bingluen.heyyzu.fragment.MaterialFragment.getInstance(material).show(getChildFragmentManager(), "showMaterial");
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            return root;
        }

        private void prepareData() {
            String token = ContextUtils.getSP(this, SPKey.NAME, Context.MODE_PRIVATE).getString(SPKey.USER_ACCESS_TOKEN, "");

            if (token.length() > 0) {
                Call<List<CourseMaterial>> materialCall = HeyYZUAPIClient.get().courseMaterials(lessonID, token);
                materialCall.enqueue(new Callback<List<CourseMaterial>>() {
                    @Override
                    public void onResponse(Call<List<CourseMaterial>> call, Response<List<CourseMaterial>> response) {
                        if (response.isSuccessful()) {
                            for (CourseMaterial material : response.body()) {
                                dataList.add(material);
                            }
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            if (dataList.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                exceptionView.setVisibility(View.VISIBLE);
                                ((TextView) exceptionView.findViewById(R.id.txt_exception_view)).setText(R.string.txt_material_empty);
                            }
                        } else {
                            // GG
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CourseMaterial>> call, Throwable t) {
                        // 網路掛了
                    }
                });
            }
        }


    }

    public static class HomeworkFragment extends CourseFragment {
        private List<CourseHomework> dataList;

        private HomeworkAdapter adapter;

        private ProgressBar progressBar;

        private View exceptionView;

        private RecyclerView recyclerView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            dataList = new ArrayList<>();

            View root = inflater.inflate(R.layout.recycle_view, container, false);
            recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
            progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
            exceptionView = root.findViewById(R.id.exception_view);

            adapter = new HomeworkAdapter(dataList, new HomeworkAdapter.HomeworkCallback() {
                @Override
                public void showHomework(View v, int position, CourseHomework homework) {
                    FragmentHelper helper = (FragmentHelper) getActivity();
                    helper.replaceContentFragment(tw.bingluen.heyyzu.fragment.HomeworkFragment.getInstance(homework), false);
                }
            });

            prepareData();

            recyclerView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            return root;
        }

        public void prepareData() {
            String token = ContextUtils.getSP(this, SPKey.NAME, Context.MODE_PRIVATE).getString(SPKey.USER_ACCESS_TOKEN, "");

            if (token.length() > 0) {
                Call<List<CourseHomework>> homeworkCall = HeyYZUAPIClient.get().courseHomeworks(lessonID, token);
                homeworkCall.enqueue(new Callback<List<CourseHomework>>() {
                    @Override
                    public void onResponse(Call<List<CourseHomework>> call, Response<List<CourseHomework>> response) {
                        if (response.isSuccessful()) {
                            for (CourseHomework homework : response.body()) {
                                dataList.add(homework);
                            }
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            if (dataList.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                exceptionView.setVisibility(View.VISIBLE);
                                ((TextView) exceptionView.findViewById(R.id.txt_exception_view)).setText(R.string.txt_homework_empty);
                            }
                        } else {
                            // GG
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CourseHomework>> call, Throwable t) {
                        // 網路爛了
                    }
                });
            }
        }
    }

    public static class AnnouncementFragment extends CourseFragment {
        private List<CourseAnnouncement> dataList;

        private AnnouncementAdapter adapter;

        private ProgressBar progressBar;

        private View exceptionView;

        private RecyclerView recyclerView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            dataList = new ArrayList<>();

            prepareData();

            View root = inflater.inflate(R.layout.recycle_view, container, false);
            recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
            progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
            exceptionView = root.findViewById(R.id.exception_view);

            adapter = new AnnouncementAdapter(dataList, new AnnouncementAdapter.AnnouncementCallback() {
                @Override
                public void showAnnouncement(View v, int position, CourseAnnouncement announcement) {
                    FragmentHelper helper = (FragmentHelper) getActivity();
                    helper.replaceContentFragment(tw.bingluen.heyyzu.fragment.AnnouncementFragment.getInstance(announcement), false);
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);



            return root;
        }

        public void prepareData() {

            String token = ContextUtils.getSP(this, SPKey.NAME, Context.MODE_PRIVATE).getString(SPKey.USER_ACCESS_TOKEN, "");

            if (token.length() > 0) {
                Call<List<CourseAnnouncement>> announcementCall = HeyYZUAPIClient.get().courseAnnouncements(lessonID, token);
                announcementCall.enqueue(new Callback<List<CourseAnnouncement>>() {
                    @Override
                    public void onResponse(Call<List<CourseAnnouncement>> call, Response<List<CourseAnnouncement>> response) {
                        if (response.isSuccessful()) {
                            for (CourseAnnouncement announcement : response.body()) {
                                dataList.add(announcement);
                            }
                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);

                            if (dataList.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                exceptionView.setVisibility(View.VISIBLE);
                                ((TextView) exceptionView.findViewById(R.id.txt_exception_view)).setText(R.string.txt_announcement_empty);
                            }

                        } else {
                            // GG
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CourseAnnouncement>> call, Throwable t) {
                        // 網路炸了
                    }
                });
            }
        }

    }
}

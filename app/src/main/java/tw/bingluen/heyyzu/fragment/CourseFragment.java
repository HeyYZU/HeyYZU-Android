package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public abstract class CourseFragment extends Fragment {
    public static final int ANNOUNCEMENT_FRAGMENT = 0;
    public static final int HOMEWORK_FRAGMENT = 1;
    public static final int MATERIAL_FRAGMENT = 2;

    @IntDef({ANNOUNCEMENT_FRAGMENT, HOMEWORK_FRAGMENT, MATERIAL_FRAGMENT})
    public @interface CourseFragmentType{}

    protected String lessonID;
    protected String lessonName;

    public static CourseFragment getInstance(@CourseFragmentType int fragmentType, @NonNull String lessonID, @NonNull String lessonName) throws Exception {
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
        fragment.lessonName = lessonName;

        return fragment;
    }

    protected void errorResponseHandler(int responseCode, @Nullable View rootExceptionView) {
        rootExceptionView.setVisibility(View.VISIBLE);
        ImageView exceptionImage = (ImageView) rootExceptionView.findViewById(R.id.img_exception_view);
        TextView exceptionText = (TextView) rootExceptionView.findViewById(R.id.txt_exception_view);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) exceptionImage.getLayoutParams();
        params.width *= 3;
        params.height *= 3;
        exceptionImage.setLayoutParams(params);
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
                exceptionImage.setImageResource(R.drawable.ic_busying);
                exceptionText.setText(R.string.dialog_message_yzu_server_error);
                break;
            case 0:
                exceptionImage.setImageResource(R.drawable.ic_connection_error);
                exceptionText.setText(R.string.dialog_message_network_problem);
                break;
            default:
                exceptionImage.setImageResource(R.drawable.ic_busying);
                exceptionText.setText(R.string.dialog_message_server_error);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareData();
    }

    protected abstract void prepareData();

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

        protected void prepareData() {
            String token = ContextUtils.getSP(this, SPKey.NAME, Context.MODE_PRIVATE).getString(SPKey.USER_ACCESS_TOKEN, "");

            if (token.length() > 0) {
                Call<List<CourseMaterial>> materialCall = HeyYZUAPIClient.get().courseMaterials(lessonID, token);
                materialCall.enqueue(new Callback<List<CourseMaterial>>() {
                    @Override
                    public void onResponse(Call<List<CourseMaterial>> call, Response<List<CourseMaterial>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            for (CourseMaterial material : response.body()) {
                                dataList.add(material);
                            }
                            adapter.notifyDataSetChanged();
                            if (dataList.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                exceptionView.setVisibility(View.VISIBLE);
                                ((TextView) exceptionView.findViewById(R.id.txt_exception_view)).setText(R.string.txt_material_empty);
                            }
                        } else {
                            errorResponseHandler(response.code(), exceptionView);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CourseMaterial>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        errorResponseHandler(0, exceptionView);
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
                    helper.replaceContentFragment(tw.bingluen.heyyzu.fragment.HomeworkFragment.getInstance(homework, lessonName));
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            return root;
        }

        protected void prepareData() {
            String token = ContextUtils.getSP(this, SPKey.NAME, Context.MODE_PRIVATE).getString(SPKey.USER_ACCESS_TOKEN, "");

            if (token.length() > 0) {
                Call<List<CourseHomework>> homeworkCall = HeyYZUAPIClient.get().courseHomeworks(lessonID, token);
                homeworkCall.enqueue(new Callback<List<CourseHomework>>() {
                    @Override
                    public void onResponse(Call<List<CourseHomework>> call, Response<List<CourseHomework>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            for (CourseHomework homework : response.body()) {
                                dataList.add(homework);
                            }
                            adapter.notifyDataSetChanged();
                            if (dataList.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                exceptionView.setVisibility(View.VISIBLE);
                                ((TextView) exceptionView.findViewById(R.id.txt_exception_view)).setText(R.string.txt_homework_empty);
                            }
                        } else {
                            errorResponseHandler(response.code(), exceptionView);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CourseHomework>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        errorResponseHandler(0, exceptionView);
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

            View root = inflater.inflate(R.layout.recycle_view, container, false);
            recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
            progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
            exceptionView = root.findViewById(R.id.exception_view);

            adapter = new AnnouncementAdapter(dataList, new AnnouncementAdapter.AnnouncementCallback() {
                @Override
                public void showAnnouncement(View v, int position, CourseAnnouncement announcement) {
                    FragmentHelper helper = (FragmentHelper) getActivity();
                    helper.replaceContentFragment(tw.bingluen.heyyzu.fragment.AnnouncementFragment.getInstance(announcement, lessonName));
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);



            return root;
        }

        protected void prepareData() {

            String token = ContextUtils.getSP(this, SPKey.NAME, Context.MODE_PRIVATE).getString(SPKey.USER_ACCESS_TOKEN, "");

            if (token.length() > 0) {
                Call<List<CourseAnnouncement>> announcementCall = HeyYZUAPIClient.get().courseAnnouncements(lessonID, token);
                announcementCall.enqueue(new Callback<List<CourseAnnouncement>>() {
                    @Override
                    public void onResponse(Call<List<CourseAnnouncement>> call, Response<List<CourseAnnouncement>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            for (CourseAnnouncement announcement : response.body()) {
                                dataList.add(announcement);
                            }
                            adapter.notifyDataSetChanged();

                            if (dataList.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                exceptionView.setVisibility(View.VISIBLE);
                                ((TextView) exceptionView.findViewById(R.id.txt_exception_view)).setText(R.string.txt_announcement_empty);
                            }

                        } else {
                            errorResponseHandler(response.code(), exceptionView);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CourseAnnouncement>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        errorResponseHandler(0, exceptionView);
                    }
                });
            }
        }

    }
}

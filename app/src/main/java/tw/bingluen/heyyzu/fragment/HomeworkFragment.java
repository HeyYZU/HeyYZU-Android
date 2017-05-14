package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.course.CourseHomework;
import tw.bingluen.heyyzu.model.file.Archive;
import tw.bingluen.heyyzu.model.file.Attach;
import tw.bingluen.heyyzu.tool.ContextUtils;

public class HomeworkFragment extends Fragment {
    private CourseHomework homework;
    private String previousToolbarTitle, lessonName;

    public static HomeworkFragment getInstance(CourseHomework homework, String lessonName) {
        HomeworkFragment fragment = new HomeworkFragment();
        fragment.homework = homework;
        fragment.lessonName = lessonName;
        return fragment;
    }

    @Override
    public void onDestroyView() {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(previousToolbarTitle);
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        previousToolbarTitle = collapsingToolbarLayout.getTitle().toString();
        collapsingToolbarLayout.setTitle(lessonName);

        View root = inflater.inflate(R.layout.fragment_homework, container, false);

        TextView subject, description, deadline, deadlineCountdown, attachFilename, gradeText, comment, gradeTitle;
        ImageButton downloadAttach;
        ConstraintLayout attachment;
        RecyclerView recyclerView;

        subject = (TextView) root.findViewById(R.id.tv_homework_subject);
        description = (TextView) root.findViewById(R.id.tv_homework_description);
        deadline = (TextView) root.findViewById(R.id.tv_homework_deadline);
        deadlineCountdown = (TextView) root.findViewById(R.id.txt_homework_deadline_countdown);
        attachFilename = (TextView) root.findViewById(R.id.tv_homework_attach_filename);
        downloadAttach = (ImageButton) root.findViewById(R.id.btn_download_attach);
        attachment = (ConstraintLayout) root.findViewById(R.id.layout_attachment);
        gradeTitle = (TextView) root.findViewById(R.id.txt_grade);
        gradeText = (TextView) root.findViewById(R.id.txt_score);
        comment = (TextView) root.findViewById(R.id.txt_comment);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        final long deadlineInUnixTimeStamp = homework.getDeadline();

        subject.setText(homework.getSubject());
        description.setText(homework.getContent());
        deadline.setText(DateFormat.getDateFormat(ContextUtils.getContext(this)).format(
                deadlineInUnixTimeStamp * 1000
        ));

        if (deadlineInUnixTimeStamp > System.currentTimeMillis() / 1000) {
            deadlineCountdown.setText(String.format(
                    Locale.getDefault(),
                    getString(R.string.homework_deadline_remaining_days_txt),
                    (deadlineInUnixTimeStamp - (System.currentTimeMillis() / 1000)) / 60 / 60 / 24
            ));
        } else {
            deadlineCountdown.setVisibility(View.GONE);
        }

        final Attach attach = homework.getAttach();

        if (attach == null) {
            attachment.setVisibility(View.GONE);
        } else {
            attachFilename.setText(attach.getFilename());
            Pattern pattern = Pattern.compile("[.](.*)$");
            Matcher matcher = pattern.matcher(attach.getFilename());
            if (matcher.find()) {
                switch (matcher.group(1).toLowerCase()) {
                    case "zip":
                        attachFilename.setCompoundDrawablesWithIntrinsicBounds(
                                ContextUtils.getDrawable(this, R.drawable.ic_zip_box), null, null, null);
                        break;
                    case "pptx":
                    case "ppt":
                    case "odp":
                        attachFilename.setCompoundDrawablesWithIntrinsicBounds(ContextUtils.getDrawable(
                                this, R.drawable.ic_file_powerpoint_box), null, null, null);
                        break;
                    case "docx":
                    case "doc":
                    case "odt":
                        attachFilename.setCompoundDrawablesWithIntrinsicBounds(ContextUtils.getDrawable(
                                this, R.drawable.ic_file_word_box), null, null, null);
                        break;
                    case "xls":
                    case "xlsx":
                    case "ods":
                        attachFilename.setCompoundDrawablesWithIntrinsicBounds(ContextUtils.getDrawable(
                                this, R.drawable.ic_file_excel_box), null, null, null);
                        break;
                    case "pdf":
                        attachFilename.setCompoundDrawablesWithIntrinsicBounds(ContextUtils.getDrawable(
                                this, R.drawable.ic_file_pdf_box), null, null, null);
                        break;
                    case "jpg":
                    case "jpeg":
                    case "png":
                    case "gif":
                    case "bmp":
                    case "svg":
                        attachFilename.setCompoundDrawablesWithIntrinsicBounds(ContextUtils.getDrawable(
                                this, R.drawable.ic_file_image), null, null, null);
                        break;
                    default:
                        attachFilename.setCompoundDrawablesWithIntrinsicBounds(ContextUtils.getDrawable(
                                this, R.drawable.ic_file), null, null, null);
                        break;

                }
            } else {
                attachFilename.setCompoundDrawablesWithIntrinsicBounds(ContextUtils.getDrawable(
                        this, R.drawable.ic_file), null, null, null);
            }

            downloadAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        if (homework.getGrades() != null) {
            gradeText.setText(homework.getGrades());
            comment.setText(homework.getComment());
        } else {
            gradeTitle.setVisibility(View.GONE);
            gradeText.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
        }

        final List<Archive> archiveList = homework.getArchive();

        if (archiveList != null) {
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(ContextUtils.getContext(this)));
            recyclerView.setAdapter(new UploadedFileAdapter(archiveList));
        }

        return root;
    }

    class UploadedFileAdapter extends RecyclerView.Adapter<UploadedFileAdapter.ViewHolder> {

        private List<Archive> archiveList;
        private Context context;

        UploadedFileAdapter(List<Archive> archive) {
            this.archiveList = archive;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_homework_archive, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Archive archive = archiveList.get(position);
            final long datetime = archive.getDatetime();

            final String UploadedTimeString = DateFormat.getDateFormat(context).format(datetime * 1000) +
                    DateFormat.getTimeFormat(context).format(datetime * 1000);

            holder.filename.setText(archive.getFilename());
            holder.uploadedTime.setText(UploadedTimeString);
        }

        @Override
        public int getItemCount() {
            return archiveList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView filename, uploadedTime;
            public ViewHolder(View itemView) {
                super(itemView);
                filename = (TextView) itemView.findViewById(R.id.txt_archive_filename);
                uploadedTime = (TextView) itemView.findViewById(R.id.txt_archive_uploaded_time);
            }
        }


    }
}

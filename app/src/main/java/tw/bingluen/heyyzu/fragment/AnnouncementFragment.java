package tw.bingluen.heyyzu.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.course.CourseAnnouncement;
import tw.bingluen.heyyzu.model.file.Attach;
import tw.bingluen.heyyzu.tool.ContextUtils;

public class AnnouncementFragment extends Fragment {
    private CourseAnnouncement announcement;
    private String previousToolbarTitle, lessonName;

    public static AnnouncementFragment getInstance(CourseAnnouncement announcement, String lessonName) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.announcement = announcement;
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

        View root = inflater.inflate(R.layout.fragment_announcement, container, false);

        TextView subject, content, author, attachFilename;
        ImageButton downloadAttach;
        ConstraintLayout attachLayout;

        subject = (TextView) root.findViewById(R.id.tv_announcement_subject);
        content = (TextView) root.findViewById(R.id.tv_announcement_content);
        author = (TextView) root.findViewById(R.id.tv_announcement_author);
        attachFilename = (TextView) root.findViewById(R.id.tv_announcement_attach_filename);
        downloadAttach = (ImageButton) root.findViewById(R.id.btn_download_attach);
        attachLayout = (ConstraintLayout) root.findViewById(R.id.layout_attachment);

        final String dateTimeString = DateFormat.getDateFormat(ContextUtils.getContext(this)).format(announcement.getDatetime() * 1000) +
                DateFormat.getTimeFormat(ContextUtils.getContext(this)).format(announcement.getDatetime() * 1000);

        subject.setText(announcement.getSubject());
        content.setText(announcement.getContent());
        author.setText(String.format(
                getString(R.string.txt_announcement_signature),
                announcement.getAuthor(),
                dateTimeString
        ));

        final Attach attach = announcement.getAttach();

        if (attach == null) {
            attachLayout.setVisibility(View.GONE);
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

        return root;
    }
}

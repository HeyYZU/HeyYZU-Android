package tw.bingluen.heyyzu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.course.CourseAnnouncement;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private List<CourseAnnouncement> announcementList;
    private AnnouncementCallback mCallback;
    private Context context;

    public AnnouncementAdapter(List<CourseAnnouncement> announcements, AnnouncementCallback callback) {
        announcementList = announcements;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View item = LayoutInflater.from(context).inflate(R.layout.item_announcement, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        final CourseAnnouncement announcement = announcementList.get(pos);
        holder.title.setText(announcement.getSubject());
        holder.content.setText(announcement.getContent());
        holder.datetime.setText(DateFormat.getDateFormat(context).format(
                announcement.getDatetime() * 1000
        ));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.showAnnouncement(v, pos, announcement);
            }
        });
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;
        public TextView datetime;
        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.txt_announcement_title);
            content = (TextView) itemView.findViewById(R.id.txt_announcement_content);
            datetime = (TextView) itemView.findViewById(R.id.txt_announcement_datetime);
        }
    }

    public interface AnnouncementCallback {
        void showAnnouncement(View v, int position, CourseAnnouncement announcement);
    }
}

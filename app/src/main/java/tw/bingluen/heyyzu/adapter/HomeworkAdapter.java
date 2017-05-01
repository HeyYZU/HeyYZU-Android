package tw.bingluen.heyyzu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.course.CourseHomework;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.ViewHolder> {

    private List<CourseHomework> homeworkList;
    private HomeworkCallback mCallback;
    private Context context;

    public HomeworkAdapter(List<CourseHomework> homeworkList, HomeworkCallback callback) {
        this.homeworkList = homeworkList;
        this.mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View item = LayoutInflater.from(context).inflate(R.layout.item_homework, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CourseHomework homework = homeworkList.get(position);
        final long deadline = homework.getDeadline();
        final long duration = deadline - (System.currentTimeMillis() / 1000);

        holder.subject.setText(homework.getSubject());

        if (duration < 0) {

            holder.deadline.setText(String.format(
                    Locale.getDefault(),
                    context.getString(R.string.homework_deadline_has_been_close_txt),
                    DateFormat.getDateFormat(context).format(new Date(deadline * 1000))
            ));
        } else if (duration < 24 * 60 * 6) {
            holder.deadline.setText(String.format(
                    Locale.getDefault(),
                    context.getString(R.string.homework_deadline_remaining_hours_txt),
                    duration / 60 / 60,
                    duration / 60
            ));
        } else {
            holder.deadline.setText(String.format(
                    Locale.getDefault(),
                    context.getString(R.string.homework_deadline_remaining_days_txt),
                    duration / 60 / 60 / 24
            ));
        }

        if (homework.getGrades() != null) {
            holder.score.setText(homework.getGrades());
        } else {
            holder.score.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return homeworkList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView subject, deadline, score;

        public ViewHolder(View itemView) {
            super(itemView);

            subject = (TextView) itemView.findViewById(R.id.txt_homework_subject);
            deadline = (TextView) itemView.findViewById(R.id.txt_homework_deadline);
            score = (TextView) itemView.findViewById(R.id.txt_score);
        }
    }

    public interface HomeworkCallback{
        void showHomework(View v, int position, CourseHomework homework);
    }
}

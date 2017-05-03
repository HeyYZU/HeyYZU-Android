package tw.bingluen.heyyzu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.course.CourseMaterial;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    private List<CourseMaterial> materialList;
    private MaterialCallback mCallback;

    private Context context;

    public MaterialAdapter(List<CourseMaterial> materialList, MaterialCallback mCallback) {
        this.materialList = materialList;
        this.mCallback = mCallback;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_material, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        final CourseMaterial material = materialList.get(pos);
        holder.subject.setText(material.getSubject());

        if (material.getAttach() != null) {
            holder.icDocument.setImageResource(R.drawable.ic_file_document_enabled_small);
        }

        if (material.getWebsite() != null) {
            holder.icLink.setImageResource(R.drawable.ic_file_link_enabled_small);
        }

        if (material.getVideo() != null) {
            holder.icVideo.setImageResource(R.drawable.ic_file_video_enabled_small);
        }

        holder.uploadTime.setText(DateFormat.getDateFormat(context).format(new Date(material.getDatetime() * 1000)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.showMaterial(v, pos, material);
            }
        });
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView subject, uploadTime;
        private ImageView icVideo, icLink, icDocument;
        public ViewHolder(View itemView) {
            super(itemView);
            subject = (TextView) itemView.findViewById(R.id.txt_material_subject);
            uploadTime = (TextView) itemView.findViewById(R.id.txt_material_upload_time);
            icDocument = (ImageView) itemView.findViewById(R.id.img_material_document);
            icLink = (ImageView) itemView.findViewById(R.id.img_material_link);
            icVideo = (ImageView) itemView.findViewById(R.id.img_material_video);
        }
    }

    public interface MaterialCallback {
        void showMaterial(View v, int position, CourseMaterial material);
    }
}

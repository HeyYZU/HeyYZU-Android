package tw.bingluen.heyyzu.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.course.CourseMaterial;

public class MaterialFragment extends DialogFragment {

    private CourseMaterial material;

    public static MaterialFragment getInstance(CourseMaterial material) {
        MaterialFragment fragment = new MaterialFragment();
        fragment.material = material;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_material, container, false);

        TextView subject;
        ImageView handout, link, video;

        subject = (TextView) root.findViewById(R.id.txt_material_subject);
        handout = (ImageView) root.findViewById(R.id.img_handout);
        link = (ImageView) root.findViewById(R.id.img_link);
        video = (ImageView) root.findViewById(R.id.img_video);

        subject.setText(material.getSubject());

        if (material.getAttach() != null) {
            handout.setImageResource(R.drawable.ic_file_handout_enabled_big);
            handout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        if (material.getWebsite() != null) {
            link.setImageResource(R.drawable.ic_file_link_enabled_big);
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        if (material.getVideo() != null) {
            video.setImageResource(R.drawable.ic_file_video_enabled_big);
            video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        return root;
    }
}

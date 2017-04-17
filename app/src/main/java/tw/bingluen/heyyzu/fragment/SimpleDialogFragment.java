package tw.bingluen.heyyzu.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import tw.bingluen.heyyzu.R;

public class SimpleDialogFragment extends DialogFragment {

    private int dialogType;

    public static final int NORMAL_WITH_ONE_BUTTON = 0b00;
    public static final int NORMAL_WITH_TWO_BUTTON = 0b10;
    public static final int ERROR_WITH_ONE_BUTTON = 0b01;
    public static final int ERROR_WITH_TWO_BUTTON = 0b11;

    private static final int NORMAL = 0b00;
    private static final int ERROR = 0b01;
    private static final int ONE_BUTTON = 0b00;
    private static final int TWO_BUTTON = 0b10;

    private View rootView;
    private DialogInterface.OnClickListener leftCallback, rightCallback;
    private int titleRes, rightBtnRes, leftBtnRes, messageRes;
    private String messageStr;
    private boolean enableLeftBtn, canceledOnTouchOutside;

    @IntDef({NORMAL_WITH_ONE_BUTTON, NORMAL_WITH_TWO_BUTTON, ERROR_WITH_ONE_BUTTON, ERROR_WITH_TWO_BUTTON})
    private @interface DialogType{}

    public static SimpleDialogFragment getInstance(@DialogType int dialogType) {
        SimpleDialogFragment simpleDialogFragment = new SimpleDialogFragment();
        simpleDialogFragment.dialogType = dialogType;

        return simpleDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(canceledOnTouchOutside);

        switch(dialogType & 0b01) {
            case ERROR:
                rootView = inflater.inflate(R.layout.fragment_simple_dialog_error, container, false);
                break;
            case NORMAL:
            default:
                rootView = inflater.inflate(R.layout.fragment_simple_dialog_normal, container, false);
                break;
        }

        enableLeftBtn = (dialogType & 0b10) == TWO_BUTTON;

        // Initialize Text
        initialText();

        return rootView;
    }

    private void initialText() {
        final TextView title = (TextView) rootView.findViewById(R.id.txt_dialog_title);
        title.setText(titleRes);

        final Button rightButton = (Button) rootView.findViewById(R.id.btn_right);
        rightButton.setText(rightBtnRes);

        final Button leftButton = (Button) rootView.findViewById(R.id.btn_left);
        leftButton.setText(leftBtnRes);

        final TextView message = (TextView) rootView.findViewById(R.id.txt_dialog_message);
        try {
            message.setText(messageRes);
        } catch (Resources.NotFoundException e) {
            message.setText(messageStr);
        } catch (NullPointerException e) {
            message.setText("Message Not Set");
        }

    }

    public SimpleDialogFragment setTitle(@StringRes int stringRes) {
        titleRes = stringRes;
        return this;
    }

    public SimpleDialogFragment setRightButtonText(@StringRes int stringRes) {
        rightBtnRes = stringRes;
        return this;
    }

    public SimpleDialogFragment setLeftButtonText(@StringRes int stringRes) {
        leftBtnRes = stringRes;
        return this;
    }

    public SimpleDialogFragment setMessage(@StringRes int stringRes) {
        messageRes = stringRes;
        return this;
    }

    public SimpleDialogFragment setMessage(String string) {
        messageStr = string;
        return this;
    }

    public SimpleDialogFragment setRightButtonCallback(DialogInterface.OnClickListener callback) {
        rightCallback = callback;
        return this;
    }

    public SimpleDialogFragment setLeftButtonCallback(DialogInterface.OnClickListener callback) {
        leftCallback = callback;
        return this;
    }

    public SimpleDialogFragment setCanceledOnTuochOutside(boolean enable) {
        canceledOnTouchOutside = enable;
        return this;
    }


}

package tw.bingluen.heyyzu.activity;

import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import tw.bingluen.heyyzu.R;

public class LoginActivity extends AppCompatActivity {

    private ImageView logo;
    private ConstraintLayout formView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logo = (ImageView) findViewById(R.id.logo);
        formView = (ConstraintLayout) findViewById(R.id.form_layout);

        // Add keyboard observer
        keyboardShowObserver();

    }

    private void keyboardShowObserver() {
        // Threshold for minimal keyboard height.
        final int MIN_KEYBOARD_HEIGHT_PX = 150;

        // Top-level window decor view.
        final View decorView = getWindow().getDecorView();

        // Register global layout listener.
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private final Rect windowsVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                // Retrieve visible rectangle inside window.
                decorView.getWindowVisibleDisplayFrame(windowsVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowsVisibleDisplayFrame.height();

                // Decide whether keyboard is visible from changing decor view height.
                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        int currentKeyboardHeight = decorView.getHeight() - windowsVisibleDisplayFrame.bottom;
                        keyboardDidShow(currentKeyboardHeight);
                    } else if(lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        keyboardDidHide();
                    }
                }

                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });

    }

    protected void keyboardDidShow(int currentKeyboardHeight) {
        zoomOutLogo();
    }

    protected void keyboardDidHide() {
        zoomInLogo();
    }

    private void zoomOutLogo() {
        ConstraintLayout.LayoutParams logoParams = (ConstraintLayout.LayoutParams) logo.getLayoutParams();
        logoParams.topMargin *= 0.5;
        logoParams.height *= 0.75;
        logoParams.width *= 0.75;
        logo.setLayoutParams(logoParams);

        ConstraintLayout.LayoutParams formParams = (ConstraintLayout.LayoutParams) formView.getLayoutParams();
        formParams.topMargin *= 0.4;
        formView.setLayoutParams(formParams);

        getWindow().getDecorView().invalidate();
    }

    private void zoomInLogo() {
        ConstraintLayout.LayoutParams logoParams = (ConstraintLayout.LayoutParams) logo.getLayoutParams();
        logoParams.topMargin /= 0.5;
        logoParams.height /= 0.75;
        logoParams.width /= 0.75;
        logo.setLayoutParams(logoParams);

        ConstraintLayout.LayoutParams formParams = (ConstraintLayout.LayoutParams) formView.getLayoutParams();
        formParams.topMargin /= 0.4;
        formView.setLayoutParams(formParams);

        getWindow().getDecorView().invalidate();
    }
}

package tw.bingluen.heyyzu.activity;

import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.AccessToken;
import tw.bingluen.heyyzu.model.PublicKey;
import tw.bingluen.heyyzu.network.YZUAPIClient;
import tw.bingluen.heyyzu.tool.RSACipher;
import tw.bingluen.heyyzu.tool.RSAUtils;

public class LoginActivity extends AppCompatActivity {

    private ImageView logo;
    private ConstraintLayout formView;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logo = (ImageView) findViewById(R.id.logo);
        formView = (ConstraintLayout) findViewById(R.id.form_layout);
        btnLogin = (Button) findViewById(R.id.btn_login);


        // Add keyboard observer
        keyboardShowObserver();

        // Add ViewListener
        setViewListener();
    }

    private void setViewListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = ((EditText) findViewById(R.id.username)).getText().toString();
                final String password = ((EditText) findViewById(R.id.password)).getText().toString();
                if (username.length() > 0 && password.length() > 0) getRSAKey();
            }
        });
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

    private void getRSAKey() {
        final Call<PublicKey> getRSAPubKey = YZUAPIClient.getRSAKey();
        getRSAPubKey.enqueue(new Callback<PublicKey>() {
            @Override
            public void onResponse(Call<PublicKey> call, Response<PublicKey> response) {
                if(response.isSuccessful()) {
                    try {
                        doLogin(RSAUtils.genEncryptorFromPublicKey(response.body()));
                    } catch (Exception e) {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<PublicKey> call, Throwable t) {

            }
        });
    }

    private void doLogin(RSACipher rsaCipher) throws Exception {
        final String username = ((EditText) findViewById(R.id.username)).getText().toString();
        final String password = ((EditText) findViewById(R.id.password)).getText().toString();

        final Call<AccessToken> getToken = YZUAPIClient.getAccessToken(
                rsaCipher.getEncryptTextOnBase64(username),
                rsaCipher.getEncryptTextOnBase64(password)
        );

        getToken.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful() && response.body().getToken() != null) {
                    saveAccessToken(response.body());
                } else {
                    // login fail
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                // request fail
            }
        });

    }

    protected void saveAccessToken(AccessToken accessToken) {
        // Save accessToken
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

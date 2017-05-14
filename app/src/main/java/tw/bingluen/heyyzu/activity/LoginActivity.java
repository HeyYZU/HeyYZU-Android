package tw.bingluen.heyyzu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.constant.SPKey;
import tw.bingluen.heyyzu.fragment.SimpleDialogFragment;
import tw.bingluen.heyyzu.model.AccessToken;
import tw.bingluen.heyyzu.model.PublicKey;
import tw.bingluen.heyyzu.network.YZUAPIClient;
import tw.bingluen.heyyzu.tool.RSACipher;
import tw.bingluen.heyyzu.tool.RSAUtils;

public class LoginActivity extends AppCompatActivity {

    private ImageView logo;
    private ConstraintLayout formView;
    private Button btnLogin;
    private EditText etUsername, etPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logo = (ImageView) findViewById(R.id.logo);
        formView = (ConstraintLayout) findViewById(R.id.form_layout);
        btnLogin = (Button) findViewById(R.id.btn_login);
        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        // Add keyboard observer
        keyboardShowObserver();

        // Add ViewListener
        setViewListener();

        // Check Token
        checkToken();
    }

    private void checkToken() {
        SharedPreferences sp = getSharedPreferences(SPKey.NAME, MODE_PRIVATE);
        long tokenExpired = sp.getLong(SPKey.ACCESS_TOKEN_EXPIRED, 0);

        if (tokenExpired > 0 && tokenExpired > System.currentTimeMillis() / 1000) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setViewListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                if (username.length() > 0 && password.length() > 0) {
                    getRSAKey();
                    showProgressbar(true);
                } else {
                    Toast.makeText(getBaseContext(), R.string.toast_message_username_or_password_is_empty, Toast.LENGTH_LONG)
                            .show();
                }
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
                final SimpleDialogFragment dialog;
                if(response.isSuccessful()) {
                    try {
                        doLogin(RSAUtils.genEncryptorFromPublicKey(response.body()));
                    } catch (Exception e) {
                        showProgressbar(false);
                        dialog = SimpleDialogFragment.getInstance(SimpleDialogFragment.ERROR_WITH_ONE_BUTTON);
                        dialog.setTitle(R.string.dialog_title_oops)
                                .setMessage(R.string.dialog_message_yzu_server_error)
                                .setRightButtonText(R.string.btn_ok)
                                .setRightButtonCallback(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                })
                                .show(getFragmentManager(), "Dialog");
                    }
                } else {
                    showProgressbar(false);
                    switch(response.code()) {
                        case 401:
                            dialog = SimpleDialogFragment.getInstance(SimpleDialogFragment.NORMAL_WITH_ONE_BUTTON);
                            dialog.setTitle(R.string.dialog_title_please_upgrade)
                                    .setMessage(R.string.dialog_message_please_upgrade_client)
                                    .setRightButtonText(R.string.btn_ok)
                                    .setRightButtonCallback(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show(getFragmentManager(), "Dialog");
                            break;
                        case 500:
                            dialog = SimpleDialogFragment.getInstance(SimpleDialogFragment.ERROR_WITH_ONE_BUTTON);
                            dialog.setTitle(R.string.dialog_title_oops)
                                    .setMessage(R.string.dialog_message_yzu_server_error)
                                    .setRightButtonText(R.string.btn_ok)
                                    .setRightButtonCallback(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show(getFragmentManager(), "Dialog");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<PublicKey> call, Throwable t) {
                showProgressbar(false);
                final SimpleDialogFragment dialog = SimpleDialogFragment.getInstance(SimpleDialogFragment.ERROR_WITH_ONE_BUTTON);
                dialog.setTitle(R.string.dialog_title_oops)
                        .setMessage(R.string.dialog_message_network_problem)
                        .setRightButtonText(R.string.btn_ok)
                        .setRightButtonCallback(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        })
                        .show(getFragmentManager(), "Dialog");
            }
        });
    }

    private void doLogin(RSACipher rsaCipher) throws Exception {
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        final Call<AccessToken> getToken = YZUAPIClient.getAccessToken(
                rsaCipher.getEncryptTextOnBase64(username),
                rsaCipher.getEncryptTextOnBase64(password)
        );

        getToken.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    if (response.body().getToken() != null) {
                        saveAccessToken(response.body());
                    }

                } else {
                    showProgressbar(false);
                    final SimpleDialogFragment dialog;
                    // login fail
                    switch(response.code()) {
                        case 401:
                            dialog = SimpleDialogFragment.getInstance(SimpleDialogFragment.NORMAL_WITH_ONE_BUTTON);
                            dialog.setTitle(R.string.dialog_title_please_upgrade)
                                    .setMessage(R.string.dialog_message_please_upgrade_client)
                                    .setRightButtonText(R.string.btn_ok)
                                    .setRightButtonCallback(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show(getFragmentManager(), "Dialog");
                            break;
                        case 500:
                            dialog = SimpleDialogFragment.getInstance(SimpleDialogFragment.ERROR_WITH_ONE_BUTTON);
                            dialog.setTitle(R.string.dialog_title_oops)
                                    .setMessage(R.string.dialog_message_yzu_server_error)
                                    .setRightButtonText(R.string.btn_ok)
                                    .setRightButtonCallback(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show(getFragmentManager(), "Dialog");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                showProgressbar(false);
                if (t instanceof JsonSyntaxException) {
                    final SimpleDialogFragment dialog = SimpleDialogFragment.getInstance(SimpleDialogFragment.ERROR_WITH_ONE_BUTTON);
                    dialog.setTitle(R.string.dialog_title_invalid_user)
                            .setMessage(R.string.dialog_message_invalid_username_or_password)
                            .setRightButtonText(R.string.btn_ok)
                            .setRightButtonCallback(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            })
                            .show(getFragmentManager(), "Dialog");
                } else {
                    // request fail
                    final SimpleDialogFragment dialog = SimpleDialogFragment.getInstance(SimpleDialogFragment.ERROR_WITH_ONE_BUTTON);
                    dialog.setTitle(R.string.dialog_title_oops)
                            .setMessage(R.string.dialog_message_network_problem)
                            .setRightButtonText(R.string.btn_ok)
                            .setRightButtonCallback(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            })
                            .show(getFragmentManager(), "Dialog");
                }
            }
        });

    }

    protected void saveAccessToken(AccessToken accessToken) {
        // Save accessToken
        SharedPreferences sp = getSharedPreferences(SPKey.NAME, MODE_PRIVATE);
        sp.edit()
                .putString(SPKey.USER_ACCESS_TOKEN, accessToken.getToken())
                .putLong(SPKey.ACCESS_TOKEN_EXPIRED, accessToken.getExpired())
                .apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void keyboardDidShow(int currentKeyboardHeight) {
        zoomOutLogo();
    }

    protected void keyboardDidHide() {
        zoomInLogo();
    }

    private void showProgressbar(boolean enable) {
        etUsername.setVisibility(enable ? View.INVISIBLE : View.VISIBLE);
        etPassword.setVisibility(enable ? View.INVISIBLE : View.VISIBLE);
        btnLogin.setVisibility(enable ? View.INVISIBLE : View.VISIBLE);
        progressBar.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    private void zoomOutLogo() {
        ConstraintLayout.LayoutParams logoParams = (ConstraintLayout.LayoutParams) logo.getLayoutParams();
        logoParams.height *= 0.75;
        logoParams.width *= 0.75;
        logo.setLayoutParams(logoParams);

        ConstraintLayout.LayoutParams formParams = (ConstraintLayout.LayoutParams) formView.getLayoutParams();
        formParams.bottomMargin += 250;
        formView.setLayoutParams(formParams);

        getWindow().getDecorView().invalidate();
    }

    private void zoomInLogo() {
        ConstraintLayout.LayoutParams logoParams = (ConstraintLayout.LayoutParams) logo.getLayoutParams();
        logoParams.height /= 0.75;
        logoParams.width /= 0.75;
        logo.setLayoutParams(logoParams);

        ConstraintLayout.LayoutParams formParams = (ConstraintLayout.LayoutParams) formView.getLayoutParams();
        formParams.bottomMargin -= 250;
        formView.setLayoutParams(formParams);

        getWindow().getDecorView().invalidate();
    }
}

package com.suffix.fieldforce.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.LoginResponse;
import com.suffix.fieldforce.model.User;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.log_input_email)
    EditText logInputEmail;
    @BindView(R.id.log_input_password)
    EditText logInputPassword;
    @BindView(R.id.log_btn_login)
    Button logBtnLogin;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private FieldForcePreferences preferences;
    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type_two);
        ButterKnife.bind(this);

        preferences = new FieldForcePreferences(this);
        apiInterface = APIClient.getApiClient().create(APIInterface.class);
    }

    @OnClick(R.id.log_btn_login)
    public void onViewClicked() {
        login();
        //startActivity(new Intent(LoginActivity.this,MainDashboard.class));
        //finish();
    }

    private void login() {
        String userId = logInputEmail.getText().toString().trim();
        String password = logInputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userId)) {
            Snackbar.make(logInputEmail, "Please Enter User ID", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Snackbar.make(logInputPassword, "Please Enter Password", Snackbar.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(preferences.getPushToken())) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
                String newToken = instanceIdResult.getToken();
                Log.i(TAG, "newToken = " + newToken);
                preferences.putPushToken(newToken);
                callLoginService(userId, password, newToken);
            });
        } else {
            callLoginService(userId, password, preferences.getPushToken());
        }
    }

    private void callLoginService(String userId, String password, String token) {
        Call<LoginResponse> loginCall = apiInterface.login(Constants.INSTANCE.getKEY(), userId, password, token);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                try {
                    if (response.body().getResponseCode().equalsIgnoreCase("1")) {
                        User user = response.body().getResponseData();
                        preferences.putUser(user);
                        startActivity(new Intent(LoginActivity.this, MainDashboard.class));
                        finish();
                    } else {
                        Snackbar.make(logBtnLogin, response.body().getResponseText(), Snackbar.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage(), e);
                    Snackbar.make(logBtnLogin, R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onResponse: " + t.getMessage(), t);
                Snackbar.make(logBtnLogin, R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}

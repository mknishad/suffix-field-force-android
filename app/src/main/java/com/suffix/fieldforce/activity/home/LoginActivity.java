package com.suffix.fieldforce.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.abul.api.AbulApiClient;
import com.suffix.fieldforce.abul.api.AbulApiInterface;
import com.suffix.fieldforce.abul.model.AbulLoginResponse;
import com.suffix.fieldforce.abul.model.LoginRequest;
import com.suffix.fieldforce.preference.FieldForcePreferences;

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
  TextView logBtnLogin;
  @BindView(R.id.progressBar)
  ProgressBar progressBar;

  private FieldForcePreferences preferences;
  private AbulApiInterface apiInterface;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login_type_one);
    ButterKnife.bind(this);

    preferences = new FieldForcePreferences(this);
    apiInterface = AbulApiClient.getApiClient().create(AbulApiInterface.class);

    /*if (preferences.getUser() != null) {
      startActivity(new Intent(LoginActivity.this, MainDashboardActivity.class));
      finish();
    }*/
  }

  @OnClick(R.id.log_btn_login)
  public void onViewClicked() {
    login();
    //startActivity(new Intent(LoginActivity.this,MainDashboardActivity.class));
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
        loginAbul(userId, password);
        //callLoginService(userId, password, newToken);
      });
    } else {
      //callLoginService(userId, password, preferences.getPushToken());
      loginAbul(userId, password);
    }
  }

  private void loginAbul(String userId, String password) {
    LoginRequest loginRequest = new LoginRequest(userId, password);
    Call<AbulLoginResponse> abulLoginCall = apiInterface.login(loginRequest);
    abulLoginCall.enqueue(new Callback<AbulLoginResponse>() {
      @Override
      public void onResponse(Call<AbulLoginResponse> call, Response<AbulLoginResponse> response) {
        progressBar.setVisibility(View.GONE);
        try {
          if (response.isSuccessful()) {
            if (response.body().getCode() == 200) {
              //User user = response.body().getResponseData();
              //preferences.putUser(user);
              startActivity(new Intent(LoginActivity.this, MainDashboardActivity.class));
              finish();
            } else {
              progressBar.setVisibility(View.GONE);
              Snackbar.make(logBtnLogin, response.body().getMessage(), Snackbar.LENGTH_SHORT).show();
            }
          } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
          }
        } catch (Exception e) {
          progressBar.setVisibility(View.GONE);
          Log.e(TAG, "onResponse: " + e.getMessage(), e);
          Snackbar.make(logBtnLogin, R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<AbulLoginResponse> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t);
        progressBar.setVisibility(View.GONE);
      }
    });
  }

  /*private void callLoginService(String userId, String password, String token) {
    Call<LoginResponse> loginCall = apiInterface.login(Constants.KEY, userId, password, token);
    loginCall.enqueue(new Callback<LoginResponse>() {
      @Override
      public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        progressBar.setVisibility(View.GONE);
        try {
          if (response.isSuccessful()) {
            if (response.body().getResponseCode().equalsIgnoreCase("1")) {
              User user = response.body().getResponseData();
              preferences.putUser(user);
              startActivity(new Intent(LoginActivity.this, MainDashboardActivity.class));
              finish();

            } else {

              Snackbar.make(logBtnLogin, response.body().getResponseText(), Snackbar.LENGTH_SHORT).show();
            }
          } else {
            Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
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
  }*/
}

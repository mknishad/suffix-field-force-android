package com.suffix.fieldforce.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.suffix.fieldforce.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.log_input_email)
    EditText logInputEmail;
    @BindView(R.id.log_input_password)
    EditText logInputPassword;
    @BindView(R.id.log_btn_login)
    Button logBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type_two);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.log_btn_login)
    public void onViewClicked() {
        startActivity(new Intent(LoginActivity.this,MainDashboard.class));
        finish();
    }
}

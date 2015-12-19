package com.community.tsinghua;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.community.tsinghua.R;
import com.community.tsinghua.app.AppConfig;
import com.community.tsinghua.helper.ParseUtils;
import com.community.tsinghua.helper.PrefManager;

/**
 * Created by LG on 2015-11-24.
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText inputEmail;
    private Button btnLogin;
    private PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
/*
        findViewById(R.id.btnLinkToRegisterScreen).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class); // 액티비티넘어가는거
                        startActivity(intent);
                        finish();

                    }
                });
*/
        ParseUtils.verifyParseConfiguration(this);

        pref = new PrefManager(getApplicationContext());
        if (pref.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        }

        setContentView(R.layout.login);

        inputEmail = (EditText) findViewById(R.id.thu_id);
        btnLogin = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        btnLogin.setOnClickListener(this);
    }
    private void login() {
        String email = inputEmail.getText().toString();

        if (isValidEmail(email)) {

            pref.createLoginSession(email);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid email address!", Toast.LENGTH_LONG).show();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLinkToRegisterScreen:
                login();
                break;
            default:
        }
    }
}

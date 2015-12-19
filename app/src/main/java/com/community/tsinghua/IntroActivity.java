package com.community.tsinghua;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.community.tsinghua.R;

/**
 * Created by LG on 2015-11-24.
 */
public class IntroActivity extends Activity implements Runnable{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro);   //인트로화면 띠움

        (new Thread(this)).start();      //1초기다렸다가 다음화면으로넘어감
    }

    public void run() {
        try {
            Thread.sleep(1000); // 3초
        } catch (Exception e) {
        }

        Intent intent = new Intent(this, LoginActivity.class); // 액티비티넘어가는거
        startActivity(intent);

        finish(); // 뒤로가기눌러도 이화면으로 안들아옴
    }
}

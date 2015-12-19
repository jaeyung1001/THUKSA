package com.community.tsinghua.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.community.tsinghua.JSON.JSONParser;
import com.community.tsinghua.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by CJKIM on 2015. 11. 27..
 */

public class CommunityFragment extends AppCompatActivity {


    private TextView detail_title;
    private TextView detail_author;
    private TextView detail_releasetime;
    private TextView detail_contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_community_detail);

        //load data which is clicked from AppbarActivity
        Intent intent = new Intent(this.getIntent());
        String get_title = intent.getStringExtra("title");
        String get_author = intent.getStringExtra("author");
        String get_contents = intent.getStringExtra("contents");
        String get_releasetime = intent.getStringExtra("time");


        //textview configure
        detail_title = (TextView) findViewById(R.id.detail_title);
        detail_author = (TextView) findViewById(R.id.detail_author);
        detail_releasetime = (TextView) findViewById(R.id.detail_releasetime);
        detail_contents = (TextView) findViewById(R.id.detail_content);


        //set textview data
        detail_title.setText(get_title);
        detail_author.setText(get_author);
        detail_releasetime.setText(get_releasetime);
        detail_contents.setText(get_contents);
    }

}

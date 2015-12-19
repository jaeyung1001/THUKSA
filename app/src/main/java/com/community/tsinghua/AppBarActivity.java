package com.community.tsinghua;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.community.tsinghua.JSON.JSONParser;
import com.community.tsinghua.fragment.CommunityFragment;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/*
 load data from database and show data on listview
 use JSONParser
 */

public class AppBarActivity extends AppCompatActivity {



	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> articleList = new ArrayList<HashMap<String, String>>();

	// url to get all products list
	private static String url_all_account = "http://1.doksuri3.sinaapp.com/CJK_demo_article/get.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ARTICLE = "article";
	private static final String TAG_TITLE = "title";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_RELEASETIME = "releasetime";
	private static final String TAG_CONTENTS = "contents";

	// products JSONArray
	JSONArray account = null;


	@InjectView(R.id.recyclerView)
	RecyclerView recyclerView;
	@InjectView(R.id.toolbar)
	Toolbar toolBar;
	@InjectView(R.id.collapsingToolbarLayout)
	CollapsingToolbarLayout collapsingToolbarLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_bar);
		ButterKnife.inject(this);

		setSupportActionBar(toolBar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		collapsingToolbarLayout.setTitle(getString(R.string.title_activity_app_bar));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		new LoadAllArticle().execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

		private final List<HashMap<String, String>> list;

		public MyAdapter(List<HashMap<String, String>> list) {

			this.list = list;
		}

		@Override
		public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			View view
				= LayoutInflater.from(viewGroup.getContext())
								.inflate(R.layout.custom_listview_item, viewGroup, false);
			return new MyViewHolder(view);
		}

		@Override
		public void onBindViewHolder(MyViewHolder viewHolder, int position) {
			HashMap<String, String> text = list.get(position);

			/*

			Iterator<String> iterator = text.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String)iterator.next();
				Log.d("line 112:", key);

			}
			*/

			//viewHolder.title.setText("title");
			viewHolder.title.setText(text.get("title"));
			viewHolder.author.setText(text.get("author"));
			viewHolder.releasetime.setText(text.get("releasetime"));
			viewHolder.contents = text.get("contents");
		}

		@Override
		public int getItemCount() {
			return list.size();
		}
	}


	private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public TextView title;
		public TextView author;
		public TextView releasetime;
		public String contents;


		private Context context;
		private LayoutInflater inflater;

		public MyViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			title = (TextView) itemView.findViewById(R.id.title);
			author = (TextView) itemView.findViewById(R.id.author);
			releasetime = (TextView) itemView.findViewById(R.id.releasedate);

		}

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(view.getContext(),CommunityFragment.class);
			intent.putExtra("title",title.getText().toString());
			intent.putExtra("author",author.getText().toString());
			intent.putExtra("contents",contents);
			intent.putExtra("time",releasetime.getText().toString());
			startActivity(intent);

		}
	}
	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadAllArticle extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AppBarActivity.this);
			pDialog.setMessage("게시글을 불러오는 중입니다...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_account, "GET", params);



			try {

				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {

					// products found
					// Getting Array of Products
					account = json.getJSONArray(TAG_ARTICLE);

					// looping through All Products
					for (int i = 0; i < account.length(); i++) {
						JSONObject c = account.getJSONObject(i);

						// Storing each json item in variable
						String article_title = c.getString(TAG_TITLE);
						String article_author = c.getString(TAG_AUTHOR);
						String article_releasetime = c.getString(TAG_RELEASETIME);
						String article_contents = c.getString(TAG_CONTENTS);


						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_TITLE, article_title);
						map.put(TAG_AUTHOR, article_author);
						map.put(TAG_RELEASETIME, article_releasetime);
						map.put(TAG_CONTENTS,article_contents);

						// adding HashList to ArrayList
						articleList.add(map);


					}
				}

                /*
                else { // 즉 안에 아무것도 없을때, 일단 생략
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                */

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					recyclerView.setAdapter(new MyAdapter(articleList));



				}
			});

		}

	}
}
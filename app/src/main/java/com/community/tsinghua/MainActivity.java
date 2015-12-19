package com.community.tsinghua;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.community.tsinghua.R;
import com.community.tsinghua.app.AppConfig;
import com.community.tsinghua.fragment.FloatingButtonFragment;
import com.community.tsinghua.fragment.FloatingLabelFragment;
import com.community.tsinghua.fragment.MainFragment;
import com.community.tsinghua.fragment.TabsFragment;
import com.community.tsinghua.helper.ParseUtils;
import com.community.tsinghua.helper.PrefManager;
import com.community.tsinghua.model.Message;
import com.parse.Parse;
import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
	implements NavigationView.OnNavigationItemSelectedListener {

	@InjectView(R.id.main_tool_bar)
	Toolbar toolBar;
	@InjectView(R.id.main_drawer_view)
	NavigationView navigationView;
	@InjectView(R.id.drawer_layout)
	DrawerLayout drawerLayout;

	private ActionBarDrawerToggle mDrawerToggle;

	private static String TAG = MainActivity.class.getSimpleName();

	private Toolbar mToolbar;
	private ListView listView;
	private List<Message> listMessages = new ArrayList<>();
	private MessageAdapter adapter;
	private PrefManager pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);

		Parse.initialize(this, AppConfig.PARSE_APPLICATION_ID, AppConfig.PARSE_CLIENT_KEY);
		ParseInstallation.getCurrentInstallation().saveInBackground();

		setSupportActionBar(toolBar);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		listView = (ListView) findViewById(R.id.list_view);
		adapter = new MessageAdapter(this);
		pref = new PrefManager(getApplicationContext());

		listView.setAdapter(adapter);

		Intent intent = getIntent();

		String email = intent.getStringExtra("email");

		if (email != null) {
			ParseUtils.subscribeWithEmail(pref.getEmail());
		}


		mDrawerToggle
			= new ActionBarDrawerToggle(this, drawerLayout, toolBar,
										R.string.app_name, R.string.app_name);
		mDrawerToggle.setDrawerIndicatorEnabled(true);
		drawerLayout.setDrawerListener(mDrawerToggle);

		navigationView.setNavigationItemSelectedListener(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		String message = intent.getStringExtra("message");

		Message m = new Message(message, System.currentTimeMillis());
		listMessages.add(0, m);
		adapter.notifyDataSetChanged();
	}

	private class MessageAdapter extends BaseAdapter {

		LayoutInflater inflater;

		public MessageAdapter(Activity activity) {
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return listMessages.size();
		}

		@Override
		public Object getItem(int position) {
			return listMessages.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.list_row, null);
			}

			TextView txtMessage = (TextView) view.findViewById(R.id.message);
			TextView txtTimestamp = (TextView) view.findViewById(R.id.timestamp);

			Message message = listMessages.get(position);
			txtMessage.setText(message.getMessage());

			CharSequence ago = DateUtils.getRelativeTimeSpanString(message.getTimestamp(), System.currentTimeMillis(),
					0L, DateUtils.FORMAT_ABBREV_ALL);

			txtTimestamp.setText(String.valueOf(ago));

			return view;
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();

		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction()
			   .replace(R.id.main_frame, MainFragment.newInstance())
			   .commit();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem) {
		int id = menuItem.getItemId();

		Fragment fragment = null;
		switch (id) {
			case R.id.navi_group1_item0:
				fragment = MainFragment.newInstance();
				break;
			case R.id.navi_group1_item1:
				fragment = FloatingLabelFragment.newInstance();
				break;
			case R.id.navi_group1_item2:
				fragment = FloatingButtonFragment.newInstance();
				break;
			case R.id.navi_group1_item3:
				fragment = TabsFragment.newInstance();
				break;
			case R.id.navi_group1_item4:
				startActivity(new Intent(this, AppBarActivity.class));
				break;
			case R.id.action_logout:
				pref.logout();
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
				break;
		}

		if (fragment != null) {
			FragmentManager manager = getSupportFragmentManager();
			manager.beginTransaction()
				   .replace(R.id.main_frame, fragment)
				   .commit();

			drawerLayout.closeDrawers();
			menuItem.setChecked(true);
		}
		return true;
	}

}

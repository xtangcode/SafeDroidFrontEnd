package com.xin.safedroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AppMain extends Activity implements OnItemClickListener {

	private static final boolean INCLUDE_SYSTEM_APPS = false;
	
	private ListView mAppsList;
	private List<App> mApps;
	private AppListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_main);
		
		mAppsList = (ListView) findViewById(R.id.appmainlist);
		mAppsList.setOnItemClickListener(this);
		
		mApps = loadInstalledApps(INCLUDE_SYSTEM_APPS);
		
		mAdapter = new AppListAdapter(getApplicationContext());
		mAdapter.setListItems(mApps);
		mAppsList.setAdapter(mAdapter);
		
		new LoadIconsTask().execute(mApps.toArray(new App[]{}));
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		App app = (App) parent.getItemAtPosition(position);

		try {
			Class<?> ourClass = Class.forName("com.xin.safedroid.PermissionMain");
			Intent ourIntent = new Intent(this, ourClass);
			Bundle b = new Bundle();
			b.putString("appName", app.getPackageName());
			ourIntent.putExtras(b);
			startActivity(ourIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private List<App> loadInstalledApps(boolean includeSystemApps) {
		List<App> apps = new ArrayList<App>();
		
		PackageManager pm = getPackageManager();
		List<PackageInfo> pgs = pm.getInstalledPackages(
								PackageManager.GET_META_DATA);
		
		for (int i=0; i<pgs.size(); i++) {
			PackageInfo pg = pgs.get(i);
			ApplicationInfo ap = pg.applicationInfo;
			if ((!includeSystemApps) && (ap.flags & 
					ApplicationInfo.FLAG_SYSTEM) == 1) {
				continue;
			}
			App tmp = new App();
			tmp.setTitle(ap.loadLabel(pm).toString());
			tmp.setPackageName(pg.packageName);
			tmp.setVersionName(pg.versionName);
			tmp.setVersionCode(pg.versionCode);
			apps.add(tmp);
		}
		return apps;
	}


	private class LoadIconsTask extends AsyncTask<App, Void, Void> {

		@Override
		protected Void doInBackground(App... apps) {

			Map<String, Drawable> icons = new HashMap<String, Drawable>();
			PackageManager pm = getApplicationContext().getPackageManager();
			
			for (App app : apps) {
				String pkgName = app.getPackageName();
				Drawable ico = null;
				try {
					Intent i = pm.getLaunchIntentForPackage(pkgName);
					if (i != null) {
						ico = pm.getActivityIcon(i);
					}
				} catch (NameNotFoundException e) {
					Log.e("ERROR", "Unable to find icon for package '" 
							+ pkgName + "': " + e.getMessage());
				}
				icons.put(app.getPackageName(), ico);
			}
			mAdapter.setIcons(icons);
			
			return null;
		}
		
		protected void onPostExecute(Void result) {
			mAdapter.notifyDataSetChanged();
		}
	}

}

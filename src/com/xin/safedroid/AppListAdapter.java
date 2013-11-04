package com.xin.safedroid;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<App> mApps;
	private Map<String, Drawable> mIcons;
	private Drawable mStdImg;
	
	public AppListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mStdImg = context.getResources().getDrawable(R.drawable.ic_launcher);
	}

	@Override
	public int getCount() {
		return mApps.size();
	}

	@Override
	public Object getItem(int position) {
		return mApps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setIcons(Map<String, Drawable> icons) {
		this.mIcons = icons;
	}
	
	public Map<String, Drawable> getIcons() {
		return mIcons;
	}

	public void setListItems(List<App> list) {
		mApps = list;
	}
	
	@Override
	public View getView(int position, 
						View convertView, 
						ViewGroup parent) {

		AppViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_app_list, null);
			holder = new AppViewHolder();
			holder.mTitle = (TextView)convertView.findViewById(R.id.apptitle);
			holder.mIcon = (ImageView)convertView.findViewById(R.id.appicon);
			convertView.setTag(holder);
		} else {
			holder = (AppViewHolder) convertView.getTag();
		}

		App app = mApps.get(position);

		holder.setTitle(mApps.get(position).getTitle());
		if (mIcons == null || mIcons.get(app.getPackageName()) == null ) {
			holder.setIcon(mStdImg);
		} else {
			holder.setIcon(mIcons.get(app.getPackageName()));
		}

		return convertView;
	}

	public class AppViewHolder {
		private TextView mTitle;
		private ImageView mIcon;

		public void setTitle(String title) {
			mTitle.setText(title);
		}

		public void setIcon(Drawable img) {
			if (img != null) {
				mIcon.setImageDrawable(img);
			}
		}
		
	}

}

package com.xin.safedroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.SimpleAdapter;

public class PermissionListAdapter {

	private static final String PERMISSION_NAME = "pname";
	private static final String STATUS = "status";
	private static final boolean DEFAULT_STATUS = true;
	private static final int LAYOUT_ID = R.layout.adapter_permission_list;
	private static final int PERMISSION_VIEW_ID = R.id.permtitle;
	private static final int SWITCH_VIEW_ID = R.id.permswitch;
	
	private List<HashMap<String, Object>> permissionObjectList;
	private List<String> permissionNameList;
	private Context context;
	private SimpleAdapter adapter;
	private HashMap<String, Boolean> permissionStatus;
	
	public PermissionListAdapter (Context context, String appName) {
		this.context = context;
		setpermissionObjectListAndStatus(appName);
		setAdapter();
	}
	
	private void setpermissionObjectListAndStatus(String appName) {
		try {
			String pm[] = this.context.getPackageManager().
					getPackageInfo(appName, PackageManager.GET_PERMISSIONS).
					requestedPermissions;
			this.permissionObjectList = new ArrayList<HashMap<String,Object>>();
			this.permissionStatus = new HashMap<String, Boolean>();
			this.permissionNameList = new ArrayList<String>();
			if ( pm == null || pm.length == 0) return;
			
			for (String c : pm){
				Permission permission = Permission.newInstance(this.context, 
						c, true);
				if (permission != null) permissionNameList.add( permission.
						getSystemName() );
			}
			
			for (int i=0; i<permissionNameList.size(); i++) {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put(PERMISSION_NAME, Permission.getDisplayName( 
						permissionNameList.get(i)) );
				hm.put(STATUS, DEFAULT_STATUS);
				this.permissionObjectList.add(hm);
				this.permissionStatus.put(permissionNameList.get(i), 
						DEFAULT_STATUS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setAdapter() {
		String from[] = { PERMISSION_NAME, STATUS };
		int to[] = { PERMISSION_VIEW_ID, SWITCH_VIEW_ID };
		this.adapter = new SimpleAdapter(this.context, 
				this.permissionObjectList,
				LAYOUT_ID, from, to);
		
	}
	
	public SimpleAdapter getAdapter() {
		return this.adapter;
	}

	public boolean isEmpty() {
		return this.permissionObjectList.isEmpty();
	}
	
	public boolean getPermissionStatus(int position) {
		return this.permissionStatus.get(
				this.permissionNameList.get(position) );
	}
	
	public void setPermissionStatus(int position, boolean status) {
		this.permissionStatus.put( 
				this.permissionNameList.get(position), 
				status);
	}
	
	public String getPermissionName(int position) {
		return this.permissionNameList.get(position);
	}
	
	public int getPermissionNum(){
		return this.permissionNameList.size();
	}
	
}
package com.xin.safedroid;

import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;

public class Permission {

	private static final String ANDROID_PERMISSION = "android.permission";
	private String displayName;
	private String systemName;
	private PermissionInfo info;
	
	public static Permission newInstance(Context context, String systemName,
									boolean onlyAndroidPermission) {
		if (onlyAndroidPermission && 
				!systemName.contains(ANDROID_PERMISSION)) return null;
		
		return new Permission(context,systemName);
	}
	
	private Permission (Context context, String systemName) {
		this.systemName = systemName;
		this.displayName = setDisplayName(systemName);
		try {
			PackageManager pm = context.getPackageManager();
			this.info = pm.getPermissionInfo(systemName, 
							PackageManager.GET_META_DATA);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String getDisplayName() {
		return displayName;
	}
	
	
	public String getSystemName() {
		return systemName;
	}
	
	
	public PermissionInfo getInfo() {
		return info;
	}
	
	
	static private String setDisplayName(String systemName) {
		String tmp = systemName.substring( 
			systemName.lastIndexOf(".")+1 ).
			toLowerCase(Locale.ENGLISH).replace('_', ' ');
		
		String displayName = "";
		boolean firstChar = true;
		for (int i=0; i<tmp.length(); i++){
			String c = tmp.substring(i, i+1);
			if (firstChar) { 
				displayName += c.toUpperCase(Locale.ENGLISH);
				firstChar = false;
			}
			else displayName += c;
			if (c.equals(" ")) firstChar = true;
		}
		
		return displayName;
	}
	
	static public String getDisplayName(String systemName) {
		String tmp = systemName.substring( 
				systemName.lastIndexOf(".")+1 ).
				toLowerCase(Locale.ENGLISH).replace('_', ' ');
			
			String displayName = "";
			boolean firstChar = true;
			for (int i=0; i<tmp.length(); i++){
				String c = tmp.substring(i, i+1);
				if (firstChar) { 
					displayName += c.toUpperCase(Locale.ENGLISH);
					firstChar = false;
				}
				else displayName += c;
				if (c.equals(" ")) firstChar = true;
			}
			
			return displayName;
	}
	
}
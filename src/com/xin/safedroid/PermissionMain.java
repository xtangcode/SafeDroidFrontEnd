package com.xin.safedroid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ToggleButton;

public class PermissionMain extends Activity  {

	PermissionListAdapter myPermissionListAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_permission_main);
		ListView lvPermission = (ListView) findViewById(R.id.lv_permissions);
		Bundle b = getIntent().getExtras();
		String appName = b.getString("appName");
		myPermissionListAdapter = 
				new PermissionListAdapter(
				getApplicationContext(), appName);
		if (myPermissionListAdapter.isEmpty()) {
			lvPermission.setEmptyView(findViewById(R.id.emptyView));
		} else {
			lvPermission.setAdapter( myPermissionListAdapter.getAdapter() );
		}
		
		OnItemClickListener itemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> lv, View item, int position,
					long id) {
				ListView lView = (ListView) lv;
				SimpleAdapter adapter = (SimpleAdapter) lView.getAdapter();
				HashMap<String, Object> hm = (HashMap) adapter.
						getItem(position);
				
				RelativeLayout rLayout = (RelativeLayout) item;
				ToggleButton tgl = (ToggleButton) rLayout.getChildAt(1);
				
				String strStatus = "";
				if ( tgl.isChecked() ) {
					tgl.setChecked(false);
					strStatus = "Off";
					myPermissionListAdapter.setPermissionStatus(position, false);
				} else {
					tgl.setChecked(true);
					strStatus = "On";
					myPermissionListAdapter.setPermissionStatus(position, true);
				}
//				Toast.makeText(getBaseContext(), (String) hm.get("pname") + 
//						" : " + strStatus, Toast.LENGTH_SHORT).show();
			}
			
		};
		
		lvPermission.setOnItemClickListener(itemClickListener);


		Button save = (Button) findViewById(R.id.btn_save);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String fout = getPermissionsInJSON();
				writeToFile("permission.txt", fout);
				Toast.makeText(getBaseContext(), 
						"Written permissions to download folder", 
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private String getPermissionsInJSON() {
		String jstr = "{\n";
		int num = myPermissionListAdapter.getPermissionNum();
		for (int i=0; i<num; i++) {
			jstr += "   \"" + 
					myPermissionListAdapter.getPermissionName(i) +
					"\" : " + 
					String.valueOf( myPermissionListAdapter.
							getPermissionStatus(i) ) +
					",\n";
		}
		if (jstr.length() > 2) {
			// not empty, remove the trailing comma
			jstr = jstr.substring(0, jstr.length()-2) + "\n"; 
		}
		jstr += "}";
		
		return jstr;
	}
	
	
	private void writeToFile(String fileName, String data) {
	    try {
	    	File fout = new File(
	    			Environment.
	    			getExternalStoragePublicDirectory(DOWNLOAD_SERVICE), 
	    			fileName);
			if (fout.exists()) {
				fout.delete();
			}
			fout.createNewFile();
			
	    	FileOutputStream fop = new FileOutputStream(fout);
			byte[] contentInBytes = data.getBytes();
 			fop.write(contentInBytes);
			fop.flush();
			fop.close();

	    }
	    catch (IOException e) {
	    	e.printStackTrace();
	    } 
	}


}

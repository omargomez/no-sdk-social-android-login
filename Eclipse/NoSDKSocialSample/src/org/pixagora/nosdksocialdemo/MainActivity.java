package org.pixagora.nosdksocialdemo;

import org.pixagora.nosdksocial.facebook.FacebookLoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	
	protected static final String TAG = "MainActivity";

	private OnClickListener facebookListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			// Open facebook dialog
			Log.d(TAG, "onClick");
			Intent intent = new Intent();
			intent.setClass( MainActivity.this, FacebookLoginActivity.class);
			//intent.putExtra("major", targetMajor);
			//intent.putExtra("minor", targetMinor);
			startActivityForResult(intent, 200);
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnFacebook = (Button) findViewById(R.id.main_facebook);
		btnFacebook.setOnClickListener(facebookListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "onActivityResult");
	}
}

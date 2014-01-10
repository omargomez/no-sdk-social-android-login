package org.pixagora.nosdksocialdemo;

import org.json.JSONObject;
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
			//TODO: Utility class to encapsulate this
			Log.d(TAG, "onClick");
			Intent intent = new Intent();
			intent.setClass( MainActivity.this, FacebookLoginActivity.class);
			intent.putExtra("client_id", "358781964212408");
			startActivityForResult(intent, FacebookLoginActivity.class.getName().hashCode());
			
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
		Log.i(TAG, "onActivityResult: " + requestCode);
		super.onActivityResult(requestCode, resultCode, data);
		
		//TODO: Utility class to encapsulate this
		if (data != null) {
			Bundle extras = data.getExtras();
			String meData =  extras.getString("me_data");
			Log.d(TAG, meData );
		} else {
			Log.d(TAG, "Canceled activity");
		}
	}
}

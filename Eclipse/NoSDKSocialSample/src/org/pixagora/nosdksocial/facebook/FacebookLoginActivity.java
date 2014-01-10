package org.pixagora.nosdksocial.facebook;

import java.io.Serializable;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.pixagora.nosdksocialdemo.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.Ostermiller.util.CGIParser;
import com.tylersmith.net.RequestMethod;
import com.tylersmith.net.RestClient;

public class FacebookLoginActivity extends Activity {

	public static final String REDIRECT_URI = "https://www.facebook.com/connect/login_success.html";
	protected static String OAUTH_ENDPOINT = "https://www.facebook.com/dialog/oauth/";
	private WebView webView;
	private ProgressBar progressIndicator;

	static class FbWebViewClient extends WebViewClient {
		
		private Activity activity;
		private ProgressBar progressIndicator;
		
		public FbWebViewClient( Activity activity, ProgressBar progressInd) {
			this.activity = activity;
			//this.callaback = callback;
			this.progressIndicator = progressInd;
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			this.progressIndicator.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			
			this.progressIndicator.setVisibility(View.GONE);
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(this.getClass().getName(), "return url: " + url);
			
			try {
				URL theUrl = new URL(url);
				String path = theUrl.getPath();
				String query = theUrl.getQuery();
				
				if (query!=null) {
					CGIParser cgiPrsr = new CGIParser( query, "UTF-8");
					//Log.d(TAG, "access: "+cgiPrsr.getParameter("access_token"));
				}
			
				if (path.equalsIgnoreCase("/connect/login_success.html")) {
					// Se logueo!
					int start = url.indexOf("#");
					if (start == -1) {
						return false;//huh?
					}
					
					String params = url.substring(start+1); 
					CGIParser cgiPrsr = new CGIParser( params, "UTF-8");
					final String access_token = cgiPrsr.getParameter("access_token");
					if (access_token == null) {
						// error fatal, que hacer?
						return false;
					}
					
					// Lets proceeed ...
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							final String meData = getMeData( access_token );
							
							activity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									//TODO: Retornar un status
									Intent returnIntent = new Intent();
									returnIntent.putExtra("me_data", meData );
									activity.setResult(200, returnIntent);
									activity.finish();
								}
							});
						}
					
					}).start();
					
					return true;

					
				}
				else {
					;
				}
				
				return false;
	
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private String getMeData(String access_token) {
			
			try {
				RestClient client = new RestClient(
						"https://graph.facebook.com/me");
				
				client.addParam("access_token", access_token);
				client.addParam("scope", "email");
				
				client.execute(RequestMethod.GET);
				if (client.getResponseCode() == 200) {
					 //TODO: Map<String, String> result = (Map<String,String>)JSONValue.parse(client.getResponse());
					return client.getResponse();
				} else {
					//online failed
					throw  new RuntimeException("Error en tales 2"); // TODO
				}
			} catch (Throwable throwable) {
				throw new RuntimeException(throwable);
			}
				
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		//TODO: assert parameters
		String clientId = getIntent().getExtras().getString("client_id");
		setContentView(R.layout.nosdka_facebook_layout);
		progressIndicator = (ProgressBar) findViewById(R.id.facebook_load_progress);
		webView = (WebView) findViewById(R.id.webview);
		Bundle parameters = new Bundle();
		parameters.putString("client_id", clientId);
		parameters.putString("response_type", "token");
		parameters.putString("scope", "email");
		parameters.putString("redirect_uri", REDIRECT_URI );
		String url = OAUTH_ENDPOINT + "?" + Util.encodeUrl(parameters);
		Log.d(this.getClass().getName(), "url: " + url);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebViewClient(new FbWebViewClient(this,  progressIndicator));
		webView.loadUrl(url);
	}

}

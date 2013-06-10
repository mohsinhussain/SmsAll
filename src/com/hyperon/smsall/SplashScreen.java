package com.hyperon.smsall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {
	Handler handler;
	Thread thread;
	int SPLASH_SCREEN_TIME_IN_MILLIS = 4000;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	  //Remove notification bar
	  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.logo_start_screen_relative_layout);
	    handler = new Handler();
	}

	@Override
	protected void onResume() {
	    super.onResume();
	    thread = new Thread() {
	        @Override
	        public void run() {
	            try {
	                Thread.sleep(SPLASH_SCREEN_TIME_IN_MILLIS);
	                handler.post(new Runnable() {
	                    public void run() {
	                        goToNextScreen();
	                    }
	                });
	            } catch (InterruptedException e) {
	            }
	        }
	    };

	    thread.start();
	}

	protected void goToNextScreen() {
	    Intent intent = new Intent(this, LoginActivity.class);
	    startActivity(intent);
	}

}

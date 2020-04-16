package com.kodeintel.sample.video.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;
import com.kodeintel.sample.video.BuildConfig;
import com.kodeintel.sample.video.R;

public class SplashActivity extends Activity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    TextView version = findViewById(R.id.version_splash);
    version.setText(BuildConfig.VERSION_NAME);
    moveToNext();
  }
  
  /**
   * Go to next page
   */
  private void moveToNext(){
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
      }
    }, 2000);
  }
  
  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    switch (event.getKeyCode()) {
      case KeyEvent.KEYCODE_SEARCH: {
        return true;
      }
      case KeyEvent.KEYCODE_BACK:
        if (event.getAction() == 0) {
          return true;
        }
      default:
        return super.dispatchKeyEvent(event);
    }
  }
}

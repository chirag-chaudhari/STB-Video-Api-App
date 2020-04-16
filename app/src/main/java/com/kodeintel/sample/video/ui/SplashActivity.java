package com.kodeintel.sample.video.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;

import com.kodeintel.sample.video.BuildConfig;
import com.kodeintel.sample.video.R;
import com.kodeintel.sample.video.data.FetchVideoService;

public class SplashActivity extends Activity {
    private int data = -1;
    private TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        version = findViewById(R.id.version_splash);
        version.setText(BuildConfig.VERSION_NAME);
        moveNextScreen();
    }

    private void moveNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent serviceIntent = new Intent(SplashActivity.this, FetchVideoService.class);
                serviceIntent.putExtra("sender","fromsplash");
                startService(serviceIntent);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 5000);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_SEARCH: {
                return true;
            }
            case KeyEvent.KEYCODE_BACK:
                if (event.getAction() == 0)
                    return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}

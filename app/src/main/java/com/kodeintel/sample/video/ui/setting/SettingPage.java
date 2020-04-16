package com.kodeintel.sample.video.ui.setting;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kodeintel.sample.video.BuildConfig;
import com.kodeintel.sample.video.R;
import com.kodeintel.sample.video.ui.SearchActivity;
import com.kodeintel.sample.video.ui.privacyPolicy.PrivacyPolicyActivity;
import com.kodeintel.sample.video.ui.termsCondition.TermsConditionActivity;


public class SettingPage extends Activity implements View.OnFocusChangeListener {
    int PERMISSIONS_REQUEST_RECORD_AUDIO = 101;
    private CardView ver;
    private CardView tnc;
    private CardView pp;
    private CardView faq;
    private ImageView ppiv;
    private ImageView tnciv;
    private ImageView faqiv;
    private TextView ver_text;
    private TextView tnc_text;
    private TextView pp_text;
    private TextView faq_text;
    private boolean GO_TO_SETTING = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ppiv = findViewById(R.id.ppiv);
        ppiv.setImageDrawable(getResources().getDrawable(R.drawable.ic_privacy_focus));
        tnciv = findViewById(R.id.tnciv);
        faqiv = findViewById(R.id.faqiv);

        ver = findViewById(R.id.version);
        tnc = findViewById(R.id.tnc);
        pp = findViewById(R.id.privacyPolicy);
        faq = findViewById(R.id.faq);
        ver_text = findViewById(R.id.version_text);
        tnc_text = findViewById(R.id.tnc_text);
        pp_text = findViewById(R.id.privacyPolicy_text);
        faq_text = findViewById(R.id.faq_text);
        ver_text.setText("Version: " + BuildConfig.VERSION_NAME);
        tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingPage.this, TermsConditionActivity.class));
            }
        });
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingPage.this, PrivacyPolicyActivity.class));
            }
        });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingPage.this, FAQActivity.class));
            }
        });
        pp.requestFocus();
        pp_text.setTextColor(this.getResources().getColor(R.color.button_un_select));
        tnc.setOnFocusChangeListener(this);
        ver.setOnFocusChangeListener(this);
        pp.setOnFocusChangeListener(this);
        faq.setOnFocusChangeListener(this);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.version:
                if (hasFocus) {
                    ver_text.setTextColor(this.getResources().getColor(R.color.button_un_select));
                } else {
                    ver_text.setTextColor(this.getResources().getColor(R.color.button_select));
                }
                break;
            case R.id.tnc:
                if (hasFocus) {
                    tnciv.setImageDrawable(getResources().getDrawable(R.drawable.ic_tnc_focused));
                    tnc_text.setTextColor(this.getResources().getColor(R.color.button_un_select));
                } else {
                    tnc_text.setTextColor(this.getResources().getColor(R.color.button_select));
                }
                break;
            case R.id.privacyPolicy:
                if (hasFocus) {
                    ppiv.setImageDrawable(getResources().getDrawable(R.drawable.ic_privacy_focus));
                    pp_text.setTextColor(this.getResources().getColor(R.color.button_un_select));
                } else {
                    pp_text.setTextColor(this.getResources().getColor(R.color.button_select));
                }
                break;
            case R.id.faq:
                if (hasFocus) {
                    faqiv.setImageDrawable(getResources().getDrawable(R.drawable.ic_faq_focus));
                    faq_text.setTextColor(this.getResources().getColor(R.color.button_un_select));
                } else {
                    faq_text.setTextColor(this.getResources().getColor(R.color.button_select));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_SEARCH: {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(this, SearchActivity.class));
                    } else {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                            GO_TO_SETTING = true;
                        }
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
                    }

                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    return true;
                }
            }
            default:
                return super.dispatchKeyEvent(event);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("TAGvv", "onRequestPermissionsResult: INTO");
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {

                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                    if (GO_TO_SETTING) {
                        permissionDialog("Permission Request", "Please allow audio permissions in the app settings.");
                    }
                }
                GO_TO_SETTING = false;
            }
        }
    }

    private void permissionDialog(String title, String desc) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


        alertDialogBuilder.setTitle(title)
                .setMessage(desc)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNeutralButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.show();
    }
}

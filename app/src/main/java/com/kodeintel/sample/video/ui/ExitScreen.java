package com.kodeintel.sample.video.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import com.kodeintel.sample.video.R;

import java.util.List;
import java.util.Objects;

public class ExitScreen extends FragmentActivity {

    private static final int CONTINUE = 0;
    private static final int BACK = 1;

    private static void addAction(
            Context context,
            List<GuidedAction> actions,
            long id,
            String title) {
        actions.add(new GuidedAction.Builder(context)
                .id(id)
                .title(title)
                .description(null)
                .build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == savedInstanceState) {
            GuidedStepSupportFragment.addAsRoot(this, new FirstStepFragment(), android.R.id.content);
        }
    }

    public static class FirstStepFragment extends GuidedStepSupportFragment {
        @Override
        public int onProvideTheme() {
            return R.style.Theme_Example_Leanback_GuidedStep_First;
        }


        @Override
        @NonNull
        public GuidanceStylist.Guidance onCreateGuidance(@NonNull Bundle savedInstanceState) {
            String title = getString(R.string.guidedstep_first_title);
            Drawable icon = null;
            return new GuidanceStylist.Guidance(title, null, null, icon);
        }

        @Override
        public void onCreateActions(@NonNull List<GuidedAction> actions,
                                    Bundle savedInstanceState) {
            addAction(getContext(),
                    actions,
                    CONTINUE,
                    getResources().getString(R.string.guidedstep_continue)
            );
            addAction(getContext(),
                    actions,
                    BACK,
                    getResources().getString(R.string.guidedstep_cancel)
            );
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            FragmentManager fm = getFragmentManager();
            if (action.getId() == CONTINUE) {
                ActivityCompat.finishAffinity(Objects.requireNonNull(getActivity()));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Objects.requireNonNull(getActivity()).finishAfterTransition();
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_SEARCH: {
                return true;
            }
            case KeyEvent.KEYCODE_BACK:
                if (event.getAction() == 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP                                                                                                                                                                                                                  ) {
                        this.finishAfterTransition();
                    }
                    return true;
                }
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}


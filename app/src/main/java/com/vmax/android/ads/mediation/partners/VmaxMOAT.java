package com.vmax.android.ads.mediation.partners;

import android.app.Application;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;

import com.moat.analytics.mobile.rel.MoatAdEvent;
import com.moat.analytics.mobile.rel.MoatAdEventType;
import com.moat.analytics.mobile.rel.MoatAnalytics;
import com.moat.analytics.mobile.rel.MoatFactory;
import com.moat.analytics.mobile.rel.MoatOptions;
import com.moat.analytics.mobile.rel.NativeDisplayTracker;
import com.moat.analytics.mobile.rel.NativeVideoTracker;
import com.moat.analytics.mobile.rel.TrackerListener;
import com.moat.analytics.mobile.rel.VideoTrackerListener;
import com.moat.analytics.mobile.rel.WebAdTracker;
import com.vmax.android.ads.util.Utility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * MOAT Version 2.4.2
 */
public class VmaxMOAT {
    private static MoatAnalytics moatAnalytics;
    MoatFactory factory;
    WebAdTracker webAdTracker;
    NativeVideoTracker nativeVideoTracker;
    NativeDisplayTracker nativeDisplayTracker;
    public VmaxMOAT(Application application){
        try {
            if (moatAnalytics == null) {
                moatAnalytics = MoatAnalytics.getInstance();
                MoatOptions options = new MoatOptions();
                options.loggingEnabled = true;
                moatAnalytics.start(options,application);
                Utility.showDebugLog("MoatAnalytics_vmax", "Created MOAT Application session");
            }
        }catch (Exception e){}
    }

    public void registerDisplayAd(WebView view){
        try {
            Utility.showDebugLog("MoatAnalytics_vmax", "Initializing MOAT Display Ad Session");
            factory = MoatFactory.create();
            if(factory!=null) {
                webAdTracker = factory.createWebAdTracker(view);
            }
            if(webAdTracker!=null){
                webAdTracker.setListener(new TrackerListener() {
                    @Override
                    public void onTrackingStarted(String s) {
                        Utility.showDebugLog("MoatAnalytics_vmax","MOAT Display: onTrackingStarted");
                    }

                    @Override
                    public void onTrackingFailedToStart(String s) {
                        Utility.showDebugLog("MoatAnalytics_vmax","MOAT Display: onTrackingFailedToStart");
                    }

                    @Override
                    public void onTrackingStopped(String s) {
                        Utility.showDebugLog("MoatAnalytics_vmax","MOAT Display: onTrackingStopped");
                    }
                });
            }
        }
        catch (Exception e){
            Utility.showDebugLog("MoatAnalytics_vmax","Exception for MOAT display ads");
        }
    }

    public void displayStartTracking(){
        if(webAdTracker!=null) {
            Utility.showDebugLog("MoatAnalytics_vmax","MOAT Display displayStartTracking");
            webAdTracker.startTracking();
        }
    }

    public void endDisplayAdSession(){
        if(webAdTracker!=null){
            Utility.showDebugLog("MoatAnalytics_vmax","Terminating MOAT Display Ad session");
            webAdTracker.stopTracking();
        }
        webAdTracker = null;
        factory = null;
    }

    public void startVastAdSession(String adSpotId, MediaPlayer mp, View playerView, String packageName, List<String> extensionResources, String moatHeaderValues){
        if(moatHeaderValues!=null && !TextUtils.isEmpty(moatHeaderValues)) {
            try {
                Utility.showDebugLog("MoatAnalytics_vmax", "Initializing MOAT VAST Ad Session");
                String vastPartnerCode = null;
                JSONObject headerObj = null;
                headerObj = new JSONObject(moatHeaderValues);
                if (headerObj.has("id")) {
                    vastPartnerCode = headerObj.optString("id");
                }
                factory = MoatFactory.create();
                if (factory != null) {
                    nativeVideoTracker = factory.createNativeVideoTracker(vastPartnerCode);
                }

                if (nativeVideoTracker != null) {
                    String viewableImpressions = "";
                    if (extensionResources != null) {
                        for (int i = 0; i < extensionResources.size(); i++) {
                            viewableImpressions += extensionResources.get(i) + ";";
                        }
                    }
                    HashMap<String, String> adIds = new HashMap();
                    if (headerObj.has("macros")) {
                        JSONObject macroObj = headerObj.optJSONObject("macros");
                        if (macroObj != null) {
                            if (macroObj.has("moatClientLevel1")) {
                                adIds.put("level1", macroObj.optString("moatClientLevel1"));
                            }
                            if (macroObj.has("moatClientLevel2")) {
                                adIds.put("level2", macroObj.optString("moatClientLevel2"));
                            }
                            if (macroObj.has("moatClientLevel3")) {
                                adIds.put("level3", macroObj.optString("moatClientLevel3"));
                            }
                            if (macroObj.has("moatClientLevel4")) {
                                adIds.put("level4", macroObj.optString("moatClientLevel4"));
                            }
                            if (macroObj.has("moatClientSlicer1")) {
                                adIds.put("slicer1", macroObj.optString("moatClientSlicer1"));
                            } else {
                                adIds.put("slicer1", packageName);
                            }
                            if (macroObj.has("moatClientSlicer2")) {
                                adIds.put("slicer2", macroObj.optString("moatClientSlicer2"));
                            } else {
                                adIds.put("slicer2", adSpotId);
                            }
                        }
                    }

                    if (!TextUtils.isEmpty(viewableImpressions)) {
                        adIds.put("zMoatVASTIDs", viewableImpressions);
                    }
                    Utility.showDebugLog("MoatAnalytics_vmax", "Data = " + adIds.toString());
                    nativeVideoTracker.setListener(new TrackerListener() {
                        @Override
                        public void onTrackingStarted(String s) {
                            Utility.showDebugLog("MoatAnalytics_vmax", "MOAT Vast: onTrackingStarted");
                        }

                        @Override
                        public void onTrackingFailedToStart(String s) {
                            Utility.showDebugLog("MoatAnalytics_vmax", "MOAT Vast: onTrackingFailedToStart");
                        }

                        @Override
                        public void onTrackingStopped(String s) {
                            Utility.showDebugLog("MoatAnalytics_vmax", "MOAT Vast: onTrackingStopped");
                            nativeVideoTracker = null;
                            factory = null;
                        }
                    });

                    nativeVideoTracker.setVideoListener(new VideoTrackerListener() {
                        @Override
                        public void onVideoEventReported(MoatAdEventType moatAdEventType) {
                            Utility.showDebugLog("MoatAnalytics_vmax", "MOAT Vast: onVideoEventReported");
                        }
                    });
                    nativeVideoTracker.trackVideoAd(adIds, mp, playerView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void recordVastEvent(final String event){
        Utility.showDebugLog("MoatAnalytics_vmax","Registering MOAT Vast event= "+event);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                MoatAdEvent moatEvent;
                if(nativeVideoTracker!=null){
                    switch (event){
                        case "start":
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_START);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        case "stop":
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_STOPPED);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        case "complete":
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_COMPLETE);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        case "firstQuartile":
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_FIRST_QUARTILE);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        case "midpoint":
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_MID_POINT);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        case "thirdQuartile":
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_THIRD_QUARTILE);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        case "pause":
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_PAUSED);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        case "expand":
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_ENTER_FULLSCREEN);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        case "collapse":
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_EXIT_FULLSCREEN);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        case "mute":
                            nativeVideoTracker.setPlayerVolume(MoatAdEvent.VOLUME_MUTED);
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_VOLUME_CHANGE);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        case "unmute":
                            nativeVideoTracker.setPlayerVolume(MoatAdEvent.VOLUME_UNMUTED);
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_VOLUME_CHANGE);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        case "skipped":
                            moatEvent = new MoatAdEvent(MoatAdEventType.AD_EVT_SKIPPED);
                            nativeVideoTracker.dispatchEvent(moatEvent);
                            break;
                        default:
                            Utility.showDebugLog("MoatAnalytics_vmax","No such event available for MOAT");
                            break;
                    }
                }
            }
        });
    }

    public void endVastAdSession(){
        if(nativeVideoTracker!=null){
            Utility.showDebugLog("MoatAnalytics_vmax","Terminating MOAT Vast Ad session");
            nativeVideoTracker.stopTracking();
        }
    }

    public void startNativeAdSession(View adView, String adSpotId, String packageName, String moatHeaderValues){
        if(moatHeaderValues!=null && !TextUtils.isEmpty(moatHeaderValues)){
            try{
                String nativePartnerCode = null;
                Utility.showDebugLog("MoatAnalytics_vmax", "Initializing MOAT NATIVE Ad Session");
                JSONObject headerObj = null;
                headerObj = new JSONObject(moatHeaderValues);
                if (headerObj.has("id")) {
                    nativePartnerCode = headerObj.optString("id");
                }
                if(moatAnalytics!=null){
                    moatAnalytics.prepareNativeDisplayTracking(nativePartnerCode);
                }
                factory = MoatFactory.create();
                if(factory!=null) {
                    HashMap<String, String> adIds = new HashMap();
                    if(headerObj.has("macros")){
                        JSONObject macroObj = headerObj.optJSONObject("macros");
                        if(macroObj!=null){
                            if(macroObj.has("moatClientLevel1")){
                                adIds.put("moatClientLevel1", macroObj.optString("moatClientLevel1"));
                            }
                            if(macroObj.has("moatClientLevel2")){
                                adIds.put("moatClientLevel2", macroObj.optString("moatClientLevel2"));
                            }
                            if(macroObj.has("moatClientLevel3")){
                                adIds.put("moatClientLevel3", macroObj.optString("moatClientLevel3"));
                            }
                            if(macroObj.has("moatClientLevel4")){
                                adIds.put("moatClientLevel4", macroObj.optString("moatClientLevel4"));
                            }
                            if(macroObj.has("moatClientSlicer1")){
                                adIds.put("moatClientSlicer1", macroObj.optString("moatClientSlicer1"));
                            }
                            else{
                                adIds.put("moatClientSlicer1", packageName);
                            }
                            if(macroObj.has("moatClientSlicer2")){
                                adIds.put("moatClientSlicer2", macroObj.optString("moatClientSlicer2"));
                            }
                            else{
                                adIds.put("moatClientSlicer2", adSpotId);
                            }
                        }
                    }
                    Utility.showDebugLog("MoatAnalytics_vmax", "Data = "+adIds.toString());
                    nativeDisplayTracker = factory.createNativeDisplayTracker(adView,adIds);
                }
                if(nativeDisplayTracker!=null){
                    nativeDisplayTracker.setListener(new TrackerListener() {
                        @Override
                        public void onTrackingStarted(String s) {
                            Utility.showDebugLog("MoatAnalytics_vmax","MOAT Native: onTrackingStarted");
                        }

                        @Override
                        public void onTrackingFailedToStart(String s) {
                            Utility.showDebugLog("MoatAnalytics_vmax","MOAT Native: onTrackingFailedToStart");
                        }

                        @Override
                        public void onTrackingStopped(String s) {
                            Utility.showDebugLog("MoatAnalytics_vmax","MOAT Native: onTrackingStopped");
                        }
                    });
                    nativeDisplayTracker.startTracking();
					 if(adView!=null){
                        adView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if(nativeDisplayTracker!=null) {
                                    Utility.showDebugLog("MoatAnalytics_vmax", "Registering MOAT Native Ad Touch");
                                    nativeDisplayTracker.reportUserInteractionEvent(NativeDisplayTracker.MoatUserInteractionType.TOUCH);
                                }
                                return false;
                            }
                        });
                    }
                }
            }catch (Exception e){}
        }
    }

    public void registerNativeAdClick(){
        if(nativeDisplayTracker!=null){
            Utility.showDebugLog("MoatAnalytics_vmax","Registering MOAT Native Ad Click");
            nativeDisplayTracker.reportUserInteractionEvent(NativeDisplayTracker.MoatUserInteractionType.CLICK);
        }
    }

    public void endNativeAdSession(){
        if(nativeDisplayTracker!=null){
            Utility.showDebugLog("MoatAnalytics_vmax","Terminating MOAT Native Ad session");
            nativeDisplayTracker.stopTracking();
        }
        nativeDisplayTracker = null;
        factory = null;
    }
}

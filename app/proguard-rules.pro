# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/google/home/cartland/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepattributes *Annotation*,JavascriptInterface,Exceptions,InnerClasses,Signature,*Annotation*,EnclosingMethod ,*Annotation*,Signature

-dontwarn com.google.** -dontwarn com.google.firebase.** -dontwarn com.google.android.gms.**

-keep public class com.android.volley.** {     <fields>;     <methods>; }
-dontwarn com.android.volley.**

-keep public class com.vmax.android.ads.util.UrlConstants {public ;public ;}
-keep public class com.vmax.android.ads.util.UrlConstants$** {public ;public ;}

-keep public class com.vmax.android.ads.api.VmaxAdView {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.api.NativeAd {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.api.AdContainer {     public <fields>;     public <methods>; }



-keep public class com.vmax.android.ads.api.VmaxSdk {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.api.ImageLoader {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.api.NativeImageDownload {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.api.NativeImageDownloadListener {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.api.VmaxAdPartner {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.common.VmaxAdListener {     <fields>;     <methods>; }

-keep public class com.vmax.android.ads.api.VmaxRequest {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.common.AdCustomizer {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.common.AdCustomizer$Builder {    <fields>;
   public <methods>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public class com.vmax.android.ads.exception.VmaxRequestError {     public <fields>;     public <methods>; }


-keep public class com.vmax.android.ads.exception.VmaxAdError {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.mediation.** {     public <fields>;     public <methods>;     }

-keep class com.vmax.android.ads.mediation.partners.** {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.nativeads.** {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.util.Utility {     <fields>;     <methods>; }

-keep public class com.vmax.android.ads.util.Constants {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.util.Constants$** {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.util.CountryAttributes {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.util.CountryNames {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.util.IntentUtils {     <fields>;     <methods>; }

-keep public class com.vmax.android.ads.common.VmaxRequestListener {     <fields>;     <methods>; }


 -keep public class com.google.android.gms.** {     <fields>;    <methods>; }

-keep public class com.google.ads.** {     public <fields>;     public <methods>; }

#Google IMA -keep class com.google.** { *; } -keep interface com.google.** { *; } -keep class com.google.ads.interactivemedia.v3.api.** { *;} -keep interface com.google.ads.interactivemedia.** { *; }

-dontwarn com.google.ads.interactivemedia.v3.api.**

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {     public static final *** NULL; }

# Keep names - Native method names. Keep all native class/method names. -keepclasseswithmembers class * {     native <methods>; }

-keep public enum  com.vmax.android.ads.api.VmaxAdView$AdState {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.VmaxAdView$MediaQuality{   <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.VmaxAdView$AdViewState {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.VmaxAdView$AdspotSize {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.AddOns$Environment {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.VmaxSdk$LogLevel {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.VmaxSdk$Gender {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.VmaxSdk$CacheMode {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.VmaxSdk$ContentVideoPlayer {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.VmaxSdk$ContentVideoHandler {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.VmaxSdk$ViewabilityPartner {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }
-keep public enum  com.vmax.android.ads.api.VmaxSdk$RequestType {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.VmaxSdk$UserAge {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public enum  com.vmax.android.ads.api.VmaxSdk$MediaType {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public class com.vmax.android.ads.api.Section {     public <fields>;     public <methods>; }

-keep public enum  com.vmax.android.ads.api.Section$** {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep public class com.vmax.android.ads.common.vast.VmaxTrackingEventInterface {     <fields>;     <methods>; }


-keep public class com.vmax.android.ads.api.VmaxAdEvent {     <fields>;     <methods>; }

-keep public class com.google.android.gms.location.FusedLocationProviderClient {     public <fields>;     public <methods>; }

 -keep public class com.google.android.gms.tasks.OnSuccessListener {     <fields>;     <methods>; }

-keep public class com.vmax.android.ads.common.DataReceiver {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.common.AppInstallReceiver {     public <fields>;     public <methods>; }

-keep public class com.vmax.android.ads.common.AdsSPCListener {     <fields>;     <methods>; }



#Chrome Custom Tab -keep public class android.support.customtabs.CustomTabsIntent {     public <fields>;     public <methods>; }

#GDPR Proguard -keep public class com.vmax.android.ads.common.RegionCheckListener {     <fields>;     <methods>; }

#MOAT Custom Plyer integration -keep public class com.vmax.android.ads.api.ViewabilityTracker {     public <fields>;     public <methods>; }

#OM Custom Plyer integration -keep public class com.iab.omid.library.vmax.** { *; } -dontwarn com.iab.omid.library.vmax.**

-keep public enum  com.vmax.android.ads.api.ViewabilityTracker$VOLUME_EVENTS {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }

-keep,allowshrinking @com.google.android.gms.common.annotation.KeepName class *


-keep public enum  com.vmax.android.ads.api.VmaxSdk$AdChoicePlacement {    <fields>;    public static **[] values();    public static ** valueOf(java.lang.String); }


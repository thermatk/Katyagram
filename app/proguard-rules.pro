# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/mvince/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepclassmembers public class * extends com.bluelinelabs.conductor.Controller {
   public <init>();
   public <init>(android.os.Bundle);
}

-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**

-dontwarn com.squareup.okhttp.**

-keep class cz.msebera.android.httpclient.** { *; }
-keep class com.loopj.android.http.** { *; }

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
-keep class android.support.v7.widget.RoundRectDrawable { *; }

-keep class com.mikepenz.iconics.** { *; }
-keep class com.mikepenz.community_material_typeface_library.CommunityMaterial
-keep class com.mikepenz.fontawesome_typeface_library.FontAwesome
-keep class com.mikepenz.google_material_typeface_library.GoogleMaterial
-keep class com.mikepenz.meteocons_typeface_library.Meteoconcs
-keep class com.mikepenz.octicons_typeface_library.Octicons

-dontwarn java.lang.invoke.*
-dontwarn sun.misc.Unsafe

-keepattributes Signature

-keep class android.support.v7.widget.RoundRectDrawable { *; }
-dontwarn android.support.v4.**
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# mars
-keep class com.tencent.mars.xlog.** { *; }
-keep class com.tencent.mars.comm.** { *; }
-keep class com.tencent.mars.app.** { *; }
-keep class com.tencent.mars.stn.** {*;}
-keep class com.tencent.mars.** { *; }
-keepclassmembers class com.tencent.mars.** { *; }
-dontwarn com.tencent.mars.**

# so
#-libraryjars libs/armeabi/libmarsxlog.so
#-libraryjars libs/armeabi/libc++_shared.so
-libraryjars libs/armeabi-v7a/libmarsxlog.so
-libraryjars libs/armeabi-v7a/libc++_shared.so
-libraryjars libs/arm64-v8a/libmarsxlog.so
-libraryjars libs/arm64-v8a/libc++_shared.so
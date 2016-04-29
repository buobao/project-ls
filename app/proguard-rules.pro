# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
###########
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#忽略警告 也可以用-ignorewarnings
-dontwarn
#声明第三方jar包,不用管第三方jar包中的.so文件(如果有)
#-libraryjars libs/baidumapapi.jar
#-libraryjars libs/tencent_openapi.jar
#-libraryjars libs/httpmime-4.1.3.jar
#-libraryjars libs/weibo.sdk.android.sso.jar
#-libraryjars libs/android-support-v4.jar
#-libraryjars libs/appcompat-v7.jar
##-libraryjars libs/gson.jar
#-libraryjars libs/android-async-http.jar
#-libraryjars libs/smack-android-extensions.jar
#-libraryjars libs/smack-tcp.jar
#-libraryjars libs/commons-codec.jar
#-libraryjars libs/core.jar
#-libraryjars libs/httpmime.jar
#-libraryjars libs/BaiduLBS_Android.jar
#-libraryjars libs/jpush-sdk-release.jar
#-libraryjars libs/libammsdk.jar
#-libraryjars libs/SocialSDK_email.jar
#-libraryjars libs/SocialSDK_QQZone_1.jar
#-libraryjars libs/SocialSDK_QQZone_2.jar
#-libraryjars libs/SocialSDK_QQZone_3.jar
#-libraryjars libs/SocialSDK_tencentWB_1.jar
#-libraryjars libs/SocialSDK_tencentWB_2.jar
#-libraryjars libs/SocialSDK_tencentWB_3.jar
#-libraryjars libs/sun.misc.BASE64Decoder.jar
#-libraryjars libs/umeng-update.jar
#-libraryjars libs/umeng_social_sdk.jar
#-libraryjars libs/universal-image-loader.jar
#-libraryjars libs/locSDK.jar
#-libraryjars libs/photo.jar
#不混淆第三方jar包中的类
-keep class com.example.mylibrary.** {*;}
-keep class com.umeng.socialize.** {*;}
#Note: duplicate definition of program class [com.umeng.socialize.media.QQShareContent$1]
#######################
#-keep class com.baidu.mapapi.** {*;}
#-keep class com.tencent.tauth.** {*;}
#-keep class org.apache.http.entity.mime.** {*;}
#-keep class android.support.v4.** {*;}
#-keep class android.support.v7.** {*;}
#-keep class android.net.http.** {*;}
#-keep class com.weibo.sdk.android.** {*;}
#-keep class com.sina.sso.** {*;}
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keepclasseswithmembernames class * {
native <methods>;
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}
#-keep class MyClass;
##############
#-optimizationpasses 5          # 指定代码的压缩级别
#-dontusemixedcaseclassnames   # 是否使用大小写混合
#-dontpreverify           # 混淆时是否做预校验
#-verbose                # 混淆时是否记录日志
#
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
#
#-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
#-keep public class * extends android.app.Application   # 保持哪些类不被混淆
#-keep public class * extends android.app.Service       # 保持哪些类不被混淆
#-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
#-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
#-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
#-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
#-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
#
#-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
#    native <methods>;
#}
#-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
#    public void *(android.view.View);
#}
#-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
#    public static final android.os.Parcelable$Creator *;
#}
#==========支付宝sdk==========
-dontwarn com.alipay.**
-dontwarn org.json.alipay.**
-dontwarn com.ta.utdid2.**
-dontwarn com.ut.device.**

-keep class com.alipay.**{*;}
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}


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

# adding this in to preserve line numbers so that the stack traces
# can be remapped
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
#==========支付宝sdk==========

#==========微信sdk==========
-dontwarn com.tencent.**
-keep class com.tencent.** { *; }
#==========微信sdk==========

#==========银联==========
-dontwarn com.unionpay.**
-keep class com.unionpay.**{*;}
-dontwarn cn.gov.**
-keep class cn.gov.**{*;}
-dontwarn org.simalliance.**
-keep class org.simalliance.**{*;}
#==========银联==========

# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
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
#指定代码的压缩级别
    -optimizationpasses 5

    #包明不混合大小写
    -dontusemixedcaseclassnames

    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses

     #优化  不优化输入的类文件
    -dontoptimize

     #预校验
    -dontpreverify

     #混淆时是否记录日志
    -verbose

     # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    #保护注解
    -keepattributes *Annotation*

    # 保持哪些类不被混淆
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService
    #如果有引用v4包可以添加下面这行
    -keep public class * extends android.support.v4.app.Fragment

    #忽略警告
    -ignorewarning

    ##记录生成的日志数据,gradle build时在本项目根目录输出##
    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt
    ########记录生成的日志数据，gradle build时 在本项目根目录输出-end######

    #####混淆保护自己项目的部分代码以及引用的第三方jar包library#######

#    -libraryjars libs/geognsslocationlib.jar

    #三星应用市场需要添加:sdk-v1.0.0.jar,look-v1.0.1.jar
    #-libraryjars libs/sdk-v1.0.0.jar
    #-libraryjars libs/look-v1.0.1.jar

    #项目特殊处理代码

    #忽略警告
    -dontwarn com.lippi.recorder.utils**
    #保留一个完整的包
    -keep class com.lippi.recorder.utils.** {
        *;
     }

    -keep class  com.lippi.recorder.utils.AudioRecorder{*;}


    #如果引用了v4或者v7包
    -dontwarn android.support.**

    ####-en混淆保护自己项目的部分代码以及引用的第三方jar包libraryd####

    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

    #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }

    #保持自定义控件类不被混淆
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    #保持自定义控件类不被混淆
    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable

    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

    #保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
    #-keepclassmembers enum * {
    #  public static **[] values();
    #  public static ** valueOf(java.lang.String);
    #}

    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    #避免混淆泛型 如果混淆报错建议关掉
    #–keepattributes Signature

    #移除log 测试了下没有用还是建议自己定义一个开关控制是否输出日志
    #-assumenosideeffects class android.util.Log {
    #    public static boolean isLoggable(java.lang.String, int);
    #    public static int v(...);
    #    public static int i(...);
    #    public static int w(...);
    #    public static int d(...);
    #    public static int e(...);
    #}

    #如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
    #gson
    #-libraryjars libs/gson-2.2.2.jar
    -keepattributes Signature
    # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    # Application classes that will be serialized/deserialized over Gson
    -keep class com.google.gson.** { *; }


    # 蓝牙崩溃问题解决方案
    -keep class com.geo.base.** { *; }
    -keep class com.geo.device.** { *; }
    -keep class com.geo.parse.** { *; }
    -keepclassmembers class ** {
        public void onEvent*(**);
    }

    #以下是高德地图混淆报错解决方案
        #libs文件夹下的第三方jar包，注意名称
        -dontwarn com.amap.api.**
        -dontwarn com.a.a.**
        -dontwarn com.aps.**
        -keep class com.amap.api.**  {*;}
        -keep class com.aps.**  {*;}
        -keep class com.a.a.**  {*;}
        -keep class com.autonavi.aps.amapapi.model.**{*;}

        #以下是Arcgis地图混淆报错解决方案
        -dontwarn com.esri.**
        -dontwarn org.achartengine.internal.**
        -keep class com.esri.** {*;}
        -keep class org.achartengine.internal.** {*;}



   -keep class butterknife.** { *; }
   -dontwarn butterknife.internal.**
   -keep class **$$ViewBinder { *; }

   -keepclasseswithmembernames class * {
       @butterknife.* <fields>;
   }

   -keepclasseswithmembernames class * {
       @butterknife.* <methods>;
   }

        -keep class com.uflycn.locationsystem.entity.**{*;}


        ################### region for xUtils
        -keepattributes Signature,*Annotation*
        -keep public class org.xutils.** {
            public protected *;
        }
        -keep public interface org.xutils.** {
            public protected *;
        }
        -keepclassmembers class * extends org.xutils.** {
            public protected *;
        }
        -keepclassmembers @org.xutils.db.annotation.* class * {*;}
        -keepclassmembers @org.xutils.http.annotation.* class * {*;}
        -keepclassmembers class * {
            @org.xutils.view.annotation.Event <methods>;
        }

        -dontwarn com.tencent.bugly.**
        -keep public class com.tencent.bugly.**{*;}

        ## 保持行号
        -keepattributes SourceFile,LineNumberTable




        -keep class org.greenrobot.greendao.**{*;}

                -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
                public static java.lang.String TABLENAME;
                }

                -keep class **$Properties
    -dontwarn sun.misc.**
    -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode producerNode;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode consumerNode;
    }
    -keep class com.uflycn.uoperation.bean.**{*;}

     ## EventBus
    -keepattributes *Annotation*
    -keepclassmembers class ** {
        @org.greenrobot.eventbus.Subscribe <methods>;
    }
    -keep enum org.greenrobot.eventbus.ThreadMode { *; }


    # Only required if you use AsyncExecutor
    -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
        <init>(java.lang.Throwable);
    }

    # 高德导航 混淆
    -keep class com.amap.api.navi.**{*;}
    -keep class com.autonavi.**{*;}
    #高德 搜索
    -keep   class com.amap.api.services.**{*;}

    -keep class com.chad.library.adapter.** {
    *;
    }
    -keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
    -keep public class * extends com.chad.library.adapter.base.BaseViewHolder
    -keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
         <init>(...);
    }
    # 混淆请求权限的 XXPermissions
    -dontwarn com.hjq.permissions.**

    -dontnote retrofit2.Platform
    -dontwarn retrofit2.Platform$Java8
    -keepattributes Signature
    -keepattributes Exceptions

    -dontwarn okhttp3.**
    -dontwarn okio.**
    -dontwarn javax.annotation.**

        -keep class com.baidu.tts.**{*;}
        -keep class com.baidu.speechsynthesizer.**{*;}
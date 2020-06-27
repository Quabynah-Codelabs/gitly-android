-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}
-keep class com.google.common.base.Preconditions { *; }
-keep class androidx.room.RoomDatabase { *; }
-keep class androidx.room.Room { *; }

-ignorewarnings

-keepattributes *Annotation*

-dontnote junit.framework.**
-dontnote junit.runner.**

-dontwarn androidx.test.**
-dontwarn org.junit.**
-dontwarn org.hamcrest.**
-dontwarn com.squareup.javawriter.JavaWriter

#-dontobfuscate
-dontwarn dev.gitly.**
-dontwarn in.uncod.android.bypass.**

-keep class dev.gitly.** { *; }
-keep class dev.gitly.core.billing.** { *; }
-keep class dev.gitly.core.prefs.** { *; }
-keep class dev.gitly.core.util.** { *; }
-keep class dev.gitly.core.worker.** { *; }
-keep class dev.gitly.model.** { *; }
-keep class dev.gitly.model.data.** { *; }
-keep class dev.gitly.model.repositories.** { *; }
-keep class dev.gitly.model.sources.local.** { *; }
-keep class dev.gitly.model.sources.local.daos.** { *; }
-keep class dev.gitly.model.sources.local.database.** { *; }
-keep class dev.gitly.model.sources.remote.** { *; }
-keep class dev.gitly.model.sources.remote.service.** { *; }
-keep class dev.gitly.view.** { *; }
-keep class dev.gitly.view.adapter.** { *; }
-keep class dev.gitly.view.auth.** { *; }
-keep class dev.gitly.view.home.** { *; }
-keep class dev.gitly.view.map.** { *; }
-keep class dev.gitly.view.search.** { *; }
-keep class dev.gitly.view.source.** { *; }
-keep class dev.gitly.view.user.** { *; }
-keep class dev.gitly.view.welcome.** { *; }
-keep class dev.gitly.view.widget.** { *; }
-keep class dev.gitly.view.widget.util.** { *; }
-keep class dev.gitly.view.widget.util.compat.** { *; }
-keep class dev.gitly.viewmodel.** { *; }
-keep class dev.gitly.** { *; }
-keep class dev.gitly.** { *; }
-keep class androidx.fragment.app.** { *; }
-keep class in.uncod.android.bypass.** { *; }

-keep class androidx.drawerlayout.widget.DrawerLayout { *; }

# Proguard rules that are applied to your test apk/code.
-ignorewarnings

-keep class androidx.appcompat.widget.** { *; }

# AndroidX
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Coroutines
# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembernames class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}
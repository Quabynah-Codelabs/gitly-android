package dev.gitly

import android.util.Log
import timber.log.Timber

fun debugger(message: Any?) {
    if (BuildConfig.DEBUG) Timber.tag("GITLY-DEBUGGER").d(message?.toString())
}

fun Any?.debugPrint() = debugger(this.toString())

class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }
        println(message)
    }
}


package dev.gitly.core.util

import android.annotation.SuppressLint
import android.os.Build
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * Local TextView compat functions that don't exist in TextView
 */
class LocalTextViewCompat private constructor() {
    companion object {
        @ColorInt
        fun getHighlightColor(textView: TextView): Int {
            return getHighlightColor(textView, android.R.color.transparent)
        }

        @SuppressLint("ObsoleteSdkInt")
        @ColorInt
        fun getHighlightColor(textView: TextView, @ColorRes defaultColorResId: Int): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.highlightColor
            } else {
                ContextCompat.getColor(textView.context, defaultColorResId)
            }
        }
    }

    init {
        throw AssertionError("No instances.")
    }
}
package dev.gitly.core.util

import `in`.uncod.android.bypass.style.TouchableUrlSpan
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.widget.TextView

/**
 * A movement method that only highlights any touched
 * [TouchableUrlSpan]s
 *
 *
 * Adapted from  http://stackoverflow.com/a/20905824
 */
class LinkTouchMovementMethod : LinkMovementMethod() {
    private var pressedSpan: TouchableUrlSpan? = null
    override fun onTouchEvent(
        textView: TextView,
        spannable: Spannable,
        event: MotionEvent
    ): Boolean {
        var handled = false
        if (event.action == MotionEvent.ACTION_DOWN) {
            pressedSpan = getPressedSpan(textView, spannable, event)
            if (pressedSpan != null) {
                pressedSpan?.setPressed(true)
                Selection.setSelection(
                    spannable, spannable.getSpanStart(pressedSpan),
                    spannable.getSpanEnd(pressedSpan)
                )
                handled = true
            }
        } else if (event.action == MotionEvent.ACTION_MOVE) {
            val touchedSpan: TouchableUrlSpan? = getPressedSpan(textView, spannable, event)
            if (pressedSpan != null && touchedSpan !== pressedSpan) {
                pressedSpan?.setPressed(false)
                pressedSpan = null
                Selection.removeSelection(spannable)
            }
        } else {
            if (pressedSpan != null) {
                pressedSpan?.setPressed(false)
                super.onTouchEvent(textView, spannable, event)
                handled = true
            }
            pressedSpan = null
            Selection.removeSelection(spannable)
        }
        return handled
    }

    private fun getPressedSpan(
        textView: TextView,
        spannable: Spannable,
        event: MotionEvent
    ): TouchableUrlSpan? {
        var x = event.x.toInt()
        var y = event.y.toInt()
        x -= textView.totalPaddingLeft
        y -= textView.totalPaddingTop
        x += textView.scrollX
        y += textView.scrollY
        val layout = textView.layout
        val line = layout.getLineForVertical(y)
        val off = layout.getOffsetForHorizontal(line, x.toFloat())
        val link: Array<TouchableUrlSpan> =
            spannable.getSpans(off, off, TouchableUrlSpan::class.java)
        var touchedSpan: TouchableUrlSpan? = null
        if (link.size > 0) {
            touchedSpan = link[0]
        }
        return touchedSpan
    }

    companion object {
        @JvmStatic
        var instance: LinkTouchMovementMethod? = null
            get() {
                if (field == null) field = LinkTouchMovementMethod()
                return field
            }
            private set
    }
}
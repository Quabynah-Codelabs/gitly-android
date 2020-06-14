package dev.gitly.core.util

import `in`.uncod.android.bypass.Bypass
import `in`.uncod.android.bypass.style.TouchableUrlSpan
import android.content.res.ColorStateList
import android.text.*
import android.text.style.URLSpan
import android.text.util.Linkify
import android.widget.TextView
import androidx.annotation.ColorInt
import dev.gitly.core.util.LinkTouchMovementMethod.Companion.instance

/**
 * Utility methods for working with HTML.
 */
object HtmlUtils {
    /**
     * Work around some 'features' of TextView and URLSpans. i.e. vanilla URLSpans do not react to
     * touch so we replace them with our own
     * Setting a custom MovementMethod on a TextView also alters touch handling (see
     * TextView#fixFocusableAndClickableSettings) so we need to correct this.
     */
    fun setTextWithNiceLinks(textView: TextView, input: CharSequence?) {
        textView.text = input
        textView.movementMethod = instance
        textView.isFocusable = false
        textView.isClickable = false
        textView.isLongClickable = false
    }

    /**
     * Parse the given input using [TouchableUrlSpan]s rather than vanilla [URLSpan]s
     * so that they respond to touch.
     */
    fun parseHtml(
        input: String?,
        linkTextColor: ColorStateList?,
        @ColorInt linkHighlightColor: Int
    ): SpannableStringBuilder {
        var spanned = Html.fromHtml(input) as SpannableStringBuilder

        // strip any trailing newlines
        while (spanned[spanned.length - 1] == '\n') {
            spanned = spanned.delete(spanned.length - 1, spanned.length)
        }
        val urlSpans = spanned.getSpans(0, spanned.length, URLSpan::class.java)
        for (urlSpan in urlSpans) {
            val start = spanned.getSpanStart(urlSpan)
            val end = spanned.getSpanEnd(urlSpan)
            spanned.removeSpan(urlSpan)
            spanned.setSpan(
                TouchableUrlSpan(
                    urlSpan.url, linkTextColor,
                    linkHighlightColor
                ), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spanned
    }

    fun parseAndSetText(textView: TextView, input: String?) {
        if (TextUtils.isEmpty(input)) return
        setTextWithNiceLinks(
            textView, parseHtml(
                input, textView.linkTextColors,
                LocalTextViewCompat.getHighlightColor(textView)
            )
        )
    }

    /**
     * Parse Markdown and plain-text links.
     *
     *
     * [Bypass] does not handle plain text links (i.e. not md syntax) and requires a
     * `String` input (i.e. squashes any spans). [Linkify] handles plain links but also
     * removes any existing spans. So we can't just run our input through both.
     *
     *
     * Instead we use the markdown lib, then take a copy of the output and Linkify
     * **that**. We then find any [URLSpan]s and add them to the markdown output.
     * Best of both worlds.
     */
    fun parseMarkdownAndPlainLinks(
        textView: TextView,
        input: String?,
        markdown: Bypass,
        loadImageCallback: Bypass.LoadImageCallback?
    ): CharSequence {
        var markedUp: CharSequence =
            markdown.markdownToSpannable(input, textView, loadImageCallback)
        val plainLinks = SpannableString(markedUp) // copy of the md output
        Linkify.addLinks(plainLinks, Linkify.WEB_URLS)
        val urlSpans = plainLinks.getSpans(0, plainLinks.length, URLSpan::class.java)
        if (urlSpans != null && urlSpans.size > 0) {
            // add any plain links to the markdown output
            val ssb = SpannableStringBuilder(markedUp)
            for (urlSpan in urlSpans) {
                ssb.setSpan(
                    TouchableUrlSpan(
                        urlSpan.url,
                        textView.linkTextColors,
                        LocalTextViewCompat.getHighlightColor(textView)
                    ),
                    plainLinks.getSpanStart(urlSpan),
                    plainLinks.getSpanEnd(urlSpan),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            markedUp = ssb
        }
        return markedUp
    }

    /**
     * Parse Markdown and plain-text links and set on the [TextView] with proper clickable
     * spans.
     */
    fun parseMarkdownAndSetText(
        textView: TextView,
        input: String?,
        markdown: Bypass,
        loadImageCallback: Bypass.LoadImageCallback?
    ) {
        if (TextUtils.isEmpty(input)) return
        setTextWithNiceLinks(
            textView,
            parseMarkdownAndPlainLinks(textView, input, markdown, loadImageCallback)
        )
    }
}
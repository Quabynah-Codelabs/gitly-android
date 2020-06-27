package dev.gitly.view.widget.transformers

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class DepthPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        when {
            position < -1 -> { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.alpha = 0f
            }
            position <= 0 -> { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.alpha = 1f
                view.translationX = 0f
                view.translationZ = 0f
                view.scaleX = 1f
                view.scaleY = 1f
            }
            position <= 1 -> { // (0,1]
                // Fade the page out.
                view.alpha = 1 - position

                // Counteract the default slide transition
                view.translationX = pageWidth * -position
                // Move it behind the left page
                view.translationZ = -1f

                // Scale the page down (between MIN_SCALE and 1)
                val scaleFactor = (MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position)))
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
            }
            else -> { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.alpha = 0f
            }
        }
    }

    companion object {
        private const val MIN_SCALE = 0.75f
    }
}

class DepthTransformation : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> {    // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.alpha = 0f
            }
            position <= 0 -> {    // [-1,0]
                page.alpha = 1f
                page.translationX = 0f
                page.scaleX = 1f
                page.scaleY = 1f
            }
            position <= 1 -> {    // (0,1]
                page.translationX = -position * page.width
                page.alpha = 1 - Math.abs(position)
                page.scaleX = 1 - Math.abs(position)
                page.scaleY = 1 - Math.abs(position)
            }
            else -> {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.alpha = 0f
            }
        }
    }
}

class FidgetSpinTransformation : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = -position * page.width
        if (Math.abs(position) < 0.5) {
            page.visibility = View.VISIBLE
            page.scaleX = 1 - Math.abs(position)
            page.scaleY = 1 - Math.abs(position)
        } else if (Math.abs(position) > 0.5) {
            page.visibility = View.GONE
        }
        when {
            position < -1 -> {     // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.alpha = 0f
            }
            position <= 0 -> {    // [-1,0]
                page.alpha = 1f
                page.rotation =
                    36000 * (Math.abs(position) * Math.abs(position) * Math.abs(position) * Math.abs(
                        position
                    ) * Math.abs(position) * Math.abs(position) * Math.abs(position))
            }
            position <= 1 -> {    // (0,1]
                page.alpha = 1f
                page.rotation =
                    -36000 * (Math.abs(position) * Math.abs(position) * Math.abs(position) * Math.abs(
                        position
                    ) * Math.abs(position) * Math.abs(position) * Math.abs(position))
            }
            else -> {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.alpha = 0f
            }
        }
    }
}

class PopTransformation : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = -position * page.width
        if (Math.abs(position) < 0.5) {
            page.visibility = View.VISIBLE
            page.scaleX = 1 - abs(position)
            page.scaleY = 1 - Math.abs(position)
        } else if (Math.abs(position) > 0.5) {
            page.visibility = View.GONE
        }
    }
}
package dev.gitly.core.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.Target
import dev.gitly.R

/**
 * Load avatar for user
 */
@BindingAdapter("app:url", "app:circleCrop", requireAll = false)
fun loadCircleAvatar(view: ImageView, url: String?, circleCrop: Boolean = false) {
    GlideApp.with(view.context)
        .asBitmap()
        .load(url)
        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
        .placeholder(R.drawable.avatar)
        .error(R.drawable.avatar)
        .apply {
            if (circleCrop) circleCrop()
        }
        .circleCrop()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .transition(withCrossFade())
        .into(view)
}
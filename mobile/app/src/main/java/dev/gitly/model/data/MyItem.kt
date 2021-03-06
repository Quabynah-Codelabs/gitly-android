package dev.gitly.model.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/**
 * A data class that implements the [ClusterItem] interface so it can be clustered.
 *
 *  Unfortunately Kotlin data class fields do not override Java interface methods by default
 *  (https://youtrack.jetbrains.com/issue/KT-6653?_ga=2.30406975.1494223917.1585591891-1137021041.1573759593)
 *  so we must name our fields differently and then pass them to the ClusterItem methods.
 */
data class MyItem(val latLng: LatLng, val myTitle: String?, val mySnippet: String?) : ClusterItem {
    override fun getPosition() = latLng
    override fun getTitle() = myTitle
    override fun getSnippet() = mySnippet
}
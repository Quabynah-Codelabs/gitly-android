package dev.gitly.model.data

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * Blog post data model
 */
@Entity(tableName = "posts")
@Parcelize
data class Post(
    @SerializedName("_id") val id: String,
    var title: String?,
    var description: String?,
    var rating: Double = 3.0,
    val authorId: String,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable
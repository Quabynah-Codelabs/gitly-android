package dev.gitly.model.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * User data model
 */
@Entity(tableName = "users")
@Parcelize
data class User(
    @PrimaryKey @field:SerializedName("_id") val id: String,
    val email: String,
    var name: String,
    var avatar: String? = null,
    var country: String? = null,
    var designation: String? = null,
    @field:SerializedName("createdOn") val timestamp: Long = System.currentTimeMillis()
) : Parcelable
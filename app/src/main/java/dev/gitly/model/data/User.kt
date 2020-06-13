package dev.gitly.model.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.sql.Date

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
    val timestamp: Date = Date(System.currentTimeMillis())
) : Parcelable
package dev.gitly.model.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "repos")
@Parcelize
data class Repo(
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("full_name") val fullName: String,
    @field:SerializedName("private") val isPrivate: Boolean = false,
    @field:SerializedName("description") val description: String? = null,
    @field:SerializedName("url") val url: String? = null,
    @field:SerializedName("html_url") val htmlUrl: String? = null
) : Parcelable
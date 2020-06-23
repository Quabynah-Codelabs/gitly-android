package dev.gitly.model.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "categories")
@Parcelize
data class Category(
    val name: String,
    @PrimaryKey
    val id: String = name.toLowerCase(Locale.getDefault())
        .replace(" ", "_")
        .replace("/", "_")
        .trim(),
    var isEnabled: Boolean = false,
    val icon: String? = null
) : Parcelable
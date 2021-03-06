package com.ducktapedapps.updoot.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@JsonClass(generateAdapter = true)
data class Subreddit(
        @PrimaryKey val display_name: String,
        val community_icon: String = "",
        val subscribers: Long = 0,
        val active_user_count: Long = 0,
        val public_description: String,
        val created: Long,
        var lastUpdated: Long?,
        val isTrending: Int = 0 //0 is not trending 1 is trending
) : Parcelable
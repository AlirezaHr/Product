package com.alirezahr.products.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson

class RatingConverter {
    @TypeConverter
    fun fromRating(rating: Rating): String = Gson().toJson(rating)

    @TypeConverter
    fun toRating(ratingString: String): Rating =
        Gson().fromJson(ratingString, Rating::class.java)
}
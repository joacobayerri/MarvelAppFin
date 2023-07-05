package ar.edu.uade.da2023.marvelapp.model

import com.google.gson.annotations.SerializedName

data class Character (
    val id: Int,
    val name: String,
    @SerializedName("thumbnail")
    val thumbnail: Thumbnail,
    val description: String,
    val series: ItemCount,
    val comics: ItemCount,
    val stories: ItemCount,
    val events: ItemCount


)

data class ItemCount(
    val available: Int
)

data class Thumbnail(
    val path: String,
    val extension: String
)
package ar.edu.uade.da2023.marvelapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ar.edu.uade.da2023.marvelapp.model.ItemCount
import ar.edu.uade.da2023.marvelapp.model.Thumbnail
import com.google.gson.annotations.SerializedName

@Entity(tableName = "characters")
data class CharacterLocal (
        @PrimaryKey val id: Int,
        val name: String,
        @SerializedName("thumbnail")
        val thumbnail_path: String,
        val thumbnail_ex: String,
        val description: String,
        val series: Int,
        val comics: Int,
        val stories: Int,
        val events: Int

)
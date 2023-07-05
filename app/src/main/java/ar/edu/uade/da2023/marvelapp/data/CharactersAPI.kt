package ar.edu.uade.da2023.marvelapp.data

import ar.edu.uade.da2023.marvelapp.model.Character
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class MarvelResponse<T>(
    val code: Int,
    val status: String,
    val data: MarvelData<T>
)

data class MarvelData<T>(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<T>
)

interface CharactersAPI {
    @GET("/v1/public/characters")
    fun getCharacters(
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("apikey") apiKey: String,
        @Query("ts") timestamp: Long,
        @Query("hash") hash: String,
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Call<MarvelResponse<Character>>

    @GET("/v1/public/characters/{characterId}")
    fun getCharacterById(
        @Path("characterId") characterId: Int,
        @Query("apikey") apiKey: String,
        @Query("ts") timestamp: Long,
        @Query("hash") hash: String
    ): Call<MarvelResponse<Character>>
}


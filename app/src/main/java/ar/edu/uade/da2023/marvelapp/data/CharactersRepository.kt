package ar.edu.uade.da2023.marvelapp.data

import CharactersDataSource
import android.content.Context
import ar.edu.uade.da2023.marvelapp.model.Character

class CharactersRepository {

    private val charactersDS = CharactersDataSource()

    suspend fun getCharacters(nameStartsWith: String, limit: Int?, offset: Int?) : List<Character> {
        return charactersDS.getCharacters(nameStartsWith, limit, offset)
    }

    suspend fun getCharacterById(characterId: Int, context: Context): Character? {
        return charactersDS.getCharacterById(characterId, context)
    }

    suspend fun getCharacterByIdf(characterId: Int): Character? {
        return charactersDS.getCharacterByIdf(characterId)
    }
}
package ar.edu.uade.da2023.marvelapp.ui

import FavoritesRepository
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.uade.da2023.marvelapp.model.Character
import ar.edu.uade.da2023.marvelapp.data.CharactersRepository
import kotlinx.coroutines.*

class CharacterDetailViewModel : ViewModel() {
    private val charactersRepository = CharactersRepository()
    private val favoritesRepository = FavoritesRepository()

    val characterLiveData: MutableLiveData<Character> = MutableLiveData()
    val isFavoriteLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var character: Character

    fun fetchCharacterDetails(characterId: Int, context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            // Realizar la llamada a la API en un contexto de corrutina
            try {
                character = withContext(Dispatchers.IO) {
                    charactersRepository.getCharacterById(characterId, context)!!
                }
                characterLiveData.postValue(character)
            } catch (e: Exception) {
                // Manejar cualquier error en la llamada a la API
                e.printStackTrace()
            }
        }
    }

    fun toggleFavoriteCharacter(email: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val isFavorite = runBlocking(Dispatchers.IO) {
                favoritesRepository.isFavoriteCharacter(email, character)
            }
            if (isFavorite) {
                favoritesRepository.removeFavoriteCharacter(email, character.id)
                isFavoriteLiveData.postValue(false)
            } else {
                favoritesRepository.addFavoriteCharacter(email, character)
                isFavoriteLiveData.postValue(true)
            }
        }
    }
}
package ar.edu.uade.da2023.marvelapp.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.uade.da2023.marvelapp.data.CharactersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel() {
    //Constantes
    private val _TAG = "API-DEMO"


    //Dependencias
    private val charactersRepo = CharactersRepository()

    //propiedades
    var characters = MutableLiveData<List<ar.edu.uade.da2023.marvelapp.model.Character>>()
    var nameStartsWith = ""
    private val coroutineContext: CoroutineContext = newSingleThreadContext("uadedemo")
    private val scope = CoroutineScope(coroutineContext)

    //Funciones
    fun onStart() {
        // Cargar los datos de los personajes
        scope.launch {
            kotlin.runCatching {
                charactersRepo.getCharacters(nameStartsWith,100,0)

            }.onSuccess {
                Log.d(_TAG, "Characters on success")
                characters.postValue(it)
            }.onFailure {
                Log.e(_TAG,"Characters error: "+ it)
            }
        }
    }

    fun updateNameStartsWith(nameStartsWith: String) {
        this.nameStartsWith = nameStartsWith
    }
}
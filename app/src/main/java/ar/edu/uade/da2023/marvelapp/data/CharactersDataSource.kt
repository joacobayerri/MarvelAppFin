import android.content.Context
import android.util.Log
import ar.edu.uade.da2023.marvelapp.data.CharactersAPI
import ar.edu.uade.da2023.marvelapp.data.local.AppDatabase
import ar.edu.uade.da2023.marvelapp.data.local.toExternal
import ar.edu.uade.da2023.marvelapp.data.local.toLocal
import ar.edu.uade.da2023.marvelapp.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest

class CharactersDataSource {
    private val BASE_URL = "https://gateway.marvel.com"
    private val PUBLIC_KEY = "3541d7c37268dd9134a95420ad895e89"
    private val PRIVATE_KEY = "f320c1817019ce9cd6a4f5e75be19288c89d1975"
    private val _TAG = "API-DEMO"

    private fun getHash(timestamp: Long): String {
        val input = "$timestamp$PRIVATE_KEY$PUBLIC_KEY"
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        val hashText = BigInteger(1, digest).toString(16)
        return hashText.padStart(32, '0')
    }

    suspend fun getCharacters(nameStartsWith: String, limit: Int?, offset: Int?): List<Character> {
        val timestamp = System.currentTimeMillis()
        val hash = getHash(timestamp)

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CharactersAPI::class.java)

        val call = api.getCharacters(nameStartsWith, PUBLIC_KEY, timestamp, hash, limit, offset)
        val response = call.execute()

        return if (response.isSuccessful) {
            Log.d(_TAG,"Resultado exitoso")
            response.body()?.data?.results ?: emptyList()
        } else {
            Log.e(_TAG,"No funca")
            emptyList()
        }
    }

    suspend fun getCharacterById(characterId: Int, context: Context): Character? {
        return withContext(Dispatchers.IO) {
            val timestamp = System.currentTimeMillis()
            val hash = getHash(timestamp)

            Log.d(_TAG, "Busco c local")
            var cLocal = AppDatabase.getInstance(context).charactersDao().getById(characterId)

            if (cLocal != null){
                Log.d(_TAG, "Devuelve c local")
                return@withContext cLocal.toExternal()
            }

            val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CharactersAPI::class.java)

            val call = api.getCharacterById(characterId, PUBLIC_KEY, timestamp, hash)
            val response = call.execute()

            return@withContext if (response.isSuccessful) {
                Log.d(_TAG, "Resultado exitoso")
                var resp = response.body()?.data?.results?.firstOrNull()


                if (resp != null) {
                    Log.d(_TAG, "Guardo lsita local")
                    var charLocal = resp.toLocal()
                    AppDatabase.getInstance(context).charactersDao().insert(charLocal)
                }

                resp
            } else {
                Log.e(_TAG, "Error al obtener el personaje por ID")
                null
            }
        }
    }

    suspend fun getCharacterByIdf(characterId: Int): Character? {
        return withContext(Dispatchers.IO) {
            val timestamp = System.currentTimeMillis()
            val hash = getHash(timestamp)

            val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CharactersAPI::class.java)

            val call = api.getCharacterById(characterId, PUBLIC_KEY, timestamp, hash)
            val response = call.execute()

            return@withContext if (response.isSuccessful) {
                Log.d(_TAG, "Resultado exitoso")
                response.body()?.data?.results?.firstOrNull()
            } else {
                Log.e(_TAG, "Error al obtener el personaje por ID")
                null
            }
        }
    }


}
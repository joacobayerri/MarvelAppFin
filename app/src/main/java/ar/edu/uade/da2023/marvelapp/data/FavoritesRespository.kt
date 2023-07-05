import com.google.firebase.firestore.FirebaseFirestore
import ar.edu.uade.da2023.marvelapp.model.Character
import kotlinx.coroutines.tasks.await

class FavoritesRepository {

    private val db = FirebaseFirestore.getInstance()

    fun addFavoriteCharacter(userId: String, character: Character) {
        val userFavoritesRef = db.collection("favorites").document(userId).collection("characters")
        val characterData = hashMapOf(
            "id" to character.id,
            "name" to character.name,
            "description" to character.description,
            "thumbnail" to hashMapOf(
                "path" to character.thumbnail.path,
                "extension" to character.thumbnail.extension
            )

        )

        userFavoritesRef.document(character.id.toString()).set(characterData)
            .addOnSuccessListener {
                // El personaje favorito se guardó exitosamente en Firestore
            }
            .addOnFailureListener { e ->
                // Error al guardar el personaje favorito en Firestore
                // Maneja el error según tus necesidades
            }
    }

    fun removeFavoriteCharacter(userId: String, characterId: Int) {
        val userFavoritesRef = db.collection("favorites").document(userId).collection("characters")

        userFavoritesRef.document(characterId.toString()).delete()
            .addOnSuccessListener {
                // El personaje favorito se eliminó exitosamente de Firestore
            }
            .addOnFailureListener { e ->
                // Error al eliminar el personaje favorito de Firestore
                // Maneja el error según tus necesidades
            }
    }

    suspend fun getFavoriteCharacters(userId: String): List<Character> {
        val userFavoritesRef = db.collection("favorites").document(userId).collection("characters")

        val querySnapshot = userFavoritesRef.get().await()
        val characters = mutableListOf<Character>()

        for (document in querySnapshot) {
            val character = document.toObject(Character::class.java)
            characters.add(character)
        }

        return characters
    }

    suspend fun isFavoriteCharacter(userId: String, character: Character): Boolean {
        val userFavoritesRef = db.collection("favorites").document(userId).collection("characters")

        val querySnapshot = userFavoritesRef.whereEqualTo("id", character.id).get().await()
        return !querySnapshot.isEmpty
    }
}
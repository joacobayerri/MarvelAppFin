package ar.edu.uade.da2023.marvelapp.ui

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesViewModel : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val userEmail: String? = currentUser?.email

    fun loadFavoritesFromFirestore(
        onSuccess: (List<String>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (userEmail != null) {
            firestore.collection("favorites")
                .document(userEmail)
                .collection("characters")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val favoritesList = mutableListOf<String>()
                    for (document in querySnapshot) {
                        favoritesList.add(document.id)
                    }
                    onSuccess(favoritesList)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } else {
            // Manejar el caso en que no se pueda obtener el correo electrónico del usuario actual
        }
    }
}
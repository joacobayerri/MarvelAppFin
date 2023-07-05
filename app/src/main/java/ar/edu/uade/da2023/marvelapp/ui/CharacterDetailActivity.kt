package ar.edu.uade.da2023.marvelapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import ar.edu.uade.da2023.marvelapp.R
import ar.edu.uade.da2023.marvelapp.model.Character
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CharacterDetailViewModel

    private lateinit var characterImage: ImageView
    private lateinit var characterName: TextView
    private lateinit var characterDescription: TextView
    private lateinit var comics: TextView
    private lateinit var series: TextView
    private lateinit var historias: TextView
    private lateinit var eventos: TextView
    private lateinit var favoriteButton: ImageButton

    private lateinit var progressBar: ProgressBar
    private lateinit var bgBar: View
    private var handler = android.os.Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)



        characterImage = findViewById(R.id.characterImage)
        characterName = findViewById(R.id.characterName)
        characterDescription = findViewById(R.id.characterDescription)
        comics = findViewById(R.id.lblInfo1)
        series = findViewById(R.id.lblInfo2)
        historias = findViewById(R.id.lblInfo3)
        eventos = findViewById(R.id.lblInfo4)
        favoriteButton = findViewById(R.id.favoriteButton)

        viewModel = ViewModelProvider(this).get(CharacterDetailViewModel::class.java)

        val characterId = intent.getIntExtra("characterId", -1)
        val characterName = intent.getStringExtra("characterName") ?: ""

        progressBar = findViewById(R.id.progressBar3)
        bgBar = findViewById(R.id.backgroundPBar2)


        progressBar.visibility = View.VISIBLE



        // Observar los cambios en el LiveData del personaje
        viewModel.characterLiveData.observe(this) { character ->
            if (character != null) {
                displayCharacterDetails(character)
            }
        }

        // Observar los cambios en el LiveData de favorito
        viewModel.isFavoriteLiveData.observe(this) { isFavorite ->
            val starDrawable = if (isFavorite) R.drawable.star_filled else R.drawable.ic_star_border
            favoriteButton.setImageResource(starDrawable)
        }

        // Obtener los detalles del personaje
        viewModel.fetchCharacterDetails(characterId,this)

        // Obtener el email del usuario actual
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        // Verifica si el personaje es favorito consultando Firestore
        if (email != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("favorites").document(email).collection("characters")
                .document(characterId.toString())
                .get()
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot? ->
                    val isFavorite = documentSnapshot?.exists() == true
                    viewModel.isFavoriteLiveData.value = isFavorite

                    runOnUiThread {
                        // Establece el icono de favoritos
                        val starDrawable = if (isFavorite) R.drawable.star_filled else R.drawable.ic_star_border
                        favoriteButton.setImageResource(starDrawable)
                    }
                }
                .addOnFailureListener { e ->
                    // Error al obtener el documento
                }
        }

        // Configura el clic del botón de favoritos
        favoriteButton.setOnClickListener {
            if (email != null) {
                viewModel.toggleFavoriteCharacter(email)
            }
            //favoriteButton.isEnabled = false // Deshabilita el botón temporalmente para evitar múltiples clics
        }
    }

    private fun displayCharacterDetails(character: Character) {
        characterName.text = character.name
        characterDescription.text = character.description
        comics.text = "Comics: " + character.comics.available.toString()
        series.text = "Series: " + character.series.available.toString()
        historias.text = "Historias: " + character.stories.available.toString()
        eventos.text = "Eventos: " + character.events.available.toString()

        val imageUrl = character.thumbnail?.let { "${it.path}.${it.extension}" }
        Glide.with(this)
            .load(imageUrl)
            .circleCrop()
            .into(characterImage)

        handler.postDelayed({
            progressBar.visibility = View.GONE
            bgBar.visibility = View.GONE
        },100)
    }
}
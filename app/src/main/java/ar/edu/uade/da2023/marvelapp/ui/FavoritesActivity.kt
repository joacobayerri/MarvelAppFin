package ar.edu.uade.da2023.marvelapp.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.ProgressBar

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.da2023.marvelapp.R
import java.util.logging.Handler

class FavoritesActivity : AppCompatActivity() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var favoritesViewModel: FavoritesViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var bgBar: View
    private var handler = android.os.Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)


        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        rvFavorites = findViewById(R.id.rvFavorites)
        rvFavorites.layoutManager = LinearLayoutManager(this)
        favoritesAdapter = FavoritesAdapter()
        rvFavorites.adapter = favoritesAdapter
        progressBar = findViewById(R.id.progressBar2)
        bgBar = findViewById(R.id.backgroundPBar)


        progressBar.visibility = View.VISIBLE
        loadFavoritesFromFirestore()
    }


    private fun loadFavoritesFromFirestore() {
        favoritesViewModel.loadFavoritesFromFirestore(
            onSuccess = { favoritesList ->
                // Actualizar el adaptador con la lista de IDs de personajes

                favoritesAdapter.setData(favoritesList)
                handler.postDelayed({
                    progressBar.visibility = View.GONE
                    bgBar.visibility = View.GONE
                }, 2000)
            },
            onFailure = { exception ->
                // Manejar el error en caso de fallo en la obtención de los favoritos
                // Por ejemplo, mostrar un mensaje de error o realizar alguna acción de recuperación
                handler.postDelayed({
                    progressBar.visibility = View.GONE
                }, 2000)
            }
        )
    }
}

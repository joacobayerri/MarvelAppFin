package ar.edu.uade.da2023.marvelapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.da2023.marvelapp.R
import ar.edu.uade.da2023.marvelapp.data.CharactersRepository
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoritesAdapter :
    RecyclerView.Adapter<FavoritesAdapter.CharacterViewHolder>() {

    private var charactersList: List<String> = emptyList()
    private val charactersRepository = CharactersRepository()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val characterId = charactersList[position]
        holder.bind(characterId)
    }

    override fun getItemCount(): Int = charactersList.size

    fun setData(charactersList: List<String>) {
        this.charactersList = charactersList
        notifyDataSetChanged()
    }

    inner class CharacterViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvFavoriteName)

        fun bind(characterId: String) {
            // Obtener información del personaje a partir de su ID
            GlobalScope.launch(Dispatchers.Main) {
                val character = charactersRepository.getCharacterByIdf(characterId.toInt())

                if (character != null) {
                    // Mostrar el nombre del personaje en el TextView
                    tvTitle.text = character.name

                    // Mostrar la imagen del personaje utilizando Glide
                    val imageUrl = character.thumbnail?.let { "${it.path}.${it.extension}" }
                    Glide.with(itemView)
                        .load(imageUrl)
                        .circleCrop()
                        .into(itemView.findViewById(R.id.imageCharacterFav))
                } else {
                    // Manejar el caso en el que no se pudo obtener la información del personaje
                    tvTitle.text = "Error al cargar el personaje"
                }
            }
        }
    }
}
package ar.edu.uade.da2023.marvelapp.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.da2023.marvelapp.R
import ar.edu.uade.da2023.marvelapp.model.Character

class FavoritesViewHolder(itemView: View, private val onItemClickListener: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

    private val characterNameTextView: TextView = itemView.findViewById(R.id.tvFavoriteName)
    val img: ImageView = itemView.findViewById(R.id.imageCharacterFav)

    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener(position)
            }
        }
    }

    fun bind(character: Character) {
        characterNameTextView.text = character.name
    }
}
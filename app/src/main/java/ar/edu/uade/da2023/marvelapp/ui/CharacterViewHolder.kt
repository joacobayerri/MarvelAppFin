package ar.edu.uade.da2023.marvelapp.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.da2023.marvelapp.R

class CharacterViewHolder(itemView: View, private val onItemClickListener: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.lblName)
    val img: ImageView = itemView.findViewById(R.id.imageCharacter)

    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener(position)
            }
        }
    }
}
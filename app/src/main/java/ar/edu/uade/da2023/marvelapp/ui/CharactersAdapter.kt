package ar.edu.uade.da2023.marvelapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.da2023.marvelapp.R
import com.bumptech.glide.Glide

class CharactersAdapter(private val onItemClick: (position: Int) -> Unit) : RecyclerView.Adapter<CharacterViewHolder>() {

    var items: MutableList<ar.edu.uade.da2023.marvelapp.model.Character> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.character_item, parent, false)
        val viewHolder = CharacterViewHolder(view, onItemClick)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = items[position]
        holder.name.text = character.name

        val imageUrl = character.thumbnail?.let { "${it.path}.${it.extension}" }
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .circleCrop()
            .into(holder.img)
    }

    fun updateList(newList: List<ar.edu.uade.da2023.marvelapp.model.Character>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): ar.edu.uade.da2023.marvelapp.model.Character {
        return items[position]
    }
}

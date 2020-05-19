package com.summertaker.fruits2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flaviofaria.kenburnsview.KenBurnsView
import com.squareup.picasso.Picasso

class PagerRecyclerAdapter(private val list: ArrayList<Fruit>) :
    RecyclerView.Adapter<PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
        PagerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_container_location, parent, false)
        )

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}

class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val kbvLocation: KenBurnsView = itemView.findViewById(R.id.kbvLocation)
    private val textView: TextView = itemView.findViewById(R.id.textLocation)

    fun bind(fruit: Fruit, position: Int) {
        //Glide.with(context).load(fruit.image).placeholder(R.drawable.placeholder).into(kbvLocation);
        Picasso.get().load(fruit.image).into(kbvLocation);
        textView.text = fruit.name
        //itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, bgColor))
    }
}
package com.summertaker.fruits2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flaviofaria.kenburnsview.KenBurnsView
import com.squareup.picasso.Picasso

class PagerRecyclerAdapter(private val list: ArrayList<Fruit>) :
    RecyclerView.Adapter<PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder =
        PagerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cardview, parent, false)
        )

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}

class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val kbvVp2Picture: KenBurnsView = itemView.findViewById(R.id.kbvVp2Picture)
    private val tvVp2Group: TextView = itemView.findViewById(R.id.tvVp2Group)
    private val tvVp2Team: TextView = itemView.findViewById(R.id.tvVp2Team)

    fun bind(fruit: Fruit, position: Int) {
        //itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, bgColor))
        //Glide.with(context).load(fruit.image).placeholder(R.drawable.placeholder).into(kbvLocation);
        Picasso.get().load(fruit.image).into(kbvVp2Picture);
        tvVp2Group.text = fruit.group
        tvVp2Team.text = fruit.team
    }
}
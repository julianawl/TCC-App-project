package com.julianawl.testemoov.ui.dialog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.julianawl.testemoov.data.ActorName

class GetActorsAdapter(
    private val actors: MutableList<ActorName> = mutableListOf(),
    var onActorClickListener: (actor: ActorName) -> Unit = {}
) : RecyclerView.Adapter<GetActorsAdapter.GetActorsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetActorsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
        return GetActorsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GetActorsViewHolder, position: Int) {
        holder.bind(actors[position])
    }

    override fun getItemCount(): Int = actors.size

    fun append(actors: List<ActorName>) {
        this.actors.clear()
        this.actors.addAll(actors)
        notifyDataSetChanged()
    }

    inner class GetActorsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val actorName: TextView = itemView.findViewById(android.R.id.text1)
        fun bind(actor: ActorName) {
            actorName.text = actor.name
            initActions(actor)
        }

        private fun initActions(actor: ActorName) {
            actorName.setOnClickListener {
                onActorClickListener(actor)
            }
        }
    }
}
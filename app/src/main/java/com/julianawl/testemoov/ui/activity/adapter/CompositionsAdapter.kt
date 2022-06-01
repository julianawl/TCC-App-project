package com.julianawl.testemoov.ui.activity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.julianawl.testemoov.R
import com.julianawl.framework.model.SetModel

class CompositionsAdapter(
    var onCompositionClickListener: (composition: SetModel) -> Unit = {}
) : RecyclerView.Adapter<CompositionsAdapter.CompositionViewHolder>() {

    private val compositionList: ArrayList<SetModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompositionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.composition_item, parent, false)
        return CompositionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompositionViewHolder, position: Int) {
        holder.bind(compositionList[position])
    }

    override fun getItemCount(): Int = compositionList.size

    fun update(compositions: List<SetModel>){
        compositionList.clear()
        compositionList.addAll(compositions)
    }

    inner class CompositionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var composition: SetModel
        private val item = view.findViewById<MaterialCardView>(R.id.comp_item_cv)
        private val compositionName = view.findViewById<MaterialTextView>(R.id.comp_item_title_tv)
        private val scenesCount = view.findViewById<MaterialTextView>(R.id.comp_item_frames_count_tv)
        fun bind(composition: SetModel){
            this.composition = composition
            compositionName.text = composition.name
            scenesCount.text = "Cenas: ${composition.scenes?.last()?.id}"

            initActions(composition)
        }

        private fun initActions(composition: SetModel){
            item.setOnClickListener {
                onCompositionClickListener(composition)
            }
        }
    }
}
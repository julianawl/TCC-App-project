package com.julianawl.testemoov.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.julianawl.testemoov.data.ActorName
import com.julianawl.testemoov.databinding.GetActorsDialogBinding
import com.julianawl.testemoov.ui.dialog.adapter.GetActorsAdapter

class GetActorsDialog(private val actors: List<ActorName>) : DialogFragment() {
    private val adapter by lazy { GetActorsAdapter() }
    private val binding by lazy { GetActorsDialogBinding.inflate(layoutInflater) }
    var onItemClickListener: (actor: ActorName) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setView(binding.root)

        binding.dialogGetActorsRv.adapter = adapter
        binding.dialogGetActorsRv.layoutManager = LinearLayoutManager(context)
        adapter.append(actors)

        adapter.onActorClickListener = { actor ->
            onItemClickListener(actor)
        }

        binding.dialogGetActorsCloseBtn.setOnClickListener { dismiss() }
        return dialog.create()
    }
}
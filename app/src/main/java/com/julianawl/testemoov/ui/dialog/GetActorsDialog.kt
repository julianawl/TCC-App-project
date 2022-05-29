package com.julianawl.testemoov.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.julianawl.testemoov.graphics.model.ActorName
import com.julianawl.testemoov.databinding.GetActorsDialogBinding
import com.julianawl.testemoov.ui.dialog.adapter.GetActorsAdapter

class GetActorsDialog(private val actors: List<ActorName>) : DialogFragment() {
    private val adapter by lazy { GetActorsAdapter() }
    private val binding by lazy { GetActorsDialogBinding.inflate(layoutInflater) }
    var onItemClickListener: (actor: ActorName) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
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
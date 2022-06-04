package com.julianawl.testemoov.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.julianawl.testemoov.R
import com.julianawl.testemoov.databinding.NewCompositionDialogBinding

class NewCompositionDialog : DialogFragment() {

    private val binding by lazy {
        NewCompositionDialogBinding.inflate(layoutInflater)
    }

    private val closeBtn by lazy { binding.dialogNewCompCloseBtn }
    private val createBtn by lazy { binding.dialogNewCompCreateBtn }
    private val widthEditText by lazy { binding.dialogNewCompWidthEt }
    private val heightEditText by lazy { binding.dialogNewCompHeightEt }
    private val nameEditText by lazy { binding.dialogNewCompNameEt }

    private var listener: NewCompositionListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setView(binding.root)

        this.createBtn.setOnClickListener {
            when {
                this.widthEditText.text.toString().isEmpty() && this.heightEditText.text.toString()
                    .isEmpty() && this.nameEditText.text.toString().isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_new_comp_required_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                this.widthEditText.text.toString().isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_new_comp_width),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                this.heightEditText.text.toString().isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_new_comp_height),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                this.nameEditText.text.toString().isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_new_comp_name_required),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    this.listener?.onClickCreateComposition(
                        this.widthEditText.text.toString().toFloat(),
                        this.heightEditText.text.toString().toFloat(),
                        this.nameEditText.text.toString()
                    )
                    dismiss()
                }
            }
        }

        this.closeBtn.setOnClickListener { dismiss() }
        return dialog.create()
    }

    fun newCompositionListener(dialogListener: NewCompositionListener) {
        this.listener = dialogListener
    }

    interface NewCompositionListener {
        fun onClickCreateComposition(width: Float, height: Float, name: String)
    }
}
package com.julianawl.testemoov.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.badlogic.gdx.graphics.Color
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.julianawl.testemoov.R
import com.julianawl.testemoov.databinding.AddActorDialogBinding

class AddActorDialog : DialogFragment() {
    private val binding by lazy {
        AddActorDialogBinding.inflate(layoutInflater)
    }

    private val closeBtn by lazy { binding.dialogAddCloseBtn }
    private val addBtn by lazy { binding.dialogAddDoneBtn }

    private val nameEditText by lazy { binding.dialogAddActorNameEt }
    private val colorRadioGroup by lazy { binding.dialogAddColorRdGp }

    private var listener: AddDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialAlertDialogBuilder(requireContext())
        dialog.setView(binding.root)

        this.addBtn.setOnClickListener {
            when {
                nameEditText.text.toString().isEmpty() -> {
                    Toast.makeText(requireContext(), getString(R.string.toast_dialog_add_name_required), Toast.LENGTH_SHORT)
                        .show()
                }
                colorRadioGroup.checkedRadioButtonId == View.NO_ID -> {
                    Toast.makeText(requireContext(), getString(R.string.toast_dialog_add_color_required), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    this.listener?.onClickAddActor(
                        nameEditText.text.toString(), onRadioButtonClicked(), getString(R.string.shape_actor)
                    )
                    dismiss()
                }
            }
        }

        this.closeBtn.setOnClickListener {
            dismiss()
        }

        return dialog.create()
    }

    private fun onRadioButtonClicked(): Color {
        val checkedId = colorRadioGroup.checkedRadioButtonId
        var color: Color = Color.CLEAR
        when (checkedId) {
            R.id.dialog_add_color_1_rd_btn -> color = Color.BLUE
            R.id.dialog_add_color_2_rd_btn -> color = Color.RED
            R.id.dialog_add_color_3_rd_btn -> color = Color.GREEN
        }
        return color
    }

    fun addListener(dialogListener: AddDialogListener) {
        this.listener = dialogListener
    }

    interface AddDialogListener {
        fun onClickAddActor(name: String, color: Color, shape: String)
    }
}
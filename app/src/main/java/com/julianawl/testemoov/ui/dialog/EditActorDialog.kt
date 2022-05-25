package com.julianawl.testemoov.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.badlogic.gdx.graphics.Color
import com.julianawl.testemoov.R
import com.julianawl.testemoov.databinding.EditActorDialogBinding

class EditActorDialog: DialogFragment() {
    private val binding by lazy {
        EditActorDialogBinding.inflate(layoutInflater)
    }

    private val closeBtn by lazy { binding.dialogEditCloseBtn }
    private val editBtn by lazy { binding.dialogEditDoneBtn }
    private val removeBtn by lazy { binding.dialogEditDeleteBtn }

    private val nameEditText by lazy { binding.dialogEditActorNameEt }
    private val colorRadioGroup by lazy { binding.dialogEditColorRdGp }

    private var listener: EditDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setView(binding.root)

        val actor: String = arguments?.getString("Actor").toString()
        nameEditText.setText(actor)

        this.editBtn.setOnClickListener {
            when {
                nameEditText.text.toString().isEmpty() -> {
                    Toast.makeText(requireContext(), "Nome é obrigatório", Toast.LENGTH_SHORT)
                        .show()
                }
                colorRadioGroup.checkedRadioButtonId == View.NO_ID -> {
                    Toast.makeText(requireContext(), "Cor é obrigatório", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    this.listener?.onClickEditActor(
                        actor, nameEditText.text.toString(), onRadioButtonClicked(), "circle")
                    dismiss()
                }
            }
        }

        this.removeBtn.setOnClickListener {
            this.listener?.onClickRemoveActor(actor)
            dismiss()
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

    fun editListener(dialogListener: EditDialogListener) {
        this.listener = dialogListener
    }

    interface EditDialogListener {
        fun onClickEditActor(nameOld: String, name: String, color: Color, shape: String)
        fun onClickRemoveActor(name: String)
    }
}

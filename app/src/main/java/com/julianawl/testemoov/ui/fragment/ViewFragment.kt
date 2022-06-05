package com.julianawl.testemoov.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.julianawl.testemoov.*
import com.julianawl.testemoov.graphics.BaseGraphicClass

class ViewFragment : AndroidFragmentApplication() {

    private val base = BaseGraphicClass()

    private var width = 0f
    private var height = 0f
    private var name: String? = null
    private var prefs: String? = null
    private var compositionId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            width = it.getFloat(WIDTH_KEY)
            height = it.getFloat(HEIGHT_KEY)
            name = it.getString(NAME_KEY)
            prefs = it.getString(PREFERENCES_KEY)
            compositionId = it.getInt(ID_KEY)
        }

        return initializeForView(base.also {
            it.setDimensions(width, height)
            it.setCompositionName(name!!)
            it.setPreferences(prefs!!, compositionId)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sceneCountTv = activity?.findViewById<MaterialTextView>(R.id.view_scene_count_tv)
        val backSceneBtn =
            activity?.findViewById<AppCompatImageButton>(R.id.view_back_scene_btn)
        val nextSceneBtn =
            activity?.findViewById<AppCompatImageButton>(R.id.view_next_scene_btn)
        val editorBtn = activity?.findViewById<AppCompatImageButton>(R.id.view_editor_btn)

        onClickBackSceneBtn(backSceneBtn!!, sceneCountTv!!)
        onClickNextSceneBtn(nextSceneBtn!!, sceneCountTv)
        onClickEditorBtn(editorBtn!!)
    }

    private fun onClickEditorBtn(editorBtn: AppCompatImageButton) {
        editorBtn.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext())
            dialog.setMessage("Deseja abrir o editor?")
                .setTitle("Abrir editor")
                .setPositiveButton("Sim") { _, _ ->
                    //todo
                }
                .setNegativeButton(getString(R.string.dialog_cancel_btn)) { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
            dialog.show()
        }

    }

    private fun onClickBackSceneBtn(
        backSceneBtn: AppCompatImageButton,
        sceneCountTv: MaterialTextView
    ) {
        backSceneBtn.setOnClickListener {
            val sceneCount = base.backViewScene()
            sceneCountTv.text = sceneCount.toString()
            if (sceneCount <= 1) Toast.makeText(
                requireContext(),
                getString(R.string.toast_first_scene),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onClickNextSceneBtn(
        nextSceneBtn: AppCompatImageButton,
        sceneCountTv: MaterialTextView
    ) {
        nextSceneBtn.setOnClickListener {
            val sceneCount = base.nextViewScene()
            sceneCountTv.text = sceneCount.toString()
        }
    }
}
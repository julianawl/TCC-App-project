package com.julianawl.testemoov.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.os.bundleOf
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.badlogic.gdx.graphics.Color
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.julianawl.testemoov.*
import com.julianawl.testemoov.data.ActorName
import com.julianawl.testemoov.graphics.BaseGameClass
import com.julianawl.testemoov.ui.activity.MainActivity
import com.julianawl.testemoov.ui.dialog.AddActorDialog
import com.julianawl.testemoov.ui.dialog.EditActorDialog
import com.julianawl.testemoov.ui.dialog.GetActorsDialog

class CompositionFragment : AndroidFragmentApplication() {

    private val base = BaseGameClass()
    private var addActorDialog: AddActorDialog? = null
    private var editActorDialog: EditActorDialog? = null
    private var getActorsDialog: GetActorsDialog? = null

    private var fixed: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val width = arguments?.getFloat(WIDTH_KEY)!!
        val height = arguments?.getFloat(HEIGHT_KEY)!!
        val name = arguments?.getString(NAME_KEY)!!
        val prefs = arguments?.getString(PREFERENCES_KEY)!!
        val compositionId = arguments?.getInt(ID_KEY)!!
        return initializeForView(base.also {
            it.setDimensions(width, height)
            it.setCompositionName(name)
            it.setPreferences(prefs, compositionId)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addBtn = activity?.findViewById<AppCompatImageButton>(R.id.composition_add_actor_btn)
        val editActorBtn =
            activity?.findViewById<AppCompatImageButton>(R.id.composition_edit_actor_btn)
        val fixActorBtn =
            activity?.findViewById<AppCompatImageButton>(R.id.composition_fix_actors_btn)
        val moveActorBtn =
            activity?.findViewById<AppCompatImageButton>(R.id.composition_add_move_btn)
        val playBtn = activity?.findViewById<AppCompatImageButton>(R.id.composition_play_scene_btn)
        val pauseBtn =
            activity?.findViewById<AppCompatImageButton>(R.id.composition_pause_scene_btn)
        val sceneCountTv = activity?.findViewById<MaterialTextView>(R.id.composition_scene_count_tv)
        val backSceneBtn =
            activity?.findViewById<AppCompatImageButton>(R.id.composition_back_scene_btn)
        val nextSceneBtn =
            activity?.findViewById<AppCompatImageButton>(R.id.composition_next_scene_btn)
        val saveBtn = activity?.findViewById<AppCompatImageButton>(R.id.composition_save_btn)

        onClickAddBtn(addBtn!!)
        onClickEditBtn(editActorBtn!!)
        onClickFixBtn(fixActorBtn!!)
        onClickAddMoveBtn(moveActorBtn!!)
        onClickPlayBtn(playBtn!!, pauseBtn!!)
        onClickBackSceneBtn(backSceneBtn!!, sceneCountTv!!)
        onClickNextSceneBtn(nextSceneBtn!!, sceneCountTv)
        onClickSaveBtn(saveBtn!!)
    }

    private fun onClickSaveBtn(saveBtn: AppCompatImageButton) {
        saveBtn.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext())
            dialog.setMessage(getString(R.string.dialog_save_message))
                .setTitle(getString(R.string.dialog_save_title))
                .setPositiveButton(getString(R.string.dialog_save_btn)) { _, _ ->
                    base.nextScene()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
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
            val sceneCount = base.backScene()
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
            val dialog = MaterialAlertDialogBuilder(requireContext())
            dialog.setMessage(getString(R.string.dialog_next_scene_message))
                .setTitle(getString(R.string.new_scene_dialog_title))
                .setPositiveButton(getString(R.string.create_add_btn_dialog)) { _, _ ->
                    val sceneCount = base.nextScene()
                    sceneCountTv.text = sceneCount.toString()
                }
                .setNegativeButton(getString(R.string.dialog_cancel_btn)) { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
            dialog.show()
        }
    }

    private fun onClickAddMoveBtn(moveActorBtn: AppCompatImageButton) {
        moveActorBtn.setOnClickListener {
            val stopMoveBtn =
                activity?.findViewById<AppCompatImageButton>(R.id.composition_stop_move_btn)
            activity?.let {
                getActorsDialog = GetActorsDialog(getActorsList())
                getActorsDialog?.show(
                    it.supportFragmentManager,
                    getString(R.string.get_actors_dialog_tag)
                )
                getActorsDialog?.onItemClickListener = { actor ->
                    base.addMovement(actor.name)
                    getActorsDialog?.dismiss()
                    stopMoveBtn?.visibility = View.VISIBLE
                    stopMoveBtn?.setOnClickListener {
                        base.stopMovement(actor.name)
                        stopMoveBtn.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun onClickFixBtn(fixActorBtn: AppCompatImageButton) {
        fixActorBtn.setOnClickListener {
            activity?.let {
                getActorsDialog = GetActorsDialog(getActorsList())
                getActorsDialog?.show(
                    it.supportFragmentManager,
                    getString(R.string.get_actors_dialog_tag)
                )
                getActorsDialog?.onItemClickListener = { actor ->
                    fixed = base.setInitialPosition(actor.name)
                    getActorsDialog?.dismiss()
                }
            }
        }
    }

    private fun onClickAddBtn(addBtn: AppCompatImageButton) {
        addBtn.setOnClickListener {
            activity?.let {
                addActorDialog = AddActorDialog()
                addActorDialog?.show(
                    it.supportFragmentManager,
                    getString(R.string.add_actor_dialog_tag)
                )
                addActorDialog?.addListener(object : AddActorDialog.AddDialogListener {
                    override fun onClickAddActor(name: String, color: Color, shape: String) {
                        base.addActor(name, color, shape)
                    }
                })
            }
        }
    }

    private fun onClickEditBtn(editBtn: AppCompatImageButton) {
        editBtn.setOnClickListener {
            if (getActorsList().isNotEmpty()) {
                activity?.let {
                    getActorsDialog = GetActorsDialog(getActorsList())
                    getActorsDialog?.show(
                        it.supportFragmentManager,
                        getString(R.string.get_actors_dialog_tag)
                    )
                    getActorsDialog?.onItemClickListener = { actor ->
                        editActorDialog = EditActorDialog()
                        editActorDialog?.arguments = bundleOf(Pair(ACTOR_KEY, actor.name))
                        activity?.supportFragmentManager?.let { it ->
                            editActorDialog!!.show(
                                it,
                                getString(R.string.edit_actor_dialog_tag)
                            )
                        }
                        editActorDialog?.editListener(object : EditActorDialog.EditDialogListener {
                            override fun onClickEditActor(
                                nameOld: String,
                                name: String,
                                color: Color,
                                shape: String
                            ) {
                                base.editActor(nameOld, name, color, shape)
                                getActorsDialog?.dismiss()
                            }

                            override fun onClickRemoveActor(name: String) {
                                base.removeActor(name)
                                getActorsDialog?.dismiss()
                            }
                        })
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_empty_list),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun onClickPlayBtn(playBtn: AppCompatImageButton, pauseBtn: AppCompatImageButton) {
        playBtn.setOnClickListener {
            if (fixed == 0 || fixed == -1) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_fix_actors),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                playBtn.visibility = View.GONE
                pauseBtn.visibility = View.VISIBLE
                base.playScene()
                pauseBtn.setOnClickListener {
                    playBtn.visibility = View.VISIBLE
                    pauseBtn.visibility = View.GONE
                    base.stopScene()
                }
            }
        }
    }

    private fun getActorsList(): MutableList<ActorName> {
        val actors = base.getActors()
        val actorsList: MutableList<ActorName> = mutableListOf()
        if (!actors!!.isEmpty) {
            for (actor in actors) {
                actorsList.add(ActorName(actor.name))
            }
        }
        return actorsList
    }
}


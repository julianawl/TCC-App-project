package com.julianawl.testemoov.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.os.bundleOf
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.badlogic.gdx.graphics.Color
import com.julianawl.testemoov.R
import com.julianawl.testemoov.actor.model.ActorModel
import com.julianawl.testemoov.ui.MyGame
import com.julianawl.testemoov.ui.dialog.AddActorDialog
import com.julianawl.testemoov.ui.dialog.EditActorDialog
import com.julianawl.testemoov.ui.dialog.GetActorsDialog

class CompositionFragment : AndroidFragmentApplication() {

    private val myGame = MyGame()
    private var addActorDialog: AddActorDialog? = null
    private var editActorDialog: EditActorDialog? = null
    private var getActorsDialog: GetActorsDialog? = null

    private var fixed: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val width = arguments?.getFloat("width")!!
        val height = arguments?.getFloat("height")!!
        return initializeForView(myGame.also {
            it.getDimensions(width, height)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addBtn = activity?.findViewById<AppCompatImageButton>(R.id.composition_add_actor_btn)
        onClickAddBtn(addBtn!!)
        val editActorBtn =
            activity?.findViewById<AppCompatImageButton>(R.id.composition_edit_actor_btn)
        onClickEditBtn(editActorBtn!!)

        //aparece apenas na primeira cena
        val fixActorBtn =
            activity?.findViewById<AppCompatImageButton>(R.id.composition_fix_actors_btn)
        onClickFixBtn(fixActorBtn!!)

        val moveActorBtn =
            activity?.findViewById<AppCompatImageButton>(R.id.composition_add_move_btn)
        onClickAddMoveBtn(moveActorBtn!!)

        val playBtn = activity?.findViewById<AppCompatImageButton>(R.id.composition_play_frame_btn)
        val pauseBtn = activity?.findViewById<AppCompatImageButton>(R.id.composition_pause_frame_btn)
        onClickPlayBtn(playBtn!!, pauseBtn!!)

        val nextFrameBtn = activity?.findViewById<AppCompatImageButton>(R.id.composition_next_frame_btn)
        onClickNextFrameBtn(nextFrameBtn!!)
    }

    private fun onClickNextFrameBtn(nextFrameBtn: AppCompatImageButton) {
        //salvar frame atual
        //ir para o próximo frame
    }

    private fun onClickAddMoveBtn(moveActorBtn: AppCompatImageButton) {
        moveActorBtn.setOnClickListener {
            val stopMoveBtn =
                activity?.findViewById<AppCompatImageButton>(R.id.composition_stop_move_btn)
            activity?.let {
                getActorsDialog = GetActorsDialog(getActorsList())
                getActorsDialog?.show(it.supportFragmentManager, "get actors dialog")
                getActorsDialog?.onItemClickListener = { actor ->
                    myGame.addMovement(actor.name)
                    getActorsDialog?.dismiss()
                    stopMoveBtn?.visibility = View.VISIBLE
                    stopMoveBtn?.setOnClickListener {
                        myGame.stopMovement(actor.name)
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
                getActorsDialog?.show(it.supportFragmentManager, "get actors dialog")
                getActorsDialog?.onItemClickListener = { actor ->
                    fixed = myGame.fixActorAtPosition(actor.name)
                    getActorsDialog?.dismiss()
                }
            }
        }
    }

    private fun onClickAddBtn(addBtn: AppCompatImageButton) {
        addBtn.setOnClickListener {
            activity?.let {
                addActorDialog = AddActorDialog()
                addActorDialog?.show(it.supportFragmentManager, "add actor dialog")
                addActorDialog?.addListener(object : AddActorDialog.AddDialogListener {
                    override fun onClickAddActor(name: String, color: Color, shape: String) {
                        myGame.addActor(name, color, shape)
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
                    getActorsDialog?.show(it.supportFragmentManager, "get actors dialog")
                    getActorsDialog?.onItemClickListener = { actor ->
                        editActorDialog = EditActorDialog()
                        editActorDialog?.arguments = bundleOf(Pair("Actor", actor.name))
                        activity?.supportFragmentManager?.let { it ->
                            editActorDialog!!.show(
                                it,
                                "edit actor dialog"
                            )
                        }
                        editActorDialog?.editListener(object : EditActorDialog.EditDialogListener {
                            override fun onClickEditActor(
                                nameOld: String,
                                name: String,
                                color: Color,
                                shape: String
                            ) {
                                myGame.editActor(nameOld, name, color, shape)
                                getActorsDialog?.dismiss()
                            }

                            override fun onClickRemoveActor(name: String) {
                                myGame.removeActor(name)
                                getActorsDialog?.dismiss()
                            }
                        })
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Lista de bailarinos vazia", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun onClickPlayBtn(playBtn: AppCompatImageButton, pauseBtn: AppCompatImageButton){
        playBtn.setOnClickListener {
            if(fixed == 0 || fixed == -1){
                Toast.makeText(requireContext(), "É necessário fixar os atores primeiro", Toast.LENGTH_SHORT).show()
            } else {
                playBtn.visibility = View.GONE
                pauseBtn.visibility = View.VISIBLE
                myGame.playFrame()
                pauseBtn.setOnClickListener {
                    playBtn.visibility = View.VISIBLE
                    pauseBtn.visibility = View.GONE
                    myGame.pauseFrame()
                }
            }
        }
    }

    private fun getActorsList(): MutableList<ActorModel> {
        val actors = myGame.getActors()
        val actorsList: MutableList<ActorModel> = mutableListOf()
        if (!actors!!.isEmpty) {
            for (actor in actors) {
                actorsList.add(ActorModel(actor.name))
            }
        }
        return actorsList
    }
}


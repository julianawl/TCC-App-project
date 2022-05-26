package com.julianawl.testemoov.graphics

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import ktx.app.KtxGame
import ktx.app.KtxScreen

class BaseGameClass : KtxGame<KtxScreen>() {
    private val screen = FrameScreen()

    override fun create() {
        addScreen(screen)
        setScreen<FrameScreen>()
    }

    fun addActor(name: String, color: Color, shape: String){
        screen.addActor(name, color, shape)
    }

    fun editActor(nameOld: String, name: String, color: Color, shape: String){
        screen.editActor(nameOld, name, color, shape)
    }

    fun removeActor(actor: String){
        screen.removeActor(actor)
    }

    fun getActors(): Array<Actor>?{
        return screen.getActors()
    }

    fun setDimensions(width: Float, height: Float){
        screen.width = width
        screen.height = height
    }

    fun fixActorAtPosition(actorName: String): Int{
        return screen.fixActorAtPosition(actorName)
    }

    fun addMovement(actorName: String){
        screen.addMovement(actorName)
    }

    fun stopMovement(actorName: String){
        screen.stopMovement(actorName)
    }

    fun playFrame(){
        screen.playFrame()
    }

    fun pauseFrame(){
        screen.pauseFrame()
    }
}
package com.julianawl.testemoov.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.FitViewport
import com.julianawl.testemoov.graphics.actor.ActorManager
import com.julianawl.testemoov.graphics.actor.MyActor
import com.julianawl.testemoov.graphics.background.Background
import ktx.app.KtxScreen

class FrameScreen: KtxScreen {

    private lateinit var background: Background
    private lateinit var renderer: ShapeRenderer
    private lateinit var backgroundTexture: Texture
    var width: Float = 0f
    var height: Float = 0f
    private val actorManager by lazy {
        ActorManager(background)
    }

    override fun show() {
        renderer = ShapeRenderer()
        renderer.setAutoShapeType(true)
        background = Background(FitViewport(width, height))
        Gdx.input.inputProcessor = background
        backgroundTexture = background.createBackgroundTexture()
    }

    fun addActor(name: String, color: Color, shape: String) {
        Gdx.app.postRunnable {
            background.addMyActor(
                actorManager.addActorWithColor(
                    color,
                    shape,
                    name
                )
            )
        }
    }

    fun editActor(nameOld: String, name: String, color: Color, shape: String) {
        Gdx.app.postRunnable { actorManager.editActorWithColor(nameOld, name, color, shape) }
    }

    fun removeActor(actor: String) {
        actorManager.removeActor(actor)
    }

    fun getActors(): Array<Actor>? {
        return background.actors
    }

    fun fixActorAtPosition(actorName: String): Int{
        val actor = background.root.findActor<MyActor>(actorName)
        return actor.fixActorAtPosition()
    }

    fun addMovement(actorName: String){
        val actor = background.root.findActor<MyActor>(actorName)
        actor.addMovement()
    }

    fun stopMovement(actorName: String){
        val actor = background.root.findActor<MyActor>(actorName)
        actor.stopMovement()
        Gdx.app.postRunnable { actorManager.drawLine(actor.initialPosition, actor.finalPosition) }
    }

    fun playFrame(){
        for(actor in background.actors) {
            val myActor = background.root.findActor<MyActor>(actor.name)
            with(myActor) {
                addAction(actorManager.addMovementAsAction(this,1f))
            }
        }
    }

    fun pauseFrame(){
        for (actor in background.actors){
            val myActor = background.root.findActor<MyActor>(actor.name)
            myActor.backToInitialPosition()
        }
    }

    fun saveFrame(){

    }

    fun buildNewFrame(){

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.172f, 0.184f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        background.batch.begin()
        background.batch.draw(backgroundTexture, 0f, 0f)
        background.batch.end()
        background.draw()
        background.act()
    }

    override fun dispose() {
        background.dispose()
    }
}
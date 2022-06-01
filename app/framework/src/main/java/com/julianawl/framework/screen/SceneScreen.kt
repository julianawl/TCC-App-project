package com.julianawl.framework.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.FitViewport
import com.julianawl.framework.actor.ActorManager
import com.julianawl.framework.actor.ImageActor
import com.julianawl.framework.actor.MyActor
import com.julianawl.framework.background.Background
import com.julianawl.framework.model.ActorModel
import ktx.app.KtxScreen

class SceneScreen() : KtxScreen {

    private val background by lazy {
        val result = Background(FitViewport(width, height))
        Gdx.input.inputProcessor = result
        result
    }
    private val backgroundTexture by lazy { background.createBackgroundTexture() }
    var width: Float = 0f
    var height: Float = 0f
    var actionDuration: Float = 0f
    private val actorManager by lazy {
        ActorManager(background)
    }

    override fun show() {}

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

    fun addActorAtPosition(name: String, texture: Texture, position: Vector2) {
        removeActor(name)
        Gdx.app.postRunnable {
            background.addMyActor(
                actorManager.addActorWithTexture(
                    texture,
                    name,
                    position
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

    fun fixActorAtPosition(actorName: String): Int {
        val actor = background.root.findActor<MyActor>(actorName)
        return actor.fixActorAtPosition()
    }

    fun addMovement(actorName: String) {
        val actor = background.root.findActor<MyActor>(actorName)
        actor.addMovement()
    }

    fun stopMovement(actorName: String) {
        val actor = background.root.findActor<MyActor>(actorName)
        actor.stopMovement()
    }

    fun playScene() {
        for (actor in background.actors) {
            val myActor = background.root.findActor<MyActor>(actor.name)
            with(myActor) {
                addAction(
                    actorManager.addMovementAsAction(
                        this,
                        actionDuration,
                        Interpolation.linear
                    )
                )
            }
        }
    }

    fun stopScene() {
        for (actor in background.actors) {
            val myActor = background.root.findActor<MyActor>(actor.name)
            myActor.backToInitialPosition()
        }
    }

    fun setActorsList(): MutableList<ActorModel> {
        val sceneActors = arrayListOf<ActorModel>()
        getActors()?.forEach { actor ->
            val myActor = background.root?.findActor<MyActor>(actor.name)
            val imageActor = myActor?.findActor<ImageActor>("${actor.name} Image")
            sceneActors.add(
                ActorModel(
                    actor.name,
                    actor.color,
                    imageActor?.getTexture(),
                    getActorInitialPosition(background.root.findActor(actor.name)),
                    getActorFinalPosition(background.root.findActor(actor.name))
                )
            )
        }
        return sceneActors
    }

    private fun getActorInitialPosition(actor: MyActor): Vector2 = actor.initialPosition

    private fun getActorFinalPosition(actor: MyActor): Vector2 = actor.finalPosition

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
        backgroundTexture.dispose()
    }
}
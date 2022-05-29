package com.julianawl.testemoov.graphics

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import com.julianawl.testemoov.ModelPreferencesManager
import com.julianawl.testemoov.graphics.model.SceneModel
import com.julianawl.testemoov.graphics.model.SetModel
import ktx.app.KtxGame
import ktx.app.KtxScreen

class BaseGameClass : KtxGame<KtxScreen>() {
    private val screen = SceneScreen()
    private var scenesCount = 1
    private lateinit var setName: String

    override fun create() {
        addScreen(screen)
        setScreen<SceneScreen>()
    }

    fun addActor(name: String, color: Color, shape: String) {
        screen.addActor(name, color, shape)
    }

    fun editActor(nameOld: String, name: String, color: Color, shape: String) {
        screen.editActor(nameOld, name, color, shape)
    }

    fun removeActor(actor: String) {
        screen.removeActor(actor)
    }

    fun getActors(): Array<Actor>? {
        return screen.getActors()
    }

    fun setDimensions(width: Float, height: Float) {
        screen.width = width
        screen.height = height
    }

    fun setCompositionName(name: String) {
        this.setName = name
    }

    fun setInitialPosition(actorName: String): Int {
        return screen.fixActorAtPosition(actorName)
    }

    fun addMovement(actorName: String) {
        screen.addMovement(actorName)
    }

    fun stopMovement(actorName: String) {
        screen.stopMovement(actorName)
    }

    fun playScene() {
        screen.playScene()
    }

    fun stopScene() {
        screen.stopScene()
    }

    fun nextScene() {
        saveScene()
        scenesCount++
        buildNewScreen()
    }

    private fun buildNewScreen() {
        setScreen<SceneScreen>()
        val scenes = ModelPreferencesManager.get<SetModel>(setName)?.scenes
        val lastScene = scenes?.last()
        lastScene?.actors?.forEach { actor ->
            screen.addActorAtPosition(actor.name, actor.color, "circle", actor.finalPosition!!)
        }
    }

    private fun saveScene() {
        val set = ModelPreferencesManager.get<SetModel>(setName)
        if (scenesCount == 1) {
            set?.scenes?.clear()
            set?.scenes?.add(SceneModel(scenesCount, screen.setActorsList()))
        } else {
            set?.scenes?.add(SceneModel(scenesCount, screen.setActorsList()))
        }
        ModelPreferencesManager.put(set, setName)
    }
}
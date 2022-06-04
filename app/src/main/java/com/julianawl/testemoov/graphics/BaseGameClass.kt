package com.julianawl.testemoov.graphics

import android.util.Base64
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import com.julianawl.framework.model.SceneModel
import com.julianawl.framework.model.SetList
import com.julianawl.framework.model.SetModel
import com.julianawl.framework.screen.SceneScreen
import com.julianawl.testemoov.data.ModelPreferencesManager
import ktx.app.KtxGame
import ktx.app.KtxScreen

class BaseGameClass : KtxGame<KtxScreen>() {
    private val screen = SceneScreen()
    private var scenesCount = 1
    private lateinit var setName: String
    private lateinit var prefs: String
    private var setId: Int = 0

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

    fun setPreferences(prefs: String, id: Int) {
        this.prefs = prefs
        this.setId = id
        val compositions = ModelPreferencesManager.get<SetList>(prefs)?.setList
        val set = compositions?.find { set -> set.id == id }
        if (set?.scenes?.isNotEmpty()!!) {
            buildSet(set)
        }
    }

    fun addMovement(actorName: String) {
        screen.addMovement(actorName)
    }

    fun stopMovement(actorName: String) {
        screen.stopMovement(actorName)
    }

    fun playScene() {
        screen.playScene(1f, Interpolation.linear)
    }

    fun stopScene() {
        screen.stopScene()
    }

    fun nextScene(): Int {
        saveScene()
        scenesCount++
        buildNewScreen()
        return scenesCount
    }

    fun backScene(): Int {
        if (scenesCount > 0){
            saveScene()
            buildPreviousScreen()
            scenesCount--
        }
        return scenesCount
    }

    private fun buildSet(set: SetModel) {
        val scene = set.scenes?.first()
        scene?.actors?.forEach { actor ->
            screen.addActorAtPosition(
                actor.name,
                convertStringToTexture(actor.texture!!),
                actor.finalPosition!!
            )
        }
    }

    private fun convertStringToTexture(encode: String): Texture {
        val decodedBytes: ByteArray = Base64.decode(encode, Base64.NO_WRAP)
        val pixmap: Pixmap = Pixmap(decodedBytes, 0, decodedBytes.size)
        return Texture(pixmap)
    }

    private fun buildPreviousScreen() {
        setScreen<SceneScreen>()
        if (scenesCount > 1) {
            val setList = ModelPreferencesManager.get<SetList>(prefs)?.setList
            val scenes = setList?.find { setModel -> setModel.name == setName }?.scenes
            val previousScene = scenes?.find { sceneModel -> sceneModel.id == scenesCount - 1 }
            previousScene?.actors?.forEach { actor ->
                screen.backActorToInitialPosition(actor.name, actor.initialPosition!!)
            }
        }
    }

    private fun buildNewScreen() {
        setScreen<SceneScreen>()
        if (scenesCount > 1) {
            val setList = ModelPreferencesManager.get<SetList>(prefs)?.setList
            val scenes = setList?.find { setModel -> setModel.name == setName }?.scenes
            val lastScene = scenes?.last()
            screen.playScene(1f, Interpolation.linear)
            lastScene?.actors?.forEach { actor ->
                screen.setNewScreenActorPosition(actor.name, actor.finalPosition!!)
            }
        }
    }

    private fun saveScene() {
        val composition = ModelPreferencesManager.get<SetList>(prefs)
        val setList = composition?.setList
        val set = setList?.first { it.id == setId }
        if (scenesCount == 1) {
            set?.scenes?.clear()
            set?.scenes?.add(SceneModel(scenesCount, screen.setActorsList()))
        } else {
            set?.scenes?.add(SceneModel(scenesCount, screen.setActorsList()))
        }
        composition?.setList?.set(setId - 1, set!!)
        ModelPreferencesManager.put(composition, prefs)
    }
}
package com.julianawl.testemoov.graphics

import com.badlogic.gdx.graphics.Color
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

class BaseGraphicClass : KtxGame<KtxScreen>() {
    private val screen = SceneScreen()

    private var scenesCount = 1
    private var scenesPage = 1

    private lateinit var setName: String
    private lateinit var prefs: String
    private var setId: Int = 0

    override fun create() {
        addScreen(screen)
        val compositions = ModelPreferencesManager.get<SetList>(prefs)?.setList
        val set = compositions?.find { set -> set.id == setId }
        if (set?.scenes?.isNotEmpty()!!) {
            setScreen<SceneScreen>()
            buildViewScreen(set)
        } else {
            setScreen<SceneScreen>()
        }
    }

    private fun buildViewScreen(set: SetModel) {
        val scene = set.scenes?.first()
        scene?.actors?.forEach { actor ->
            screen.addActorAtPosition(
                actor.name,
                actor.texture!!,
                actor.finalPosition!!
            )
        }
    }

    fun setDimensions(width: Float, height: Float) {
        screen.width = width
        screen.height = height
    }

    fun setCompositionName(name: String) {
        this.setName = name
    }

    fun setPreferences(prefs: String, id: Int) {
        this.prefs = prefs
        this.setId = id
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

    fun fixActorAtPosition(actorName: String): Int {
        return screen.fixActorAtPosition(actorName)
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

    fun nextEditorScene(): Int {
        saveScene()
        scenesCount++
        buildNewEditorScreen()
        return scenesCount
    }

    fun backEditorScene(): Int {
        if (scenesCount > 0) {
            saveScene()
            buildPreviousEditorScreen()
            scenesCount--
        }
        return scenesCount
    }

    fun nextViewScene(): Int {
        val compositions = ModelPreferencesManager.get<SetList>(prefs)?.setList
        val set = compositions?.find { set -> set.id == setId }
        if(scenesPage != set?.scenes?.size!!) {
            scenesPage++
            buildNewViewScreen()
        }
        return scenesPage
    }

    fun backViewScene(): Int {
        if(scenesPage > 1){
            buildPreviousViewScreen()
            scenesPage--
        }
        return scenesPage
    }

    private fun buildPreviousEditorScreen() {
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

    private fun buildPreviousViewScreen() {
        setScreen<SceneScreen>()
        if (scenesPage > 1) {
            val setList = ModelPreferencesManager.get<SetList>(prefs)?.setList
            val scenes = setList?.find { setModel -> setModel.name == setName }?.scenes
            val previousScene = scenes?.find { sceneModel -> sceneModel.id == scenesCount - 1 }
            previousScene?.actors?.forEach { actor ->
                screen.backActorToInitialPosition(actor.name, actor.initialPosition!!)
            }
        }
    }

    private fun buildNewEditorScreen() {
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

    private fun buildNewViewScreen() {
        setScreen<SceneScreen>()
        if (scenesPage > 1) {
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
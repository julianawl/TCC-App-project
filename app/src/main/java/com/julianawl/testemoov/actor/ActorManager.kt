package com.julianawl.testemoov.actor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.julianawl.testemoov.background.Background


class ActorManager(private val background: Background) {

    private val actorSize = 100f
    private val renderer: ShapeRenderer = ShapeRenderer()

    private fun createActorTexture(color: Color, shape: String): Texture {
        val pixmap: Pixmap
        when (shape) {
            "rectangle" -> {
                pixmap = Pixmap(
                    actorSize.toInt(),
                    actorSize.toInt(),
                    Pixmap.Format.RGBA8888
                )
                pixmap.setColor(color)
                pixmap.fillRectangle(0, 0, background.width.toInt(), background.height.toInt())
            }
            "circle" -> {
                pixmap = Pixmap(
                    actorSize.toInt() * 2,
                    actorSize.toInt() * 2,
                    Pixmap.Format.RGBA8888
                )
                pixmap.setColor(color)
                pixmap.fillCircle(100, 100, actorSize.toInt())
            }
            else -> pixmap = Pixmap(
                actorSize.toInt(),
                actorSize.toInt(),
                Pixmap.Format.RGBA8888
            )
        }
        return Texture(pixmap)
    }

    private fun setActorInitialPosition(): Vector2 {
        return Vector2(background.width / 2 - actorSize / 2, background.height / 2 - actorSize / 2)
    }

    fun addActorWithColor(color: Color, shape: String, name: String): MyActor {
        val actorShape = ImageActor(
            actorSize,
            actorSize,
            createActorTexture(color, shape)
        )
        actorShape.name = "$name Image"
        val font = BitmapFont(Gdx.files.internal("fonts/roboto_med_italic.fnt"))
        val actorLabel = Label(
            name, Label.LabelStyle(font, Color.WHITE)
        )
        actorLabel.name = "$name Label"
        val actor = MyActor(setActorInitialPosition().x, setActorInitialPosition().y)
        actor.addActor(actorShape)
        actor.addActor(actorLabel)
        actor.name = name
        return actor
    }

    fun addActorWithTexture(texture: Texture): ImageActor {
        return ImageActor(
            actorSize,
            actorSize,
            texture
        )
    }

    fun editActorWithColor(nameOld: String, name: String, color: Color, shape: String) {
        val myActor = background.root.findActor<MyActor>(nameOld)
        myActor.name = name
        val label = myActor.findActor<Label>("$nameOld Label")
        val image = myActor.findActor<ImageActor>("$nameOld Image")
        image.changeTexture(createActorTexture(color, shape))
        label.setText(name)
    }

    fun removeActor(name: String) {
        background.root.findActor<Group>(name).remove()
    }

    fun drawLine(initialPosition: Vector2, finalPosition: Vector2) {
        renderer.begin(ShapeRenderer.ShapeType.Line)
        renderer.color = Color.RED
        renderer.line(initialPosition, finalPosition)
        renderer.end()
    }

    fun addMovementAsAction(actor: MyActor, duration: Float): Action {
        val action = Actions.action(MoveToAction::class.java)
        action.setPosition(actor.finalPosition.x, actor.finalPosition.y)
        action.duration = duration
        action.interpolation = Interpolation.linear
        return action
    }

//    var gridWidth: Float = mapGrid.indices * CELL_SIZE
//    var gridHeight: Float = mapGrid.get(x).size * CELL_SIZE
//    var cellX: Float = x * CELL_SIZE + SCREEN_CENTER_X - gridWidth / 2f
//    var cellY: Float = y * CELL_SIZE + SCREEN_CENTER_Y - gridHeight / 2f
}
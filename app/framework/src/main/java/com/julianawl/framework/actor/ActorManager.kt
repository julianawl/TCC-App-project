package com.julianawl.framework.actor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.julianawl.framework.background.Background
import java.io.ByteArrayOutputStream

class ActorManager(private val background: Background, private val actorSize: Float) {

    private fun createActorTexture(color: Color, shape: String): Texture {
        val pixmap: Pixmap
        when (shape) {
            RECTANGLE_SHAPE -> {
                pixmap = Pixmap(
                    actorSize.toInt(),
                    actorSize.toInt(),
                    Pixmap.Format.RGBA8888
                )
                pixmap.setColor(color)
                pixmap.fillRectangle(0, 0, background.width.toInt(), background.height.toInt())
            }
            CIRCLE_SHAPE -> {
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

    fun convertPixmapToString(pixmap: Pixmap, name: String): String? {
        val image = Gdx.files.local("${name}.png")
        PixmapIO.writePNG(image, pixmap)

        val bm = BitmapFactory.decodeFile(image.file().absolutePath)
        val baos = ByteArrayOutputStream()

        image.delete()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)

        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.NO_WRAP)
    }

    private fun convertStringToTexture(encode: String): Texture {
        val decodedBytes: ByteArray = Base64.decode(encode, Base64.NO_WRAP)
        val pixmap = Pixmap(decodedBytes, 0, decodedBytes.size)
        return Texture(pixmap)
    }

    private fun setActorInitialPosition(): Vector2 {
        return Vector2(background.width / 2 - actorSize / 2, background.height / 2 - actorSize / 2)
    }

    fun setActorInitialPosition(myActor: MyActor, position: Vector2) {
        myActor.initialPosition.set(position.x, position.y)
    }

    fun backActorToInitialPosition(myActor: MyActor, position: Vector2) {
        myActor.initialPosition.set(position.x, position.y)
        myActor.backToInitialPosition()
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
        actor.color = color
        return actor
    }

    fun addActorWithColor(color: Color, shape: String, name: String, position: Vector2): MyActor {
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
        val actor = MyActor(position.x, position.y)
        actor.addActor(actorShape)
        actor.addActor(actorLabel)
        actor.name = name
        actor.color = color
        return actor
    }

    fun addActorWithTexture(texture: Texture, name: String): MyActor {
        val actorShape = ImageActor(actorSize, actorSize, texture)
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

    fun addActorWithTexture(texture: String, name: String, position: Vector2): MyActor {
        val actorShape = ImageActor(actorSize, actorSize, convertStringToTexture(texture))
        actorShape.name = "$name Image"
        val font = BitmapFont(Gdx.files.internal("fonts/roboto_med_italic.fnt"))
        val actorLabel = Label(
            name, Label.LabelStyle(font, Color.WHITE)
        )
        actorLabel.name = "$name Label"
        val actor = MyActor(position.x, position.y)
        actor.addActor(actorShape)
        actor.addActor(actorLabel)
        actor.name = name
        return actor
    }

    fun editActorWithColor(nameOld: String, name: String, color: Color, shape: String) {
        val myActor = background.root.findActor<MyActor>(nameOld)
        myActor.name = name
        val label = myActor.findActor<Label>("$nameOld Label")
        val image = myActor.findActor<ImageActor>("$nameOld Image")
        image.color = color
        image.changeTexture(createActorTexture(color, shape))
        label.setText(name)
    }

    fun editActorWithTexture(nameOld: String, name: String, texture: Texture) {
        val myActor = background.root.findActor<MyActor>(nameOld)
        myActor.name = name
        val label = myActor.findActor<Label>("$nameOld Label")
        val image = myActor.findActor<ImageActor>("$nameOld Image")
        image.changeTexture(texture)
        label.setText(name)
    }

    fun removeActor(name: String) {
        background.root.findActor<Group>(name).remove()
    }

    fun addMovementAsAction(actor: MyActor, duration: Float, interpolation: Interpolation): Action {
        val action = Actions.action(MoveToAction::class.java)
        action.setPosition(actor.finalPosition.x, actor.finalPosition.y)
        action.duration = duration
        action.interpolation = interpolation
        return action
    }

    companion object{
        const val RECTANGLE_SHAPE = "rectangle"
        const val CIRCLE_SHAPE = "circle"
    }

}
package com.julianawl.testemoov.graphics.actor

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.utils.DragListener


class MyActor(actorX: Float, actorY: Float) : Group() {

    private var listenerIsOn = true
    val initialPosition by lazy { Vector2(x,y) }
    val finalPosition by lazy { Vector2(x,y) }

    init {
        x = actorX
        y = actorY

        touchable = Touchable.enabled
        addListener(object : DragListener() {
            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                this@MyActor.moveBy(x - this@MyActor.width, y - this@MyActor.height)
            }
        })
    }

    fun fixActorAtPosition() : Int{
        return if (listenerIsOn) {
            touchable = Touchable.disabled
            initialPosition.set(x, y)
            listenerIsOn = false
            1
        } else {
            unfixActorAtPosition()
            0
        }
    }

    private fun unfixActorAtPosition() {
        touchable = Touchable.enabled
        listenerIsOn = true
    }

    fun addMovement() {
        touchable = Touchable.enabled
        listenerIsOn = true
    }

    fun stopMovement() {
        finalPosition.set(x, y)
        listenerIsOn = false
        touchable = Touchable.disabled
        backToInitialPosition()
    }

    fun backToInitialPosition() {
        x = initialPosition.x
        y = initialPosition.y
    }
}
package com.julianawl.framework.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2

data class ActorModel(
    val name: String,
    val color: Color,
    val texture: Texture?,
    val initialPosition: Vector2?,
    val finalPosition: Vector2?
)

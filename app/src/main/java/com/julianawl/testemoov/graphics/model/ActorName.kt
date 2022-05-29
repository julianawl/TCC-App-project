package com.julianawl.testemoov.graphics.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2

data class ActorName(
    val name: String,
)

data class ActorModel(
    val name: String,
    val color: Color,
    val initialPosition: Vector2?,
    val finalPosition: Vector2?
)

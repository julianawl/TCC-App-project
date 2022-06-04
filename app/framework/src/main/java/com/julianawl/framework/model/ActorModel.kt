package com.julianawl.framework.model

import com.badlogic.gdx.math.Vector2

data class ActorModel(
    val name: String,
    val texture: String?,
    val initialPosition: Vector2?,
    val finalPosition: Vector2?
)

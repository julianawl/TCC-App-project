package com.julianawl.testemoov.graphics.actor

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

class ImageActor(
    actorWidth: Float,
    actorHeight: Float,
    private var texture: Texture
) : Actor() {

    init {
        width = actorWidth
        height = actorHeight
    }

    fun changeTexture(texture: Texture) {
        this.texture = texture
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(texture, x, y, width, height)
    }
}
package com.julianawl.framework.actor

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.TextureData
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

    fun getTexture(): TextureData = this.texture.textureData

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(texture, x, y, width, height)
    }
}
package com.julianawl.framework.background

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import com.julianawl.framework.actor.MyActor


class Background(
    viewport: Viewport
) : Stage(viewport) {

    fun createBackgroundTexture(): Texture {
        val texture: Texture
        val pixmap = Pixmap(
            width.toInt(),
            height.toInt(),
            Pixmap.Format.RGBA8888
        )

        pixmap.setColor(Color.WHITE)
        pixmap.drawRectangle(0, 0, width.toInt(), height.toInt())
        texture = Texture(pixmap)
        pixmap.dispose()
        return texture
    }

    fun addMyActor(actor: MyActor) {
        root.addActor(actor)
    }
}
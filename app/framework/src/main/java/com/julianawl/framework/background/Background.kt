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

//    private fun drawGrid(pixmap: Pixmap): Pixmap {
//        pixmap.setColor(Color.WHITE)
//        pixmap.drawRectangle(0, 0, width.toInt(), height.toInt())
//        val cellSize = 25
//        val mapGrid = Array(width.toInt()) { IntArray(height.toInt()) }
//        for (x in mapGrid.indices) {
//            for (y in 0 until mapGrid[x].size) {
//                pixmap.drawLine(x * cellSize, 0, x * cellSize, y * cellSize)
//                pixmap.drawLine(0, y * cellSize, x * cellSize, y * cellSize)
//            }
//        }
//        return pixmap
//    }
}
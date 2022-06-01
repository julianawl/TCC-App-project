package com.julianawl.framework.model

data class SetModel(
    val id: Int,
    val name: String,
    val stageWidth: Float,
    val stageHeight: Float,
    val scenes: MutableList<SceneModel>?
)

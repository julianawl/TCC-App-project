package com.julianawl.testemoov.graphics.model

data class SetModel(
    val id: Int,
    val name: String,
    val stageWidth: Float,
    val stageHeight: Float,
    val scenes: MutableList<SceneModel>?
)

data class SetList(
    val setList: MutableList<SetModel>?
)

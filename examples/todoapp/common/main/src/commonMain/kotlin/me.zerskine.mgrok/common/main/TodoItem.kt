package me.zerskine.mgrok.common.main

data class TodoItem(
    val id: Long = 0L,
    val order: Long = 0L,
    val text: String = "",
    val isDone: Boolean = false
)

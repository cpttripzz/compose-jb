package me.zerskine.mgrok.common.edit.store

import com.arkivanov.mvikotlin.core.store.Store
import me.zerskine.mgrok.common.edit.TodoItem
import me.zerskine.mgrok.common.edit.store.TodoEditStore.Intent
import me.zerskine.mgrok.common.edit.store.TodoEditStore.Label
import me.zerskine.mgrok.common.edit.store.TodoEditStore.State

internal interface TodoEditStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class SetText(val text: String) : Intent()
        data class SetDone(val isDone: Boolean) : Intent()
    }

    data class State(
        val text: String = "",
        val isDone: Boolean = false
    )

    sealed class Label {
        data class Changed(val item: TodoItem) : Label()
    }
}

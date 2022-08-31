package me.zerskine.mgrok.common.edit.store

import com.arkivanov.mvikotlin.core.store.Store
import me.zerskine.mgrok.common.edit.MgrokItem
import me.zerskine.mgrok.common.edit.store.MgrokEditStore.Intent
import me.zerskine.mgrok.common.edit.store.MgrokEditStore.Label
import me.zerskine.mgrok.common.edit.store.MgrokEditStore.State

internal interface MgrokEditStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class SetText(val text: String) : Intent()
        data class SetDone(val isDone: Boolean) : Intent()
    }

    data class State(
        val text: String = "",
        val isDone: Boolean = false
    )

    sealed class Label {
        data class Changed(val item: MgrokItem) : Label()
    }
}

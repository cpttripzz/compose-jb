package me.zerskine.mgrok.common.main.store

import com.arkivanov.mvikotlin.core.store.Store
import me.zerskine.mgrok.common.main.MgrokItem
import me.zerskine.mgrok.common.main.store.MgrokMainStore.Intent
import me.zerskine.mgrok.common.main.store.MgrokMainStore.State

internal interface MgrokMainStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class SetItemDone(val id: Long, val isDone: Boolean) : Intent()
        data class DeleteItem(val id: Long) : Intent()
        data class SetText(val text: String) : Intent()
        object AddItem : Intent()
    }

    data class State(
        val items: List<MgrokItem> = emptyList(),
        val text: String = ""
    )
}

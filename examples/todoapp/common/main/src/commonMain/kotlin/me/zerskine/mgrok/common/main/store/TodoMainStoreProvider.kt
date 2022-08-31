package me.zerskine.mgrok.common.main.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.mainScheduler
import me.zerskine.mgrok.common.main.MgrokItem
import me.zerskine.mgrok.common.main.store.MgrokMainStore.Intent
import me.zerskine.mgrok.common.main.store.MgrokMainStore.State

internal class MgrokMainStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database
) {

    fun provide(): MgrokMainStore =
        object : MgrokMainStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "MgrokListStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ItemsLoaded(val items: List<MgrokItem>) : Msg()
        data class ItemDoneChanged(val id: Long, val isDone: Boolean) : Msg()
        data class ItemDeleted(val id: Long) : Msg()
        data class TextChanged(val text: String) : Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            database
                .updates
                .observeOn(mainScheduler)
                .map(Msg::ItemsLoaded)
                .subscribeScoped(onNext = ::dispatch)
        }

        override fun executeIntent(intent: Intent, getState: () -> State): Unit =
            when (intent) {
                is Intent.SetItemDone -> setItemDone(id = intent.id, isDone = intent.isDone)
                is Intent.DeleteItem -> deleteItem(id = intent.id)
                is Intent.SetText -> dispatch(Msg.TextChanged(text = intent.text))
                is Intent.AddItem -> addItem(state = getState())
            }

        private fun setItemDone(id: Long, isDone: Boolean) {
            dispatch(Msg.ItemDoneChanged(id = id, isDone = isDone))
            database.setDone(id = id, isDone = isDone).subscribeScoped()
        }

        private fun deleteItem(id: Long) {
            dispatch(Msg.ItemDeleted(id = id))
            database.delete(id = id).subscribeScoped()
        }

        private fun addItem(state: State) {
            if (state.text.isNotEmpty()) {
                dispatch(Msg.TextChanged(text = ""))
                database.add(text = state.text).subscribeScoped()
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ItemsLoaded -> copy(items = msg.items.sorted())
                is Msg.ItemDoneChanged -> update(id = msg.id) { copy(isDone = msg.isDone) }
                is Msg.ItemDeleted -> copy(items = items.filterNot { it.id == msg.id })
                is Msg.TextChanged -> copy(text = msg.text)
            }

        private inline fun State.update(id: Long, func: MgrokItem.() -> MgrokItem): State {
            val item = items.find { it.id == id } ?: return this

            return put(item.func())
        }

        private fun State.put(item: MgrokItem): State {
            val oldItems = items.associateByTo(mutableMapOf(), MgrokItem::id)
            val oldItem: MgrokItem? = oldItems.put(item.id, item)

            return copy(items = if (oldItem?.order == item.order) oldItems.values.toList() else oldItems.values.sorted())
        }

        private fun Iterable<MgrokItem>.sorted(): List<MgrokItem> = sortedByDescending(MgrokItem::order)
    }

    interface Database {
        val updates: Observable<List<MgrokItem>>

        fun setDone(id: Long, isDone: Boolean): Completable

        fun delete(id: Long): Completable

        fun add(text: String): Completable
    }
}

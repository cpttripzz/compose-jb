package me.zerskine.mgrok.common.edit.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.map
import com.badoo.reaktive.maybe.observeOn
import com.badoo.reaktive.scheduler.mainScheduler
import me.zerskine.mgrok.common.edit.MgrokItem
import me.zerskine.mgrok.common.edit.store.MgrokEditStore.Intent
import me.zerskine.mgrok.common.edit.store.MgrokEditStore.Label
import me.zerskine.mgrok.common.edit.store.MgrokEditStore.State

internal class MgrokEditStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database,
    private val id: Long
) {

    fun provide(): MgrokEditStore =
        object : MgrokEditStore, Store<Intent, State, Label> by storeFactory.create(
            name = "EditStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class Loaded(val item: MgrokItem) : Msg()
        data class TextChanged(val text: String) : Msg()
        data class DoneChanged(val isDone: Boolean) : Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            database
                .load(id = id)
                .map(Msg::Loaded)
                .observeOn(mainScheduler)
                .subscribeScoped(onSuccess = ::dispatch)
        }

        override fun executeIntent(intent: Intent, getState: () -> State) =
            when (intent) {
                is Intent.SetText -> setText(text = intent.text, state = getState())
                is Intent.SetDone -> setDone(isDone = intent.isDone, state = getState())
            }

        private fun setText(text: String, state: State) {
            dispatch(Msg.TextChanged(text = text))
            publish(Label.Changed(MgrokItem(text = text, isDone = state.isDone)))
            database.setText(id = id, text = text).subscribeScoped()
        }

        private fun setDone(isDone: Boolean, state: State) {
            dispatch(Msg.DoneChanged(isDone = isDone))
            publish(Label.Changed(MgrokItem(text = state.text, isDone = isDone)))
            database.setDone(id = id, isDone = isDone).subscribeScoped()
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.Loaded -> copy(text = msg.item.text, isDone = msg.item.isDone)
                is Msg.TextChanged -> copy(text = msg.text)
                is Msg.DoneChanged -> copy(isDone = msg.isDone)
            }
    }

    interface Database {
        fun load(id: Long): Maybe<MgrokItem>

        fun setText(id: Long, text: String): Completable

        fun setDone(id: Long, isDone: Boolean): Completable
    }
}

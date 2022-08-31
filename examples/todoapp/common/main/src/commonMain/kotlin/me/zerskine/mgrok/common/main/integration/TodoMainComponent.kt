package me.zerskine.mgrok.common.main.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import me.zerskine.mgrok.common.database.TodoSharedDatabase
import me.zerskine.mgrok.common.main.TodoMain
import me.zerskine.mgrok.common.main.TodoMain.Model
import me.zerskine.mgrok.common.main.TodoMain.Output
import me.zerskine.mgrok.common.main.store.TodoMainStore.Intent
import me.zerskine.mgrok.common.main.store.TodoMainStoreProvider
import me.zerskine.mgrok.common.utils.asValue
import me.zerskine.mgrok.common.utils.getStore

class TodoMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: TodoSharedDatabase,
    private val output: Consumer<Output>
) : TodoMain, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            TodoMainStoreProvider(
                storeFactory = storeFactory,
                database = TodoMainStoreDatabase(database = database)
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map(stateToModel)

    override fun onItemClicked(id: Long) {
        output(Output.Selected(id = id))
    }

    override fun onItemDoneChanged(id: Long, isDone: Boolean) {
        store.accept(Intent.SetItemDone(id = id, isDone = isDone))
    }

    override fun onItemDeleteClicked(id: Long) {
        store.accept(Intent.DeleteItem(id = id))
    }

    override fun onInputTextChanged(text: String) {
        store.accept(Intent.SetText(text = text))
    }

    override fun onAddItemClicked() {
        store.accept(Intent.AddItem)
    }
}

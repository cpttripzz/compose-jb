package me.zerskine.mgrok.common.edit.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import me.zerskine.mgrok.common.database.TodoSharedDatabase
import me.zerskine.mgrok.common.edit.TodoEdit
import me.zerskine.mgrok.common.edit.TodoEdit.Model
import me.zerskine.mgrok.common.edit.TodoEdit.Output
import me.zerskine.mgrok.common.edit.store.TodoEditStore.Intent
import me.zerskine.mgrok.common.edit.store.TodoEditStoreProvider
import me.zerskine.mgrok.common.utils.asValue
import me.zerskine.mgrok.common.utils.getStore

class TodoEditComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: TodoSharedDatabase,
    itemId: Long,
    private val output: Consumer<Output>
) : TodoEdit, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            TodoEditStoreProvider(
                storeFactory = storeFactory,
                database = TodoEditStoreDatabase(database = database),
                id = itemId
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map(stateToModel)

    override fun onTextChanged(text: String) {
        store.accept(Intent.SetText(text = text))
    }

    override fun onDoneChanged(isDone: Boolean) {
        store.accept(Intent.SetDone(isDone = isDone))
    }

    override fun onCloseClicked() {
        output(Output.Finished)
    }
}

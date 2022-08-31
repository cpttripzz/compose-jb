package me.zerskine.mgrok.common.edit.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import me.zerskine.mgrok.common.database.MgrokSharedDatabase
import me.zerskine.mgrok.common.edit.MgrokEdit
import me.zerskine.mgrok.common.edit.MgrokEdit.Model
import me.zerskine.mgrok.common.edit.MgrokEdit.Output
import me.zerskine.mgrok.common.edit.store.MgrokEditStore.Intent
import me.zerskine.mgrok.common.edit.store.MgrokEditStoreProvider
import me.zerskine.mgrok.common.utils.asValue
import me.zerskine.mgrok.common.utils.getStore

class MgrokEditComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: MgrokSharedDatabase,
    itemId: Long,
    private val output: Consumer<Output>
) : MgrokEdit, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            MgrokEditStoreProvider(
                storeFactory = storeFactory,
                database = MgrokEditStoreDatabase(database = database),
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

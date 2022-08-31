package me.zerskine.mgrok.common.root.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import me.zerskine.mgrok.common.database.MgrokSharedDatabase
import me.zerskine.mgrok.common.edit.MgrokEdit
import me.zerskine.mgrok.common.edit.integration.MgrokEditComponent
import me.zerskine.mgrok.common.main.MgrokMain
import me.zerskine.mgrok.common.main.integration.MgrokMainComponent
import me.zerskine.mgrok.common.root.MgrokRoot
import me.zerskine.mgrok.common.root.MgrokRoot.Child
import me.zerskine.mgrok.common.utils.Consumer

class MgrokRootComponent internal constructor(
    componentContext: ComponentContext,
    private val mgrokMain: (ComponentContext, Consumer<MgrokMain.Output>) -> MgrokMain,
    private val mgrokEdit: (ComponentContext, itemId: Long, Consumer<MgrokEdit.Output>) -> MgrokEdit
) : MgrokRoot, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        database: MgrokSharedDatabase
    ) : this(
        componentContext = componentContext,
        mgrokMain = { childContext, output ->
            MgrokMainComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                database = database,
                output = output
            )
        },
        mgrokEdit = { childContext, itemId, output ->
            MgrokEditComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                database = database,
                itemId = itemId,
                output = output
            )
        }
    )

    private val router =
        router<Configuration, Child>(
            initialConfiguration = Configuration.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Child>> = router.state

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child =
        when (configuration) {
            is Configuration.Main -> Child.Main(mgrokMain(componentContext, Consumer(::onMainOutput)))
            is Configuration.Edit -> Child.Edit(mgrokEdit(componentContext, configuration.itemId, Consumer(::onEditOutput)))
        }

    private fun onMainOutput(output: MgrokMain.Output): Unit =
        when (output) {
            is MgrokMain.Output.Selected -> router.push(Configuration.Edit(itemId = output.id))
        }

    private fun onEditOutput(output: MgrokEdit.Output): Unit =
        when (output) {
            is MgrokEdit.Output.Finished -> router.pop()
        }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Main : Configuration()

        @Parcelize
        data class Edit(val itemId: Long) : Configuration()
    }
}

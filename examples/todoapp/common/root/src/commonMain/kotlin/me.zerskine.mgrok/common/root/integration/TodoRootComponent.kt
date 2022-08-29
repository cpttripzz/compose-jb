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
import me.zerskine.mgrok.common.database.TodoSharedDatabase
import me.zerskine.mgrok.common.edit.TodoEdit
import me.zerskine.mgrok.common.edit.integration.TodoEditComponent
import me.zerskine.mgrok.common.main.TodoMain
import me.zerskine.mgrok.common.main.integration.TodoMainComponent
import me.zerskine.mgrok.common.root.TodoRoot
import me.zerskine.mgrok.common.root.TodoRoot.Child
import me.zerskine.mgrok.common.utils.Consumer

class TodoRootComponent internal constructor(
    componentContext: ComponentContext,
    private val todoMain: (ComponentContext, Consumer<TodoMain.Output>) -> TodoMain,
    private val todoEdit: (ComponentContext, itemId: Long, Consumer<TodoEdit.Output>) -> TodoEdit
) : TodoRoot, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        database: TodoSharedDatabase
    ) : this(
        componentContext = componentContext,
        todoMain = { childContext, output ->
            TodoMainComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                database = database,
                output = output
            )
        },
        todoEdit = { childContext, itemId, output ->
            TodoEditComponent(
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
            is Configuration.Main -> Child.Main(todoMain(componentContext, Consumer(::onMainOutput)))
            is Configuration.Edit -> Child.Edit(todoEdit(componentContext, configuration.itemId, Consumer(::onEditOutput)))
        }

    private fun onMainOutput(output: TodoMain.Output): Unit =
        when (output) {
            is TodoMain.Output.Selected -> router.push(Configuration.Edit(itemId = output.id))
        }

    private fun onEditOutput(output: TodoEdit.Output): Unit =
        when (output) {
            is TodoEdit.Output.Finished -> router.pop()
        }

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Main : Configuration()

        @Parcelize
        data class Edit(val itemId: Long) : Configuration()
    }
}

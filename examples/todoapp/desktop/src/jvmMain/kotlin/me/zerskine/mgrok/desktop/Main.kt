package me.zerskine.mgrok.desktop

import androidx.compose.desktop.DesktopTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.coroutinesinterop.asScheduler
import com.badoo.reaktive.scheduler.overrideSchedulers
import me.zerskine.mgrok.common.database.DefaultTodoSharedDatabase
import me.zerskine.mgrok.common.database.TodoDatabaseDriver
import me.zerskine.mgrok.common.root.TodoRoot
import me.zerskine.mgrok.common.root.integration.TodoRootComponent
import me.zerskine.mgrok.common.ui.TodoRootContent
import kotlinx.coroutines.Dispatchers

fun main() {
    overrideSchedulers(main = Dispatchers.Main::asScheduler)

    val lifecycle = LifecycleRegistry()
    val root = todoRoot(DefaultComponentContext(lifecycle = lifecycle))

    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Todo"
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                MaterialTheme {
                    DesktopTheme {
                        TodoRootContent(root)
                    }
                }
            }
        }
    }
}

private fun todoRoot(componentContext: ComponentContext): TodoRoot =
    TodoRootComponent(
        componentContext = componentContext,
        storeFactory = DefaultStoreFactory(),
        database = DefaultTodoSharedDatabase(TodoDatabaseDriver())
    )

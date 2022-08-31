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
import kotlinx.coroutines.Dispatchers
import me.zerskine.mgrok.common.database.DefaultMgrokSharedDatabase
import me.zerskine.mgrok.common.database.MgrokDatabaseDriver
import me.zerskine.mgrok.common.root.MgrokRoot
import me.zerskine.mgrok.common.root.integration.MgrokRootComponent
import me.zerskine.mgrok.common.ui.MgrokRootContent

fun main() {
    overrideSchedulers(main = Dispatchers.Main::asScheduler)

    val lifecycle = LifecycleRegistry()
    val root = mgrokRoot(DefaultComponentContext(lifecycle = lifecycle))

    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Mgrok"
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                MaterialTheme {
                    DesktopTheme {
                        MgrokRootContent(root)
                    }
                }
            }
        }
    }
}

private fun mgrokRoot(componentContext: ComponentContext): MgrokRoot =
    MgrokRootComponent(
        componentContext = componentContext,
        storeFactory = DefaultStoreFactory(),
        database = DefaultMgrokSharedDatabase(MgrokDatabaseDriver())
    )

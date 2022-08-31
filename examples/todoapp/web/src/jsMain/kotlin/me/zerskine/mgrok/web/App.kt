package me.zerskine.mgrok.web

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import me.zerskine.mgrok.common.database.DefaultMgrokSharedDatabase
import me.zerskine.mgrok.common.database.mgrokDatabaseDriver
import me.zerskine.mgrok.common.root.integration.MgrokRootComponent
import kotlinx.browser.document
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.compose.web.ui.Styles
import org.w3c.dom.HTMLElement

fun main() {
    val rootElement = document.getElementById("root") as HTMLElement

    val lifecycle = LifecycleRegistry()

    val root =
        MgrokRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            storeFactory = DefaultStoreFactory(),
            database = DefaultMgrokSharedDatabase(mgrokDatabaseDriver())
        )

    lifecycle.resume()

    renderComposable(root = rootElement) {
        Style(Styles)

        MgrokRootUi(root)
    }
}

package me.zerskine.mgrok.web

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import me.zerskine.mgrok.common.database.DefaultTodoSharedDatabase
import me.zerskine.mgrok.common.database.todoDatabaseDriver
import me.zerskine.mgrok.common.root.integration.TodoRootComponent
import kotlinx.browser.document
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.compose.web.ui.Styles

import org.w3c.dom.HTMLElement

fun main() {
    val rootElement = document.getElementById("root") as HTMLElement

    val lifecycle = LifecycleRegistry()

    val root =
        TodoRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            storeFactory = DefaultStoreFactory(),
            database = DefaultTodoSharedDatabase(todoDatabaseDriver())
        )

    lifecycle.resume()

    renderComposable(root = rootElement) {
        Style(Styles)

        TodoRootUi(root)
    }
}

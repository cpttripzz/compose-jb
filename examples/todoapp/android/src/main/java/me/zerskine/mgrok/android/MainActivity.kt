package me.zerskine.mgrok.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.timetravel.store.TimeTravelStoreFactory
import me.zerskine.mgrok.common.database.DefaultMgrokSharedDatabase
import me.zerskine.mgrok.common.database.MgrokDatabaseDriver
import me.zerskine.mgrok.common.root.MgrokRoot
import me.zerskine.mgrok.common.root.integration.MgrokRootComponent
import me.zerskine.mgrok.common.ui.MgrokRootContent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = MgrokRoot(defaultComponentContext())

        setContent {
            ComposeAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MgrokRootContent(root)
                }
            }
        }
    }

    private fun MgrokRoot(componentContext: ComponentContext): MgrokRoot =
        MgrokRootComponent(
            componentContext = componentContext,
            storeFactory = LoggingStoreFactory(TimeTravelStoreFactory()),
            database = DefaultMgrokSharedDatabase(MgrokDatabaseDriver(context = this))
        )
}

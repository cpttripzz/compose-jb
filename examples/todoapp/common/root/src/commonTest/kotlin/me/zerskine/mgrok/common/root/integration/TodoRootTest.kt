package me.zerskine.mgrok.common.root.integration

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.badoo.reaktive.base.invoke
import me.zerskine.mgrok.common.edit.MgrokEdit
import me.zerskine.mgrok.common.main.MgrokMain
import me.zerskine.mgrok.common.root.MgrokRoot
import me.zerskine.mgrok.common.root.MgrokRoot.Child
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class MgrokRootTest {

    private val lifecycle = LifecycleRegistry().apply { resume() }

    @Test
    fun WHEN_created_THEN_MgrokMain_displayed() {
        val root = root()

        assertTrue(root.activeChild is Child.Main)
    }

    @Test
    fun GIVEN_MgrokMain_displayed_WHEN_MgrokMain_Output_Selected_THEN_MgrokEdit_displayed() {
        val root = root()

        root.activeChild.asMgrokMain().output(MgrokMain.Output.Selected(id = 10L))

        assertTrue(root.activeChild is Child.Edit)
        assertEquals(10L, root.activeChild.asMgrokEdit().itemId)
    }

    @Test
    fun GIVEN_MgrokEdit_displayed_WHEN_MgrokEdit_Output_Finished_THEN_MgrokMain_displayed() {
        val root = root()
        root.activeChild.asMgrokMain().output(MgrokMain.Output.Selected(id = 10L))

        root.activeChild.asMgrokEdit().output(MgrokEdit.Output.Finished)

        assertTrue(root.activeChild is Child.Main)
    }

    private fun root(): MgrokRoot =
        MgrokRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            mgrokMain = { _, output -> MgrokMainFake(output) },
            mgrokEdit = { _, itemId, output -> MgrokEditFake(itemId, output) }
        )

    private val MgrokRoot.activeChild: Child get() = routerState.value.activeChild.instance

    private val Child.component: Any
        get() =
            when (this) {
                is Child.Main -> component
                is Child.Edit -> component
            }

    private fun Child.asMgrokMain(): MgrokMainFake = component as MgrokMainFake

    private fun Child.asMgrokEdit(): MgrokEditFake = component as MgrokEditFake
}

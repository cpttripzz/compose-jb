package me.zerskine.mgrok.common.main.integration

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.subject.publish.PublishSubject
import com.badoo.reaktive.test.observable.assertValue
import com.badoo.reaktive.test.observable.test
import com.badoo.reaktive.test.scheduler.TestScheduler
import me.zerskine.mgrok.common.database.TestMgrokSharedDatabase
import me.zerskine.mgrok.common.database.MgrokItemEntity
import me.zerskine.mgrok.common.main.MgrokItem
import me.zerskine.mgrok.common.main.MgrokMain.Model
import me.zerskine.mgrok.common.main.MgrokMain.Output
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TestFunctionName")
class MgrokMainTest {

    private val lifecycle = LifecycleRegistry()
    private val database = TestMgrokSharedDatabase(TestScheduler())
    private val outputSubject = PublishSubject<Output>()
    private val output = outputSubject.test()
    private val databaseTesting = database.testing

    private val impl by lazy {
        MgrokMainComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            storeFactory = DefaultStoreFactory(),
            database = database,
            output = outputSubject
        )
    }

    private val model: Model get() = impl.models.value

    @BeforeTest
    fun before() {
        overrideSchedulers(
            main = { TestScheduler() },
            io = { TestScheduler() }
        )

        databaseTesting.clear()
    }

    @Test
    fun WHEN_item_added_to_database_THEN_item_displayed() {
        databaseTesting.add("Item1")

        assertEquals("Item1", firstItem().text)
    }

    @Test
    fun WHEN_item_deleted_from_database_THEN_item_not_displayed() {
        databaseTesting.add("Item1")
        val id = lastInsertItem().id

        databaseTesting.delete(id = id)

        assertFalse(model.items.any { it.id == id })
    }

    @Test
    fun WHEN_item_clicked_THEN_Output_Selected_emitted() {
        databaseTesting.add("Item1")
        val id = firstItem().id

        impl.onItemClicked(id = id)

        output.assertValue(Output.Selected(id = id))
    }

    @Test
    fun GIVEN_item_isDone_false_WHEN_done_changed_to_true_THEN_item_isDone_true_in_database() {
        databaseTesting.add("Item1")
        val id = firstItem().id
        databaseTesting.setDone(id = id, isDone = false)

        impl.onItemDoneChanged(id = id, isDone = true)

        assertTrue(databaseTesting.selectRequired(id = id).isDone)
    }

    @Test
    fun GIVEN_item_isDone_true_WHEN_done_changed_to_false_THEN_item_isDone_false_in_database() {
        databaseTesting.add("Item1")
        val id = firstItem().id
        databaseTesting.setDone(id = id, isDone = true)

        impl.onItemDoneChanged(id = id, isDone = false)

        assertFalse(databaseTesting.selectRequired(id = id).isDone)
    }

    @Test
    fun WHEN_item_delete_clicked_THEN_item_deleted_in_database() {
        databaseTesting.add("Item1")
        val id = firstItem().id

        impl.onItemDeleteClicked(id = id)

        assertNull(databaseTesting.select(id = id))
    }

    @Test
    fun WHEN_item_text_changed_in_database_THEN_item_updated() {
        databaseTesting.add("Item1")
        val id = firstItem().id

        databaseTesting.setText(id = id, text = "New text")

        assertEquals("New text", firstItem().text)
    }

    @Test
    fun WHEN_input_text_changed_THEN_text_updated() {
        impl.onInputTextChanged(text = "Item text")

        assertEquals("Item text", model.text)
    }

    @Test
    fun GIVEN_input_text_entered_WHEN_add_item_clicked_THEN_item_added_in_database() {
        impl.onInputTextChanged(text = "Item text")

        impl.onAddItemClicked()

        assertEquals("Item text", lastInsertItem().text)
    }

    private fun firstItem(): MgrokItem = model.items[0]

    private fun lastInsertItem(): MgrokItemEntity =
        databaseTesting.selectRequired(id = requireNotNull(databaseTesting.getLastInsertId()))
}

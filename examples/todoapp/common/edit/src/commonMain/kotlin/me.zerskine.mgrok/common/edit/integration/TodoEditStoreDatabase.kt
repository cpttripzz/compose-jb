package me.zerskine.mgrok.common.edit.integration

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.map
import me.zerskine.mgrok.TodoItemEntity
import me.zerskine.mgrok.common.database.TodoSharedDatabase
import me.zerskine.mgrok.common.edit.TodoItem
import me.zerskine.mgrok.common.edit.store.TodoEditStoreProvider.Database

internal class TodoEditStoreDatabase(
    private val database: TodoSharedDatabase
) : Database {

    override fun load(id: Long): Maybe<TodoItem> =
        database
            .select(id = id)
            .map { it.toItem() }

    private fun TodoItemEntity.toItem(): TodoItem =
        TodoItem(
            text = text,
            isDone = isDone
        )

    override fun setText(id: Long, text: String): Completable =
        database.setText(id = id, text = text)

    override fun setDone(id: Long, isDone: Boolean): Completable =
        database.setDone(id = id, isDone = isDone)
}

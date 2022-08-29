package me.zerskine.mgrok.common.main.integration

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapIterable
import me.zerskine.mgrok.TodoItemEntity
import me.zerskine.mgrok.common.database.TodoSharedDatabase
import me.zerskine.mgrok.common.main.TodoItem
import me.zerskine.mgrok.common.main.store.TodoMainStoreProvider

internal class TodoMainStoreDatabase(
    private val database: TodoSharedDatabase
) : TodoMainStoreProvider.Database {

    override val updates: Observable<List<TodoItem>> =
        database
            .observeAll()
            .mapIterable { it.toItem() }

    private fun TodoItemEntity.toItem(): TodoItem =
        TodoItem(
            id = id,
            order = orderNum,
            text = text,
            isDone = isDone
        )

    override fun setDone(id: Long, isDone: Boolean): Completable =
        database.setDone(id = id, isDone = isDone)

    override fun delete(id: Long): Completable =
        database.delete(id = id)

    override fun add(text: String): Completable =
        database.add(text = text)
}

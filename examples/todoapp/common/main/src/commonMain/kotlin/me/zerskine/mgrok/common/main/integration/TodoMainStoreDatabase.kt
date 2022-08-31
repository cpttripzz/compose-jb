package me.zerskine.mgrok.common.main.integration

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapIterable
import me.zerskine.mgrok.common.database.MgrokItemEntity
import me.zerskine.mgrok.common.database.MgrokSharedDatabase
import me.zerskine.mgrok.common.main.MgrokItem
import me.zerskine.mgrok.common.main.store.MgrokMainStoreProvider

internal class MgrokMainStoreDatabase(
    private val database: MgrokSharedDatabase
) : MgrokMainStoreProvider.Database {

    override val updates: Observable<List<MgrokItem>> =
        database
            .observeAll()
            .mapIterable { it.toItem() }

    private fun MgrokItemEntity.toItem(): MgrokItem =
        MgrokItem(
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

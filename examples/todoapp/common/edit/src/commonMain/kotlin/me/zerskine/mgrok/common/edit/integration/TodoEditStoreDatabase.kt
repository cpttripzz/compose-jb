package me.zerskine.mgrok.common.edit.integration

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.map
import me.zerskine.mgrok.common.database.MgrokItemEntity
import me.zerskine.mgrok.common.database.MgrokSharedDatabase
import me.zerskine.mgrok.common.edit.MgrokItem
import me.zerskine.mgrok.common.edit.store.MgrokEditStoreProvider.Database

internal class MgrokEditStoreDatabase(
    private val database: MgrokSharedDatabase
) : Database {

    override fun load(id: Long): Maybe<MgrokItem> =
        database
            .select(id = id)
            .map { it.toItem() }

    private fun MgrokItemEntity.toItem(): MgrokItem =
        MgrokItem(
            text = text,
            isDone = isDone
        )

    override fun setText(id: Long, text: String): Completable =
        database.setText(id = id, text = text)

    override fun setDone(id: Long, isDone: Boolean): Completable =
        database.setDone(id = id, isDone = isDone)
}

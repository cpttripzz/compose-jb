package me.zerskine.mgrok.common.main.store

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completableFromFunction
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import me.zerskine.mgrok.common.main.MgrokItem

internal class TestMgrokMainStoreDatabase : MgrokMainStoreProvider.Database {

    private val subject = BehaviorSubject<List<MgrokItem>>(emptyList())

    var items: List<MgrokItem>
        get() = subject.value
        set(value) {
            subject.onNext(value)
        }

    override val updates: Observable<List<MgrokItem>> = subject

    override fun setDone(id: Long, isDone: Boolean): Completable =
        completableFromFunction {
            update(id = id) { copy(isDone = isDone) }
        }

    override fun delete(id: Long): Completable =
        completableFromFunction {
            this.items = items.filterNot { it.id == id }
        }

    override fun add(text: String): Completable =
        completableFromFunction {
            val id = items.maxByOrNull(MgrokItem::id)?.id?.inc() ?: 1L
            this.items += MgrokItem(id = id, order = id, text = text)
        }

    private fun update(id: Long, func: MgrokItem.() -> MgrokItem) {
        items = items.map { if (it.id == id) it.func() else it }
    }
}

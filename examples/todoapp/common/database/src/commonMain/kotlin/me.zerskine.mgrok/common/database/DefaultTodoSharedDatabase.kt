package me.zerskine.mgrok.common.database

import com.badoo.reaktive.base.setCancellable
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.observable.*
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.single.*
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlDriver
import me.zerskine.mgrok.TodoDatabaseQueries
import me.zerskine.mgrok.TodoItemEntity
import me.zerskine.mgrok.database.TodoDatabase

class DefaultTodoSharedDatabase(driver: Single<SqlDriver>) : TodoSharedDatabase {

    constructor(driver: SqlDriver) : this(singleOf(driver))

    private val queries: Single<TodoDatabaseQueries> =
        driver
            .map { TodoDatabase(it).todoDatabaseQueries }
            .asObservable()
            .replay()
            .autoConnect()
            .firstOrError()

    override fun observeAll(): Observable<List<TodoItemEntity>> =
        query(TodoDatabaseQueries::selectAll)
            .observe { it.executeAsList() }

    override fun select(id: Long): Maybe<TodoItemEntity> =
        query { it.select(id = id) }
            .mapNotNull { it.executeAsOneOrNull() }

    override fun add(text: String): Completable =
        execute { it.add(text = text) }

    override fun setText(id: Long, text: String): Completable =
        execute { it.setText(id = id, text = text) }

    override fun setDone(id: Long, isDone: Boolean): Completable =
        execute { it.setDone(id = id, isDone = isDone) }

    override fun delete(id: Long): Completable =
        execute { it.delete(id = id) }

    override fun clear(): Completable =
        execute { it.clear() }

    private fun <T : Any> query(query: (TodoDatabaseQueries) -> Query<T>): Single<Query<T>> =
        queries
            .observeOn(ioScheduler)
            .map(query)

    private fun execute(query: (TodoDatabaseQueries) -> Unit): Completable =
        queries
            .observeOn(ioScheduler)
            .doOnBeforeSuccess(query)
            .asCompletable()

    private fun <T : Any, R> Single<Query<T>>.observe(get: (Query<T>) -> R): Observable<R> =
        flatMapObservable { it.observed() }
            .observeOn(ioScheduler)
            .map(get)

    private fun <T : Any> Query<T>.observed(): Observable<Query<T>> =
        observable { emitter ->
            val listener =
                object : Query.Listener {
                    override fun queryResultsChanged() {
                        emitter.onNext(this@observed)
                    }
                }

            emitter.onNext(this@observed)
            addListener(listener)
            emitter.setCancellable { removeListener(listener) }
        }
}

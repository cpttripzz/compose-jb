package me.zerskine.mgrok.common.database

import com.badoo.reaktive.promise.asSingle
import com.badoo.reaktive.single.Single
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import me.zerskine.mgrok.database.MgrokDatabase

fun MgrokDatabaseDriver(): Single<SqlDriver> =
    initSqlDriver(MgrokDatabase.Schema).asSingle()

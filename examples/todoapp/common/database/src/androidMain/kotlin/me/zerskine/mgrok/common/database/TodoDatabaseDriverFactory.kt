package me.zerskine.mgrok.common.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import me.zerskine.mgrok.database.MgrokDatabase

@Suppress("FunctionName") // FactoryFunction
fun MgrokDatabaseDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = MgrokDatabase.Schema,
        context = context,
        name = "MgrokDatabase.db"
    )

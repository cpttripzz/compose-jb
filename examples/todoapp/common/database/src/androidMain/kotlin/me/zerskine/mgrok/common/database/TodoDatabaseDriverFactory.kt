package me.zerskine.mgrok.common.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import me.zerskine.mgrok.database.TodoDatabase

@Suppress("FunctionName") // FactoryFunction
fun TodoDatabaseDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = TodoDatabase.Schema,
        context = context,
        name = "TodoDatabase.db"
    )

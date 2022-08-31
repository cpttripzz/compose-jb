package me.zerskine.mgrok.common.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import me.zerskine.mgrok.database.MgrokDatabase

@Suppress("FunctionName") // Factory function
fun MgrokDatabaseDriver(): SqlDriver =
    NativeSqliteDriver(MgrokDatabase.Schema, "MgrokDatabase.db")

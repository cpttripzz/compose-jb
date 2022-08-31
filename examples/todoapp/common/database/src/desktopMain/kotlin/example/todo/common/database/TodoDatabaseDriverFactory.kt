package me.zerskine.mgrok.common.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import me.zerskine.mgrok.database.MgrokDatabase
import java.io.File

@Suppress("FunctionName") // FactoryFunction
fun MgrokDatabaseDriver(): SqlDriver {
    val databasePath = File(System.getProperty("java.io.tmpdir"), "ComposeMgrokDatabase.db")
    val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePath}")
    MgrokDatabase.Schema.create(driver)

    return driver
}

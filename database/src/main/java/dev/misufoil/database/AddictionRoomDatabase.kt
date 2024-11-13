package dev.misufoil.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.misufoil.database.dao.AddictionDao
import dev.misufoil.database.models.AddictionDBO

class AddictionDatabase internal constructor(private val database: AddictionRoomDatabase) {
    val addictionDao: AddictionDao
        get() = database.addictionsDao()
}

@Database(entities = [AddictionDBO::class], version = 1)
internal abstract class AddictionRoomDatabase : RoomDatabase() {

    abstract fun addictionsDao(): AddictionDao
}

fun addictionsDataBase(applicationContext: Context): AddictionDatabase {
    val addictionRoomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        AddictionRoomDatabase::class.java,
        "addictions"
    ).build()

    return AddictionDatabase(addictionRoomDatabase)
}
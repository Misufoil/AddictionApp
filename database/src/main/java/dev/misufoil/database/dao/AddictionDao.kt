package dev.misufoil.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.misufoil.database.models.AddictionDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface AddictionDao {

    @Query("SELECT * FROM AddictionDBO")
    suspend fun getAll(): List<AddictionDBO>

    @Query("SELECT * FROM AddictionDBO")
    fun observeAll(): Flow<List<AddictionDBO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(addiction: AddictionDBO)

    @Delete
    suspend fun remove(addiction: AddictionDBO)

    @Query("SELECT * FROM AddictionDBO WHERE id = :id")
    suspend fun getById(id: String): AddictionDBO?

    @Query("SELECT * FROM AddictionDBO WHERE type = :type")
    suspend fun getByType(type: String): AddictionDBO?

}
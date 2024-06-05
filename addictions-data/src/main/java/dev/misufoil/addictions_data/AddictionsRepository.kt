package dev.misufoil.addictions_data

import dev.misufoil.addictions_data.model.Addiction
import dev.misufoil.database.AddictionDatabase
import dev.misufoil.database.models.AddictionDBO
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class AddictionsRepository @Inject constructor(
    private val database: AddictionDatabase
) {

    fun getAll(): Flow<RequestResult<List<Addiction>>> {
        val localAllAddictions: Flow<RequestResult<List<Addiction>>> = getAllFromLocalDb()

        return localAllAddictions
    }

    private fun getAllFromLocalDb(): Flow<RequestResult<List<Addiction>>> {
        val dbRequest = database.addictionDao
            .observeAll()
            .map { RequestResult.Success(it) }

        val start = flowOf<RequestResult<List<AddictionDBO>>>(RequestResult.InProgress())

        return merge(dbRequest, start)
            .map { result ->
                result.map { addictionsDBO ->
                    addictionsDBO.map { it.toAddiction() }
                }
            }
    }

    suspend fun savaAddictionToLocalDb(addiction: Addiction) {
        database.addictionDao.insert(addiction.toAddictionDBO())
    }

}
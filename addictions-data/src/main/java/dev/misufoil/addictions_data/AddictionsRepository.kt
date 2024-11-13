package dev.misufoil.addictions_data

import dev.misufoil.addictions_data.model.Addiction
import dev.misufoil.core_utils.Logger
import dev.misufoil.database.AddictionDatabase
import dev.misufoil.database.models.AddictionDBO
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class AddictionsRepository @Inject constructor(
    private val database: AddictionDatabase,
    private val logger: Logger
) {

    fun getAll(): Flow<RequestResult<List<Addiction>>> {
        val localAllAddictions: Flow<RequestResult<List<Addiction>>> = getAllFromLocalDb()

        return localAllAddictions
    }

    private fun getAllFromLocalDb(): Flow<RequestResult<List<Addiction>>> {
        val dbRequest = database.addictionDao
            .observeAll()
            .map<List<AddictionDBO>, RequestResult<List<AddictionDBO>>> { RequestResult.Success(it) }
            .catch {
                logger.e(LOG_TAG, "Error getting from database. Cause = $it")
                emit(RequestResult.Error<List<AddictionDBO>>(error = it))
            }

        val start = flowOf<RequestResult<List<AddictionDBO>>>(RequestResult.InProgress())

        return merge(dbRequest, start)
            .map { result ->
                result.map { addictionsDBO ->
                    addictionsDBO.map { it.toAddiction() }
                }
            }
    }

    suspend fun getAddictionById(id: String): RequestResult<Addiction> {
        return try {
            val dbRequest = database.addictionDao.getById(id)
            if (dbRequest != null) {
                RequestResult.Success(dbRequest.toAddiction())
            } else {
                RequestResult.Error(null, NoSuchElementException("Addiction not found"))
            }
        } catch (e: Exception) {
            RequestResult.Error(null, e)
        }
    }

    suspend fun getAddictionByType(type: String): RequestResult<Addiction> {
        return try {
            val dbRequest = database.addictionDao.getByType(type)
            if (dbRequest != null) {
                RequestResult.Success(dbRequest.toAddiction())
            } else {
                RequestResult.Error(null, NoSuchElementException("Addiction not found"))
            }
        } catch (e: Exception) {
            RequestResult.Error(null, e)
        }
    }

    suspend fun savaAddictionToLocalDb(addiction: Addiction) {
        database.addictionDao.insert(addiction.toAddictionDBO())
    }

    suspend fun removeAddictionToLocalDb(addiction: Addiction) {
        database.addictionDao.remove(addiction.toAddictionDBO())
    }

    private companion object {
        const val LOG_TAG = "AddictionRepository"
    }

}


//3
//getAllFromLocalDb
//        val dbRequest = database.addictionDao::getAll.asFlow()
//            .map { RequestResult.Success(it) }
//            .catch {
//                RequestResult.Error<List<AddictionDBO>>(error = it)
//                logger.e(LOG_TAG, "Error getting from database. Cause = $it")
//            }
//
//        val start = flowOf<RequestResult<List<AddictionDBO>>>(RequestResult.InProgress())
//
//        return merge(dbRequest, start)
//            .map { result ->
//                result.map { addictionsDBO ->
//                    addictionsDBO.map { it.toAddiction() }
//                }
//            }

// 2        return flow {
//            emit(RequestResult.InProgress())
//            database.addictionDao.observeAll()
//                .catch {
//                    RequestResult.Error<List<AddictionDBO>>(error = it)
//                    logger.e(LOG_TAG, "Error getting from database. Cause = $it")
//                }
//                .collect{ addictionsDBO ->
//                    val addictions = addictionsDBO.map { it.toAddiction() }
//                    emit(RequestResult.Success(addictions))
//                }
//        }
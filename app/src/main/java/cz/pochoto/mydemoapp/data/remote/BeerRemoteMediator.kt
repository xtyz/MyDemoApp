package cz.pochoto.mydemoapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import cz.pochoto.mydemoapp.data.local.BeerDatabase
import cz.pochoto.mydemoapp.data.local.BeerEntity
import cz.pochoto.mydemoapp.data.mappers.toBeerEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val beerDatabase: BeerDatabase,
    private val beerApi: BeerApi
) : RemoteMediator<Int, BeerEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.REFRESH -> 1
            }

            val beers = beerApi.getBeers(page = loadKey, pageCount = state.config.pageSize)

            beerDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    beerDatabase.dao.clearAll()
                }
                val beerEntities = beers.map { it.toBeerEntity() }
                beerDatabase.dao.upsertAll(beerEntities)
            }

            MediatorResult.Success(endOfPaginationReached = beers.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
package cz.pochoto.mydemoapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.withTransaction
import cz.pochoto.mydemoapp.data.local.BeerDatabase
import cz.pochoto.mydemoapp.data.local.BeerEntity
import cz.pochoto.mydemoapp.data.mappers.toBeer
import cz.pochoto.mydemoapp.data.remote.BeerApi
import cz.pochoto.mydemoapp.data.remote.BeerRemoteMediator
import cz.pochoto.mydemoapp.domain.Beer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeerRepository @Inject constructor(
    private val beerDatabase: BeerDatabase,
    private val beerApi: BeerApi
) : BaseRepository(), IBeerRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getBeerPager(): Pager<Int, BeerEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = BeerRemoteMediator(beerDatabase, beerApi),
            pagingSourceFactory = {
                beerDatabase.dao.pagingSource()
            }
        )
    }

    override suspend fun getBeer(id: Int): Beer? = beerDatabase.withTransaction {
        beerDatabase.dao.getBeer(id)?.toBeer()
    }

}
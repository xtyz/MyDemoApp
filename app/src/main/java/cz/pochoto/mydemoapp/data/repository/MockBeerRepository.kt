package cz.pochoto.mydemoapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import cz.pochoto.mydemoapp.data.local.BeerEntity
import cz.pochoto.mydemoapp.data.mappers.toBeerEntity
import cz.pochoto.mydemoapp.domain.Beer

class MockBeerRepository : BaseRepository(), IBeerRepository {

    private val beer = Beer(
        id = 1,
        name = "Beer",
        tagline = "Super tag",
        firstBrewed = "Never",
        description = "Description of super Beer",
        imageUrl = null,
    )

    @OptIn(ExperimentalPagingApi::class)
    override fun getBeerPager(): Pager<Int, BeerEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = object : RemoteMediator<Int, BeerEntity>() {
                override suspend fun load(
                    loadType: LoadType,
                    state: PagingState<Int, BeerEntity>
                ): MediatorResult {
                   return  MediatorResult.Success(endOfPaginationReached = true)
                }
            },
            pagingSourceFactory = {
                object : PagingSource<Int, BeerEntity>(){
                    override fun getRefreshKey(state: PagingState<Int, BeerEntity>): Int? {
                       return null
                    }

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BeerEntity> {
                        return LoadResult.Page(listOf(beer.toBeerEntity(), beer.toBeerEntity()), 0,0,1,0 )
                    }

                }
            }
        )
    }

    override suspend fun getBeer(id: Int): Beer = beer



}
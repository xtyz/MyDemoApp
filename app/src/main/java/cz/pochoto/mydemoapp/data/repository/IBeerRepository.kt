package cz.pochoto.mydemoapp.data.repository

import androidx.paging.Pager
import cz.pochoto.mydemoapp.data.local.BeerEntity
import cz.pochoto.mydemoapp.domain.Beer

interface IBeerRepository {

    fun getBeerPager(): Pager<Int, BeerEntity>

    suspend fun getBeer(id: Int): Beer?
}
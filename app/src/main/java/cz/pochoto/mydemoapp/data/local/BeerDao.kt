package cz.pochoto.mydemoapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface BeerDao {

    @Upsert
    suspend fun upsertAll(beers: List<BeerEntity>)

    @Query("SELECT * FROM beerentity")
    fun pagingSource(): PagingSource<Int, BeerEntity>

    @Query("SELECT * FROM beerentity WHERE id=:id")
    fun getBeer(id: Int): BeerEntity?

    @Query("DELETE FROM beerentity")
    suspend fun clearAll()
}

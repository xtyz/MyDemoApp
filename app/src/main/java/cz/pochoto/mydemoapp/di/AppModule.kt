package cz.pochoto.mydemoapp.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import cz.pochoto.mydemoapp.data.local.BeerDatabase
import cz.pochoto.mydemoapp.data.local.BeerEntity
import cz.pochoto.mydemoapp.data.remote.BeerApi
import cz.pochoto.mydemoapp.data.remote.BeerRemoteMediator
import cz.pochoto.mydemoapp.data.repository.BeerRepository
import cz.pochoto.mydemoapp.data.repository.IBeerRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBeerDatabase(@ApplicationContext context: Context): BeerDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = BeerDatabase::class.java,
            name = "beers.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBeerApi(): BeerApi {
        return Retrofit.Builder()
            .baseUrl(BeerApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(BeerApi::class.java)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideBeerPager(beerDatabase: BeerDatabase, beerApi: BeerApi): Pager<Int, BeerEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = BeerRemoteMediator(beerDatabase, beerApi),
            pagingSourceFactory = {
                beerDatabase.dao.pagingSource()
            }
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBeerRepository(beerRepository: BeerRepository): IBeerRepository
}
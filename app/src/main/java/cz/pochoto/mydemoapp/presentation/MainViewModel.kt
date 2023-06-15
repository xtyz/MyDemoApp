package cz.pochoto.mydemoapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import cz.pochoto.mydemoapp.data.mappers.toBeer
import cz.pochoto.mydemoapp.data.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    beerRepository: BeerRepository
) : ViewModel() {

    val beerPagingFlow = beerRepository.getBeerPager().flow.map { pagingData ->
        pagingData.map { it.toBeer() }
    }.cachedIn(viewModelScope)

}

package cz.pochoto.mydemoapp.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cz.pochoto.mydemoapp.data.repository.IBeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    beerRepository: IBeerRepository,
) : ViewModel() {

    private val state = savedStateHandle.getStateFlow<Int?>("id", null)

    var beerFlow = state.map { id ->
        id?.let {
            beerRepository.getBeer(it)
        }
    }
}

package com.alirezahr.products.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alirezahr.products.data.remote.base.Resource
import com.alirezahr.products.domain.usecase.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProductListState())
    val state: StateFlow<ProductListState> = _state

    fun handleIntent(intent: ProductListIntent) {
        when (intent) {
            is ProductListIntent.LoadProduct -> loadProduct()
        }
    }

    private fun loadProduct() {
        viewModelScope.launch {
            getProductUseCase.invoke().collect { res ->
                when (res.status) {
                    Resource.Status.LOADING -> _state.update { it.copy(isLoading = true) }
                    Resource.Status.SUCCESS -> _state.update {
                        it.copy(
                            isLoading = false,
                            products = res.data ?: emptyList()
                        )
                    }

                    Resource.Status.ERROR -> _state.update {
                        it.copy(
                            isLoading = false,
                            error = res.message
                        )
                    }
                }
            }
        }
    }
}
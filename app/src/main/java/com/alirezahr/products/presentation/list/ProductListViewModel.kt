package com.alirezahr.products.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alirezahr.products.data.remote.base.Resource
import com.alirezahr.products.domain.usecase.GetProductListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductUseCase: GetProductListUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProductListState())
    val state: StateFlow<ProductListState> = _state
    private var hasLoaded = false
    fun handleIntent(intent: ProductListIntent) {
        when (intent) {
            is ProductListIntent.LoadProduct -> {
                if (!hasLoaded) {
                    loadProduct()
                }
            }
            is ProductListIntent.SearchChanged -> {
                searchProduct(intent.query)
            }
        }
    }

    private fun loadProduct() {
        viewModelScope.launch {
            getProductUseCase.invoke().collect { res ->
                when (res.status) {
                    Resource.Status.LOADING -> _state.update { it.copy(isLoading = true) }
                    Resource.Status.SUCCESS -> {
                        _state.update {
                        it.copy(
                            isLoading = false,
                            products = res.data ?: emptyList()
                        )
                    }
                        hasLoaded = true
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

    private fun searchProduct(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }
}
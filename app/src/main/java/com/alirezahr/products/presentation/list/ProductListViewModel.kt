package com.alirezahr.products.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alirezahr.products.data.remote.base.Resource
import com.alirezahr.products.domain.model.Product
import com.alirezahr.products.domain.usecase.GetProductListUseCase
import com.alirezahr.products.domain.usecase.ShowOnlyBookMarkProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductUseCase: GetProductListUseCase,
    private val showOnlyBookMarkProductUseCase: ShowOnlyBookMarkProductUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProductListState())

    val state: StateFlow<ProductListState> = _state

    private var fullProductList: List<Product> = emptyList()

    fun handleIntent(intent: ProductListIntent) {
        when (intent) {
            is ProductListIntent.LoadProduct -> {
                if (!_state.value.hasLoaded) {
                    loadProduct()
                }
            }

            is ProductListIntent.SearchChanged -> {
                searchProduct(intent.query)
            }

            is ProductListIntent.SwitchChange -> {
                if (!_state.value.isBookMark) {
                    switchBookMark()
                } else {
                    _state.update { it.copy(products = fullProductList, isBookMark = false) }
                }
            }
        }
    }

    private fun loadProduct() {
        viewModelScope.launch {
            getProductUseCase.invoke().collect { res ->
                when (res.status) {
                    Resource.Status.LOADING -> _state.update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    Resource.Status.SUCCESS -> {
                        fullProductList = res.data ?: emptyList()

                        _state.update {
                        it.copy(
                            isLoading = false,
                            products = res.data ?: emptyList(),
                            hasLoaded = true
                        )
                    }
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

    private fun switchBookMark() {
        viewModelScope.launch {
            showOnlyBookMarkProductUseCase.invoke().collect { bookmarkProductList ->

                _state.update {
                    it.copy(
                        products = bookmarkProductList,
                        isBookMark = !_state.value.isBookMark
                    )
                }
            }
        }
    }
}
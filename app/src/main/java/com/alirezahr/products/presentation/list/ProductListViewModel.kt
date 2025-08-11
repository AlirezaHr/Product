package com.alirezahr.products.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alirezahr.products.data.remote.base.Resource
import com.alirezahr.products.domain.model.Product
import com.alirezahr.products.domain.usecase.GetProductListUseCase
import com.alirezahr.products.domain.usecase.ShowOnlyBookMarkProductUseCase
import com.alirezahr.products.data.remote.base.mapError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private var bookmarkJob: Job? = null

    fun handleIntent(intent: ProductListIntent) {
        when (intent) {
            is ProductListIntent.LoadProduct -> {
                if (!_state.value.hasLoaded) {
                    loadProduct()
                }
            }

            is ProductListIntent.SearchChanged -> {
                _state.update { it.copy(searchQuery = intent.query) }
            }

            is ProductListIntent.SwitchChange -> {
                toggleBookmark()
            }

            ProductListIntent.RetryClick -> {
                loadProduct()
            }

            ProductListIntent.TogglePriceSort -> {togglePriceSort()}
        }
    }

    private fun loadProduct() {
        viewModelScope.launch {
            getProductUseCase.invoke().collect { res ->
                when (res.status) {
                    Resource.Status.LOADING -> {
                        _state.update {
                            it.copy(isLoading = true, error = null)
                        }
                    }

                    Resource.Status.SUCCESS -> {
                        fullProductList = res.data ?: emptyList()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                products = fullProductList,
                                hasLoaded = true
                            )
                        }
                    }

                    Resource.Status.ERROR -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = mapError(message = res.message)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun toggleBookmark() {
        val newIsBookMark = !_state.value.isBookMark
        bookmarkJob?.cancel()

        if (newIsBookMark) {
            bookmarkJob = viewModelScope.launch {
                showOnlyBookMarkProductUseCase.invoke().collect { bookmarkProductList ->
                    _state.update {
                        it.copy(
                            isBookMark = true,
                            products = bookmarkProductList
                        )
                    }
                }
            }
        } else {
            _state.update {
                it.copy(
                    isBookMark = false,
                    products = fullProductList
                )
            }
        }
    }

    private fun togglePriceSort() {
        val current = _state.value.priceSortOrder
        val next = when (current) {
            PriceSortOrder.NONE -> PriceSortOrder.ASCENDING
            PriceSortOrder.ASCENDING -> PriceSortOrder.DESCENDING
            PriceSortOrder.DESCENDING -> PriceSortOrder.NONE
        }
        _state.update { it.copy(priceSortOrder = next) }
    }
}

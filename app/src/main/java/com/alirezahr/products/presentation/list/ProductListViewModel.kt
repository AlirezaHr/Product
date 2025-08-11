package com.alirezahr.products.presentation.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alirezahr.products.data.remote.base.Resource
import com.alirezahr.products.domain.model.Product
import com.alirezahr.products.domain.usecase.GetProductListUseCase
import com.alirezahr.products.domain.usecase.ShowOnlyBookMarkProductUseCase
import com.alirezahr.products.presentation.mapError
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
    private var bookmarkJob: Job? = null

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
                _state.update { it.copy(isBookMark = !it.isBookMark) }

                if (_state.value.isBookMark) {
                    switchBookMark()
                } else {
                    bookmarkJob?.cancel()
                    _state.update { it.copy(products = fullProductList) }
                }
            }

            ProductListIntent.RetryClick -> {
                loadProduct()
            }
        }
    }

    private fun loadProduct() {
        viewModelScope.launch {
            getProductUseCase.invoke().collect { res ->
                Log.d("LLLA", res.code.toString())

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
                            error = mapError(message = res.message)
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
        bookmarkJob?.cancel()
        bookmarkJob = viewModelScope.launch {
            showOnlyBookMarkProductUseCase.invoke().collect { bookmarkProductList ->
                _state.update {
                    it.copy(products = bookmarkProductList)
                }
            }
        }
    }
}
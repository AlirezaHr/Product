package com.alirezahr.products.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alirezahr.products.domain.usecase.GetProductListByIdUseCase
import com.alirezahr.products.domain.usecase.UpdateBookMarkProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val productByIdUseCase: GetProductListByIdUseCase,
    private val updateProductUseCase: UpdateBookMarkProductUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(DetailProductState())
    val state: StateFlow<DetailProductState> = _state

    fun handleIntent(intent: DetailProductIntent) {
        when (intent) {
            is DetailProductIntent.LoadDetailProduct -> loadProductFromDB(intent.productId)
            is DetailProductIntent.BookMarkClick -> updateBookMark()
            is DetailProductIntent.OnBackClick -> onBackPress()
        }
    }

    private fun loadProductFromDB(productId: Int) {
        viewModelScope.launch {
            productByIdUseCase.invoke(productId)
                .collect { product ->
                    _state.update { it.copy(product = product) }
                }
        }
    }

    private fun updateBookMark() {
        viewModelScope.launch(Dispatchers.IO) {
            updateProductUseCase.invoke(
                _state.value.product?.isBookMark!!,
                _state.value.product?.id!!
            )
        }
    }

    private fun onBackPress() {
        _state.update { it.copy(onBackClicked = true) }
    }
}